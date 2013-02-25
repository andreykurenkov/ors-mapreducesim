package mapreducesim.execution;

import mapreducesim.core.MapReduceSimMain;
import mapreducesim.execution.tasks.WorkTask;

import org.simgrid.msg.Host;
import org.simgrid.msg.Msg;
import org.simgrid.msg.MsgException;

public class SimpleReduceProcess extends WorkerProcess {

	public final double failureRate = 0.001;

	public SimpleReduceProcess(Host host, String name, String mailbox, TaskTrackerProcess parent, WorkTask task) {
		super(host, name, mailbox, parent, task);
	}

	@Override
	public void main(String[] arg0) throws MsgException {
		// TODO: fill out with Merge, retrive, write
		Msg.info(this.getHost().getName() + " starting WorkTask" + task + " at " + this.getTimeElapsed());
		double timeToWork = task.WORK_AMOUNT / (int) this.getHost().getSpeed();
		while (timeToWork > 0) {
			timeToWork -= MapReduceSimMain.SIM_STEP;
		}
		Msg.info(this.getHost().getName() + " finishing WorkTask" + task + " at " + this.getTimeElapsed());
		parent.notifyReduceFinish();
	}

}