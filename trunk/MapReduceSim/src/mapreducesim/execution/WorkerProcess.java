package mapreducesim.execution;

import mapreducesim.core.SimProcess;
import mapreducesim.tasks.WorkTask;

import org.simgrid.msg.Host;
import org.simgrid.msg.MsgException;
import org.simgrid.msg.Process;

//TODO: error stuff and such
public abstract class WorkerProcess extends SimProcess {
	protected TaskTrackerProcess parent;
	protected WorkTask task;
	public final double failureRate = 0.001;

	public WorkerProcess(Host host, String name, TaskTrackerProcess parent, WorkTask task) {
		super(host, name);
		this.parent = parent;
		this.timeElapsed = parent.getTimeElapsed();
		this.task = task;
	}

}
