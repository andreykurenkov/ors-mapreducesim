package mapreducesim.execution;

import java.util.ArrayList;

import org.simgrid.msg.Host;
import org.simgrid.msg.HostFailureException;
import org.simgrid.msg.HostNotFoundException;
import org.simgrid.msg.Msg;
import org.simgrid.msg.MsgException;
import org.simgrid.msg.Task;
import org.simgrid.msg.TimeoutException;
import org.simgrid.msg.TransferFailureException;
import org.simgrid.msg.Process;

import mapreducesim.core.MapReduceSimMain;
import mapreducesim.core.SimFile;
import mapreducesim.core.SimProcess;
import mapreducesim.tasks.WorkTask;
import mapreducesim.util.SafeParsing;

public class TaskTrackerProcess extends SimProcess {
	private int numMapSlots;
	private int numReduceSlots;
	private int numMapRunning;
	private int numReduceRunning;

	private int timeUntilNextHeartbeat;

	public TaskTrackerProcess(Host host, String name) {
		super(host, name);
	}

	@Override
	public void main(String[] args) {
		if (args.length > 0)
			numMapSlots = SafeParsing.safeIntParse(args[0], 2, "args[0] (int numMap) wrong format for TaskTracker at "
					+ this.getHost());
		else
			numMapSlots = 40;

		if (args.length > 1)
			numReduceSlots = SafeParsing.safeIntParse(args[1], 2, "args[1] (int numReduce) wrong format for TaskTracker at "
					+ this.getHost());
		else
			numReduceSlots = 5;

		timeUntilNextHeartbeat = 0;

		while (!finished) {
			try {
				if (timeUntilNextHeartbeat <= 0) {
					(new HeartbeatTask()).send(JobTrackerInterface.MAILBOX);
					timeUntilNextHeartbeat = JobTrackerInterface.HEARTBEAT_INTERVAL;
				}
			} catch (TransferFailureException | HostFailureException | TimeoutException e1) {
				e1.printStackTrace();
			}

			try {
				Task task = super.stepAndCheckTask();
				timeUntilNextHeartbeat -= MapReduceSimMain.SIM_STEP;
				if (task != null)
					handleTask(task);
			} catch (MsgException e) {
				e.printStackTrace();
			}

		}
	}

	public boolean hasMapSlots() {
		return numMapSlots > numMapRunning;
	}

	public boolean hasReduceSlots() {
		return numMapSlots > numReduceRunning;
	}

	protected void handleTask(Task received) {
		if (received instanceof WorkTask) {
			WorkTask workTask = (WorkTask) received;
			String name = null;
			switch (workTask.getType()) {
			case MAP:
				if (hasMapSlots()) {
					numMapRunning++;
					name = this.getHost().getName() + " Mapper " + numMapRunning;
				}
				break;
			case REDUCE:
				if (hasMapSlots()) {
					numReduceRunning++;
					name = this.getHost().getName() + " Reducer " + numReduceRunning;
				}
				break;
			}
			if (name != null) {
				try {
					Process work = workTask.getExecutionProcess(this.getHost(), name, this);
					work.start();
				} catch (HostNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public class HeartbeatTask extends Task {
		public final int sentAtTime;
		public final TaskTrackerProcess from;
		public final int numMapSlotsLeft;
		public final int numReduceSlotsLeft;

		public HeartbeatTask() {
			from = TaskTrackerProcess.this;
			sentAtTime = from.timeElapsed;
			numMapSlotsLeft = from.numMapSlots - from.numMapRunning;
			numReduceSlotsLeft = from.numReduceSlots - from.numReduceRunning;
		}
	}

}
