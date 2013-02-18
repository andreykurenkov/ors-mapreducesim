package mapreducesim.execution;

import org.simgrid.msg.Msg;
import org.simgrid.msg.Task;


	public class HeartbeatTask extends Task {
		public final double sentAtTime;
		public final TaskTrackerProcess from;
		public final int numMapSlotsLeft;
		public final int numReduceSlotsLeft;

		public HeartbeatTask(TaskTrackerProcess sourceProcess) {
			sentAtTime = Msg.getClock();
			from = sourceProcess;
			numMapSlotsLeft = from.getNumMapSlots() - from.getNumMapRunning();
			numReduceSlotsLeft = from.getNumReduceSlots() - from.getNumReduceRunning();
		}
	}

