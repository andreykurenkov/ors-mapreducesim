package mapreducesim.execution;

import mapreducesim.core.SimProcess;
import mapreducesim.execution.TaskTrackerProcess.HeartbeatTask;
import mapreducesim.interfaces.JobTrackerInterface;
import mapreducesim.storage.File.FileLocation;
import mapreducesim.tasks.WorkTask;
import mapreducesim.tasks.WorkTask.Type;

import org.simgrid.msg.Host;
import org.simgrid.msg.MsgException;
import org.simgrid.msg.Task;

public class TestJobTracker extends SimProcess {

	public TestJobTracker(Host host, String name, String[] args) {
		super(host, name, args, JobTrackerInterface.MAILBOX);
	}

	@Override
	public void main(String[] arg0) throws MsgException {
		while (!finished) {
			Task received = Task.receive(MAILBOX);
			if (received instanceof HeartbeatTask) {
				TaskTrackerProcess from = ((HeartbeatTask) received).from;
				if (from.hasMapSlots()) {
					(new WorkTask(5, Type.MAP, new FileLocation())).send(from.MAILBOX);
				}
			}
		}

	}
}
