package mapreducesim.scheduling.test;

import java.util.List;
import java.util.Set;

import mapreducesim.core.ConfigurableClass;
import mapreducesim.execution.TaskRunnerProcess;
import mapreducesim.execution.tasks.HeartbeatTask;
import mapreducesim.execution.tasks.WorkTask;
import mapreducesim.scheduling.FileSplitter;
import mapreducesim.scheduling.MapReduceJobSpecification;
import mapreducesim.scheduling.NotifyNoMoreTasks;
import mapreducesim.scheduling.TaskCacheEntry;
import mapreducesim.scheduling.JobSubmission;
import mapreducesim.scheduling.SchedulerProcess;
import mapreducesim.scheduling.TaskPool;
import mapreducesim.scheduling.FileSplitter.InputSplit;
import mapreducesim.scheduling.TaskCacheEntry.Type;
import mapreducesim.storage.FileBlockLocation;

import org.simgrid.msg.*;

/**
 * Task.
 * 
 * input: list of tasks, each with a list of node ids (preferred locations)
 * 
 * @author tdoneal
 * 
 */

public class FIFOScheduler extends SchedulerProcess {

	SimpleJobStatus currentJob;

	public FIFOScheduler(Host host, String name, String[] args) {
		super(host, name, args);

	}

	/**
	 * 
	 * 1st priority: node-local task 2nd priority: rack-local task
	 * 
	 * a "level" how many switches tasktracker needs to go through to find the data
	 * 
	 * framework function: given a job, tasktracker, and a locality level, return a task matching the criteria or null if
	 * none was found
	 * 
	 */

	public void assignTasks(TaskRunnerProcess process) {
		String src = process.getHost().getName();
		Msg.info("FIFOScheduler received heartbeat from: " + process.getHost().getName());

		int availMapSlots = process.getNumMapSlots() - process.getNumMapRunning();
		int availReduceSlots = process.getNumReduceSlots() - process.getNumReduceRunning();

		Msg.info(src + " has " + availMapSlots + " map slots available and " + availReduceSlots + " reduce slots available");
		Msg.info("The current job has " + this.currentJob.tasks.getNumNotStartedMap() + " map tasks and "
				+ this.currentJob.tasks.getNumNotStartedReduce() + " reduce tasks not started");

		// simple scheduling algorithm
		boolean mapTasksLeft = true;
		boolean reduceTasksLeft = true;
		boolean assignedAny = false;

		// for each map slot available on the task tracker
		for (int i = 0; i < process.getNumMapSlots() - process.getNumMapRunning(); i++) {
			// pick out an appropriate map task
			TaskCacheEntry mapTask = pickMapTask();
			// assign task to that tasktracker
			if (mapTask == null) {
				mapTasksLeft = false;
			} else {

				WorkTask wt = new WorkTask(0.0, WorkTask.Type.MAP, mapTask.taskData);
				// send the task to the tasktracker
				try {
					Msg.info("Assigning map task " + wt + " to task tracker " + process.getHost().getName());
					wt.send(process.MAILBOX);

					// update local cache
					mapTask.status.statusType = TaskCacheEntry.StatusType.ASSIGNED;
					mapTask.status.taskTrackerRunningOn = process.getHost().getName();

					assignedAny = true;

				} catch (TransferFailureException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (HostFailureException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TimeoutException e) {
					e.printStackTrace();
				}
			}
		}

		// for each reduce slot available on the task tracker
		for (int i = 0; i < process.getNumReduceSlots() - process.getNumReduceRunning(); i++) {
			// pick out an appropriate map task
			TaskCacheEntry reduceTask = pickReduceTask();
			// assign task to that tasktracker
			if (reduceTask == null) {
				reduceTasksLeft = false;
			} else {
				WorkTask wt = new WorkTask(0.0, WorkTask.Type.REDUCE, reduceTask.taskData);
				// send the task to the tasktracker
				try {
					Msg.info("Assigning reduce task " + wt + " to task tracker " + process.getHost().getName());
					wt.send(process.MAILBOX);
					// update local cache
					reduceTask.status.statusType = TaskCacheEntry.StatusType.ASSIGNED;
					reduceTask.status.taskTrackerRunningOn = process.getHost().getName();

					assignedAny = true;

				} catch (TransferFailureException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (HostFailureException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TimeoutException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// if no more map or reduce tasks left, notify the task tracker
			if (!assignedAny) {
				Msg.info("Notifying task tracker " + src + " that there are no more tasks available");
				NotifyNoMoreTasks notify = new NotifyNoMoreTasks();
				try {
					assignedAny = true;
					notify.send(process.MAILBOX);
				} catch (TransferFailureException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (HostFailureException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TimeoutException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		dumpTaskPool();

	}

	private void dumpTaskPool() {
		Msg.info("FIFO scheduler task pool:");
		List<TaskCacheEntry> tasks = this.currentJob.tasks.getAsList();
		for (int i = 0; i < tasks.size(); i++) {
			TaskCacheEntry tce = tasks.get(i);
			Msg.info(tce + "");
		}
	}

	/** bare-bones task picker **/
	public TaskCacheEntry pickMapTask() {
		for (int i = 0; i < this.currentJob.tasks.getAsList().size(); i++) {
			TaskCacheEntry task = this.currentJob.tasks.getAsList().get(i);
			if (task.type == Type.MAP && task.status.statusType == TaskCacheEntry.StatusType.NOTSTARTED) {
				return task;
			}
		}
		return null;
	}

	/** bare-bones task picker **/
	public TaskCacheEntry pickReduceTask() {
		for (int i = 0; i < this.currentJob.tasks.getAsList().size(); i++) {
			TaskCacheEntry task = this.currentJob.tasks.getAsList().get(i);
			if (task.type == Type.REDUCE && task.status.statusType == TaskCacheEntry.StatusType.NOTSTARTED) {
				return task;
			}
		}
		return null;
	}

	/**
	 * Creates a jobstatus object given a jobsubmission object
	 * 
	 * @param j
	 * @return
	 */
	public SimpleJobStatus createNewJobStatus(JobSubmission j) {
		TaskPool taskPool = new TaskPool();
		MapReduceJobSpecification mrj = j.jobToRun;

		// add the map tasks
		for (int i = 0; i < mrj.getOriginalMapTasks().size(); i++) {
			// add the task with no preferred location for now
			TaskCacheEntry tce = new TaskCacheEntry(TaskCacheEntry.Type.MAP, TaskCacheEntry.StatusType.NOTSTARTED);
			// annotate the cache entry with its input split
			tce.taskData = mrj.getOriginalMapTasks().get(i).taskData;
			taskPool.addTask(tce, null);
		}
		// add the reduce tasks
		for (int i = 0; i < mrj.getOriginalReduceTasks().size(); i++) {
			// add the task with no preferred location for now
			taskPool.addTask(new TaskCacheEntry(TaskCacheEntry.Type.REDUCE, TaskCacheEntry.StatusType.NOTSTARTED), null);
		}
		SimpleJobStatus retVal = new SimpleJobStatus(mrj.getName(), taskPool);
		return retVal;
	}

	@Override
	public void onJobSubmissionReceived(JobSubmission js) {
		// create a job status data structure for tracking the status of this
		// job,
		// and set it as our "current" job
		this.currentJob = createNewJobStatus(js);
		Msg.info("New job submission received (job name '" + this.currentJob.getJobName() + "')");
	}

}
