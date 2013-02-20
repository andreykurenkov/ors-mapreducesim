package mapreducesim.execution.test;

import mapreducesim.core.SimProcess;
import mapreducesim.interfaces.JobTrackerInterface;

import org.simgrid.msg.Host;
import org.simgrid.msg.Msg;
import org.simgrid.msg.MsgException;

public class TestJobTracker extends SimProcess {

	public TestJobTracker(Host host, String name, String[] args) {
		super(host, name, args, JobTrackerInterface.MAILBOX);
	}

	@Override
	public void main(String[] arg0) throws MsgException {
		while (true) {
			Msg.info("2");
		}
		/*
		 * int totalToMap = 20; while (!finished) { try { Task received = Task.receive(MAILBOX, 150); if (received instanceof
		 * HeartbeatTask) { TaskTrackerProcess from = ((HeartbeatTask) received).from; if (from.hasMapSlots()) { (new
		 * WorkTask(50, Type.MAP, new FileLocation())).send(from.MAILBOX); if (totalToMap > 0) { totalToMap--; } else {
		 * from.finish(); } } } } catch (TimeoutException e) { this.finish(); } }
		 */
	}
}
