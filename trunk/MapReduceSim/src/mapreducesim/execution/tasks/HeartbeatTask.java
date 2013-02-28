package mapreducesim.execution.tasks;

import mapreducesim.execution.TaskRunnerProcess;

import org.simgrid.msg.Msg;
import org.simgrid.msg.Task;


	public class HeartbeatTask extends Task {
		public final double sentAtTime;
		public final TaskRunnerProcess from;
		public final int numMapSlotsLeft;
		public final int numReduceSlotsLeft;

		public HeartbeatTask(TaskRunnerProcess sourceProcess) {
			sentAtTime = Msg.getClock();
			from = sourceProcess;
			numMapSlotsLeft = from.getNumMapSlots() - from.getNumMapRunning();
			numReduceSlotsLeft = from.getNumReduceSlots() - from.getNumReduceRunning();
		}
	}

