package mapreducesim.scheduling;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.simgrid.msg.Host;
import org.simgrid.msg.Msg;
import org.simgrid.msg.MsgException;
import org.simgrid.msg.Task;
import org.simgrid.msg.TimeoutException;

import mapreducesim.execution.HeartbeatTask;

public abstract class Scheduler extends org.simgrid.msg.Process{
	
	/**
	 * Maintained by the scheduler, keeps track of known TaskTrackers in the simulation
	 */
	private Map<String,TaskTrackerCacheEntry> taskTrackerCache;
	
	
	public Scheduler(Host host, String name, String[] args){
		super(host,name,args);
		taskTrackerCache = new HashMap<String,TaskTrackerCacheEntry>();
	}
	public abstract void onHeartbeatReceived(HeartbeatTask h);
	public abstract void onJobSubmissionReceived(JobSubmission js);
	
	@Override
	public void main(String[] arg0) throws MsgException {
		Msg.info("Jobtracker waiting for simgrid tasks on mailbox '" + getHost().getName() + "'");
		// receive first task
		Task taskReceived = Task.receive(getHost().getName());

		while (true) {
			onTaskReceived(taskReceived);
			// continue receiving tasks, or exit if no more tasks received within 0.5s
			try {
				taskReceived = Task.receive(getHost().getName(), 0.5);

			} catch (TimeoutException te) {
				Msg.info("Scheduler hasn't received any messages in 0.5s.  Exiting.");
				break;
			}
		}
	}
	
	public void onTaskReceived(Task task){
		if (task instanceof JobSubmission) {
			JobSubmission js = (JobSubmission) task;
			Msg.info("Job tracker received job submission: " + js.getJobName());
			onJobSubmissionReceived((JobSubmission)js);
		} else if (task instanceof HeartbeatTask) {
			Msg.info("Job tracker received heartbeat from " + task.getSource().getName());
			HeartbeatTask hb = (HeartbeatTask)task;
			
			
			// // maintain the tasktracker cache
			String sourceName = task.getSource().getName();
			//if this task tracker wasn't in the cache
			if (!taskTrackerCache.containsKey(sourceName)){
				//add it to the cache
				taskTrackerCache.put(sourceName, new TaskTrackerCacheEntry(sourceName,hb.numMapSlotsLeft,hb.numReduceSlotsLeft));
				Msg.info("Current task tracker cache: "+taskTrackerCache);
			}
			//now, update the cached information
			TaskTrackerCacheEntry entry = taskTrackerCache.get(sourceName);
			entry.bestKnownMapSlotsAvailable = hb.numMapSlotsLeft;
			entry.bestKnownReduceSlotsAvailable = hb.numReduceSlotsLeft;
			//finished updating the cache
			
			//invoke the callback method for the actual scheduling algorithm
			onHeartbeatReceived(hb);
			
		} else {
			Msg.info("Job tracker ignoring unknown task received: " + task);
		}
	}
	
	public Map<String,TaskTrackerCacheEntry> getTaskTrackerCache(){
		return this.taskTrackerCache;
	}
	
	
}
