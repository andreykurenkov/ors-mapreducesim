package mapreducesim.execution.test;

import mapreducesim.core.SimConfig;
import mapreducesim.core.SimMain;
import mapreducesim.core.SimProcess;
import mapreducesim.execution.TaskRunnerProcess;
import mapreducesim.execution.tasks.HeartbeatTask;
import mapreducesim.execution.tasks.WorkTask;
import mapreducesim.execution.tasks.WorkTask.Type;
import mapreducesim.scheduling.SchedulerProcess;
import mapreducesim.scheduling.FileSplitter.InputSplit;
import mapreducesim.storage.FileBlockLocation;
import mapreducesim.util.xml.XMLDocument;
import mapreducesim.util.xml.XMLElement;

import org.simgrid.msg.Host;
import org.simgrid.msg.MsgException;
import org.simgrid.msg.Task;
import org.simgrid.msg.TimeoutException;

public class TestJobTracker extends SimProcess {
	private static int tasklength;
	static {
		tasklength = Integer.parseInt(SimConfig.getConfigurationElementText("tasklength", "25"));
	}

	public TestJobTracker(Host host, String name, String[] args) {
		super(host, name, args, SchedulerProcess.SCHEDULER_MAILBOX);
	}

	@Override
	public void main(String[] arg0) throws MsgException {

		int totalToMap = 20;
		int runnersfinished = 0;
		while (!finished) {
			try {
				Task received = Task.receive(SchedulerProcess.SCHEDULER_MAILBOX, SchedulerProcess.getHeartbeatInterval());
				if (received instanceof HeartbeatTask) {
					TaskRunnerProcess from = ((HeartbeatTask) received).from;
					if (from.hasMapSlots()) {
						if (totalToMap > 0) {
							totalToMap--;
						} else {
							runnersfinished++;
							from.finish();
						}
						(new WorkTask(tasklength, Type.MAP, new InputSplit())).send(from.MAILBOX);

					}
				}
			} catch (TimeoutException e) {
				if (runnersfinished == 3)
					this.finish();
			}
		}

	}
}
