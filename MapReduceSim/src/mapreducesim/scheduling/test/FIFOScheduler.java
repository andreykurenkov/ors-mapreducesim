package mapreducesim.scheduling.test;

import java.util.Set;

import mapreducesim.execution.TaskRunnerProcess;
import mapreducesim.execution.tasks.HeartbeatTask;
import mapreducesim.execution.tasks.WorkTask;
import mapreducesim.scheduling.TaskCacheEntry;
import mapreducesim.scheduling.JobStatus;
import mapreducesim.scheduling.JobSubmission;
import mapreducesim.scheduling.SchedulerProcess;
import mapreducesim.scheduling.TaskPool;
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

	JobStatus currentJob;

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
		Msg.info("FIFOScheduler received heartbeat from: " + process.getHost().getName());

		// simple scheduling algorithm

		// for each map slot available on the task tracker
		for (int i = 0; i < process.getNumMapSlots() - process.getNumMapRunning(); i++) {
			// pick out an appropriate map task
			TaskCacheEntry mapTask = pickMapTask();
			// assign task to that tasktracker
			if (mapTask == null) {
				// no more map tasks left
			} else {

				WorkTask wt = new WorkTask(0.0, WorkTask.Type.MAP, mapTask.taskData);
				// send the task to the tasktracker
				try {
					Msg.info("Assigning map task " + wt + " to task tracker " + process.getHost().getName());
					wt.send(process.getHost().getName());

					// update local cache
					mapTask.status.statusType = TaskCacheEntry.StatusType.ASSIGNED;
					mapTask.status.taskTrackerRunningOn = process.getHost().getName();

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

		// for each reduce slot available on the task tracker
		for (int i = 0; i < process.getNumReduceSlots() - process.getNumReduceRunning(); i++) {
			// pick out an appropriate map task
			TaskCacheEntry reduceTask = pickReduceTask();
			// assign task to that tasktracker
			if (reduceTask == null) {
				// no more reduce tasks left
			} else {
				WorkTask wt = new WorkTask(0.0, WorkTask.Type.REDUCE, reduceTask.taskData);
				// send the task to the tasktracker
				try {
					Msg.info("Assigning reduce task " + wt + " to task tracker " + process.getHost().getName());
					wt.send(process.getHost().getName());
					// update local cache
					reduceTask.status.statusType = TaskCacheEntry.StatusType.ASSIGNED;
					reduceTask.status.taskTrackerRunningOn = process.getHost().getName();
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

	public JobStatus createNewJobStatus(JobSubmission js) {
		TaskPool taskPool = new TaskPool();
		// add the map tasks
		for (int i = 0; i < js.numMapTasks; i++) {
			// add the task with no preferred location for now
			taskPool.addTask(new TaskCacheEntry(TaskCacheEntry.Type.MAP, TaskCacheEntry.StatusType.NOTSTARTED), null);
		}
		// add the reduce tasks
		for (int i = 0; i < js.numReduceTasks; i++) {
			// add the task with no preferred location for now
			taskPool.addTask(new TaskCacheEntry(TaskCacheEntry.Type.REDUCE, TaskCacheEntry.StatusType.NOTSTARTED), null);
		}
		JobStatus retVal = new JobStatus(js.jobName, taskPool);
		return retVal;
	}

	@Override
	public void onJobSubmissionReceived(JobSubmission js) {
		// create a job status data structure for tracking the status of this job,
		// and set it as our "current" job
		this.currentJob = createNewJobStatus(js);
		Msg.info("New job submission received (job name '" + this.currentJob.getJobName() + "')");
	}

}
