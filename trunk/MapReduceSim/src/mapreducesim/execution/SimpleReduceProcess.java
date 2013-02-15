package mapreducesim.execution;

import org.simgrid.msg.Host;
import org.simgrid.msg.Msg;
import org.simgrid.msg.MsgException;

import mapreducesim.core.MapReduceSimMain;
import mapreducesim.core.SimProcess;
import mapreducesim.tasks.WorkTask;

public class SimpleReduceProcess extends WorkerProcess {

	private TaskTrackerProcess parent;
	public final double failureRate = 0.001;

	public SimpleReduceProcess(Host host, String name, String mailbox, TaskTrackerProcess parent, WorkTask task) {
		super(host, name, mailbox, parent, task);
	}

	@Override
	public void main(String[] arg0) throws MsgException {
		// TODO: fill out with Merge, retrive, write
		Msg.info(this.getHost().getName() + " starting WorkTask" + task + " at " + this.getTimeElapsed());
		long timeToWork = task.WORK_AMOUNT / (int) this.getHost().getSpeed();
		while (timeToWork > 0) {
			timeToWork -= MapReduceSimMain.SIM_STEP;
		}
		Msg.info(this.getHost().getName() + " finishing WorkTask" + task + " at " + this.getTimeElapsed());
		this.kill();
	}

}
