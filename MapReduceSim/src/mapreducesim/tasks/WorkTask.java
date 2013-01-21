package mapreducesim.tasks;

import mapreducesim.execution.WorkerProcess;

import org.simgrid.msg.Task;

public abstract class WorkTask extends Task {

	public abstract long calculateWorkLength(WorkerProcess executeIn);
}
