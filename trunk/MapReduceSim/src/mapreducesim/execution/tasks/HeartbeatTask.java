package mapreducesim.execution.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mapreducesim.execution.TaskRunnerProcess;
import mapreducesim.storage.FileBlock;

import org.simgrid.msg.Msg;
import org.simgrid.msg.Task;

/**
 * Heartbeat simulation to be sent from TaskRunnerProcess to the SchedulerProcess to indicate amounts of open slots for work
 * as well as completed and failed tasks.
 * 
 * @author Andrey Kurenkov
 * @version 1.0 Mar 13, 2013
 */
public class HeartbeatTask extends Task {
	public final double sentAtTime;
	public final TaskRunnerProcess from;
	public final int numMapSlotsLeft;
	public final int numReduceSlotsLeft;
	public final Map<WorkTask, List<FileBlock>> completedTasks;
	public final List<WorkTask> failed;

	/**
	 * Constructor for HeartbeatTask that gives values to the instance variables corresponding to the parameters.
	 * 
	 * @param sourceProcess
	 * @param completed
	 * @param failed
	 */
	public HeartbeatTask(TaskRunnerProcess sourceProcess, Map<WorkTask, List<FileBlock>> completed, List<WorkTask> failed) {
		sentAtTime = Msg.getClock();
		this.failed = failed;
		completedTasks = completed;
		from = sourceProcess;
		numMapSlotsLeft = from.getNumMapSlots() - from.getNumMapRunning();
		numReduceSlotsLeft = from.getNumReduceSlots() - from.getNumReduceRunning();
	}

	/**
	 * 
	 * Constructor for HeartbeatTask that gives values to the instance variables corresponding to the parameters and leaves
	 * the failed list empty (non-null).
	 * 
	 * @param sourceProcess
	 * @param completed
	 */
	public HeartbeatTask(TaskRunnerProcess sourceProcess, Map<WorkTask, List<FileBlock>> completed) {
		this(sourceProcess, completed, new ArrayList<WorkTask>());
	}
}
