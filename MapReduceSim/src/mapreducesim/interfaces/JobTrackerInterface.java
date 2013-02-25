package mapreducesim.interfaces;

import mapreducesim.execution.tasks.WorkTask;

public interface JobTrackerInterface {
	public final static String MAILBOX = "JobTracker";
	public final static String HOST_NAME = MAILBOX;
	public final static int HEARTBEAT_INTERVAL = 100;

}
