package mapreducesim.execution.test;

import mapreducesim.core.SimProcess;
import mapreducesim.execution.TaskTrackerProcess;
import mapreducesim.execution.tasks.HeartbeatTask;
import mapreducesim.execution.tasks.WorkTask;
import mapreducesim.execution.tasks.WorkTask.Type;
import mapreducesim.interfaces.JobTrackerInterface;
import mapreducesim.storage.File.FileLocation;

import org.simgrid.msg.Host;
import org.simgrid.msg.MsgException;
import org.simgrid.msg.Task;
import org.simgrid.msg.TimeoutException;

public class TestJobTracker extends SimProcess {

	public TestJobTracker(Host host, String name, String[] args) {
		super(host, name, args, JobTrackerInterface.MAILBOX);
	}

	@Override
	public void main(String[] arg0) throws MsgException {

		int totalToMap = 20;
		while (!finished) {
			try {
				Task received = Task.receive(MAILBOX, 150);
				if (received instanceof HeartbeatTask) {
					TaskTrackerProcess from = ((HeartbeatTask) received).from;
					if (from.hasMapSlots()) {
						(new WorkTask(50, Type.MAP, new FileLocation())).send(from.MAILBOX);
						if (totalToMap > 0) {
							totalToMap--;
						} else {
							from.finish();
						}
					}
				}
			} catch (TimeoutException e) {
				this.finish();
			}
		}

	}
}
