package mapreducesim.scheduling.test;


import java.util.Set;

import mapreducesim.execution.HeartbeatTask;
import mapreducesim.scheduling.HadoopTaskCacheEntry;
import mapreducesim.scheduling.JobStatus;
import mapreducesim.scheduling.JobSubmission;
import mapreducesim.scheduling.Scheduler;
import mapreducesim.scheduling.TaskPool;
import mapreducesim.scheduling.TaskTrackerCacheEntry;

import org.simgrid.msg.*;

public class FIFOScheduler extends Scheduler {
	
	JobStatus currentJob;
		
	
	public FIFOScheduler(Host host, String name, String[] args) {
		super(host, name, args);
		
	}

	
	
	
	
	public void onHeartbeatReceived(HeartbeatTask heartbeat){
		Msg.info("FIFOScheduler received heartbeat from: "+heartbeat.from.getHost().getName());
		
		//simple scheduling algorithm
		
		//for each map slot available on the task tracker
		for (int i = 0;i<heartbeat.numMapSlotsLeft;i++){
			//pick out an appropriate map task
			HadoopTaskCacheEntry mapTask = pickMapTask();
		}
		
		
	}
	
	/**bare-bones task picker**/
	public HadoopTaskCacheEntry pickMapTask(){
		return this.currentJob.tasks.getArbitrary();
	}
	/**bare-bones task picker**/
	public HadoopTaskCacheEntry pickReduceTask(){
		return this.currentJob.tasks.getArbitrary();
	}
	
	public JobStatus createNewJobStatus(JobSubmission js){
		TaskPool taskPool = new TaskPool();
		//add the map tasks
		for (int i = 0;i<js.numMapTasks;i++){
			//add the task with no preferred location for now
			taskPool.addTask(new HadoopTaskCacheEntry(HadoopTaskCacheEntry.Type.MAP), null);
		}
		//add the reduce tasks
		for (int i = 0;i<js.numReduceTasks;i++){
			//add the task with no preferred location for now
			taskPool.addTask(new HadoopTaskCacheEntry(HadoopTaskCacheEntry.Type.REDUCE), null);
		}
		JobStatus retVal = new JobStatus(js.jobName,taskPool);
		return retVal;
	}

	@Override
	public void onJobSubmissionReceived(JobSubmission js) {
		// create a job status data structure for tracking the status of this job,
		// and set it as our "current" job
		this.currentJob = createNewJobStatus(js);
		Msg.info("New job submission received (job name '"+this.currentJob.getJobName()+"')");
	}
}
