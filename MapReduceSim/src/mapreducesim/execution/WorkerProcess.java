package mapreducesim.execution;

import mapreducesim.core.SimProcess;
import mapreducesim.execution.tasks.WorkTask;

import org.simgrid.msg.Host;
import org.simgrid.msg.Msg;
import org.simgrid.msg.MsgException;
import org.simgrid.msg.Process;

//TODO: error stuff and such
public abstract class WorkerProcess extends SimProcess {
	protected TaskRunnerProcess parent;
	protected WorkTask task;
	public final double failureRate = 0.001;
	protected double progress;

	public WorkerProcess(Host host, String name, String mailbox, TaskRunnerProcess parent, WorkTask task) {
		super(host, name, null, mailbox);
		this.parent = parent;
		this.timeSlept = parent.getTimeElapsed();
		this.task = task;
	}

	public double getProgress() {
		return progress;
	}

}
