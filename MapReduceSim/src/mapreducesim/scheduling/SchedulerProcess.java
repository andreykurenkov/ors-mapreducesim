package mapreducesim.scheduling;

import java.util.HashMap;
import java.util.Map;

import mapreducesim.core.SimConfig;
import mapreducesim.core.SimProcess;
import mapreducesim.execution.TaskRunnerProcess;
import mapreducesim.execution.tasks.HeartbeatTask;
import mapreducesim.storage.StorageProcess;

import org.simgrid.msg.Host;
import org.simgrid.msg.Msg;
import org.simgrid.msg.MsgException;
import org.simgrid.msg.Task;
import org.simgrid.msg.TimeoutException;

public abstract class SchedulerProcess extends SimProcess {

	/**
	 * Maintained by the scheduler, keeps track of known TaskTrackers in the
	 * simulation
	 */
	private Map<String, TaskTrackerCacheEntry> taskTrackerCache;

	public static String SCHEDULER_MAILBOX = "JobTracker";

	private static int heartbeatInterval = 100;
	protected static JobMaker jobMaker;
	static {
		String beat = SimConfig.getElementText("heartbeat", "100");
		setHeartbeatInterval(Integer.parseInt(beat));
	}

	public SchedulerProcess(Host host, String name, String[] args) {
		super(host, name, args, SCHEDULER_MAILBOX);
		taskTrackerCache = new HashMap<String, TaskTrackerCacheEntry>();
	}

	public abstract void assignTasks(TaskRunnerProcess t);

	public abstract void onJobSubmissionReceived(JobSubmission js);

	@Override
	public void main(String[] arg0) throws MsgException {
		Msg.info("Jobtracker waiting for simgrid tasks on mailbox '"
				+ getHost().getName() + "'");
		// receive first task
		Task taskReceived = Task.receive(this.MAILBOX);

		while (true) {
			onTaskReceived(taskReceived);
			try {
				taskReceived = Task.receive(this.MAILBOX, heartbeatInterval + 10);
			} catch (TimeoutException te) {
				NotifyNoMoreTasks notify = new NotifyNoMoreTasks();
				notify.send(StorageProcess.DEFAULT_STORAGE_MAILBOX);
				break;
			}

		}

		onSimulationFinish();

	}

	public abstract void onSimulationFinish();

	public void onTaskReceived(Task task) {
		if (task instanceof JobSubmission) {
			JobSubmission js = (JobSubmission) task;
			Msg.info("Job tracker received job submission: "
					+ js.jobToRun.getName());
			onJobSubmissionReceived((JobSubmission) js);
		} else if (task instanceof HeartbeatTask) {

			onHeartbeatReceived((HeartbeatTask) task);
		} else {
			Msg.info("Job tracker ignoring unknown task received: " + task);
		}
	}

	public Map<String, TaskTrackerCacheEntry> getTaskTrackerCache() {
		return this.taskTrackerCache;
	}

	void onHeartbeatReceived(HeartbeatTask task) {
		Msg.info("Job tracker received heartbeat from "
				+ task.getSource().getName());
		HeartbeatTask hb = (HeartbeatTask) task;

		// // maintain the tasktracker cache
		String sourceName = task.getSource().getName();
		// if this task tracker wasn't in the cache
		if (!taskTrackerCache.containsKey(sourceName)) {
			// add it to the cache
			taskTrackerCache.put(sourceName, new TaskTrackerCacheEntry(
					sourceName, hb.numMapSlotsLeft, hb.numReduceSlotsLeft));
			Msg.info("Current task tracker cache: " + taskTrackerCache);
		}
		// now, update the cached information
		TaskTrackerCacheEntry entry = taskTrackerCache.get(sourceName);
		entry.bestKnownMapSlotsAvailable = hb.numMapSlotsLeft;
		entry.bestKnownReduceSlotsAvailable = hb.numReduceSlotsLeft;
		// finished updating the cache

		// health monitoring stuff

		// invoke the callback method for the actual scheduling algorithm
		assignTasks(hb.from);

	}

	/**
	 * Getter for the heartbeatInterval
	 * 
	 * @return the heartbeatInterval
	 */
	public static int getHeartbeatInterval() {
		return heartbeatInterval;
	}

	/**
	 * @param heartbeatInterval
	 *            the new value of type int to make heartbeatInterval
	 */
	protected static void setHeartbeatInterval(int heartbeatInterval) {
		SchedulerProcess.heartbeatInterval = heartbeatInterval;
	}

}
