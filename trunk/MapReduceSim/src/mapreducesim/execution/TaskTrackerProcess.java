package mapreducesim.execution;

import mapreducesim.core.MapReduceSimMain;
import mapreducesim.core.SimProcess;
import mapreducesim.execution.tasks.HeartbeatTask;
import mapreducesim.execution.tasks.WorkTask;
import mapreducesim.interfaces.JobTrackerInterface;
import mapreducesim.util.SafeParsing;

import org.simgrid.msg.Host;
import org.simgrid.msg.HostNotFoundException;
import org.simgrid.msg.Msg;
import org.simgrid.msg.MsgException;
import org.simgrid.msg.Task;

public class TaskTrackerProcess extends SimProcess {
	private int numMapSlots, numMapRunning;
	private int numReduceSlots, numReduceRunning;

	private int mapCount, reduceCount;

	private int timeUntilNextHeartbeat;

	/**
	 * Default constructor for Processes needs to be used as part of framework.
	 * 
	 * @param host
	 * @param name
	 * @param args
	 */
	public TaskTrackerProcess(Host host, String name, String[] args) {
		super(host, name, args);
	}

	@Override
	public void main(String[] args) {
		if (args.length > 0)
			numMapSlots = SafeParsing.safeIntParse(args[0], 2, "args[0] (int numMap) wrong format for TaskTracker at "
					+ this.getHost());
		else
			numMapSlots = 20;

		if (args.length > 1)
			numReduceSlots = SafeParsing.safeIntParse(args[1], 2, "args[1] (int numReduce) wrong format for TaskTracker at "
					+ this.getHost());
		else
			numReduceSlots = 5;

		timeUntilNextHeartbeat = 0;

		while (!finished) {
			try {
				if (timeUntilNextHeartbeat <= 0) {
					(new HeartbeatTask(this)).send(JobTrackerInterface.MAILBOX);
					timeUntilNextHeartbeat = JobTrackerInterface.HEARTBEAT_INTERVAL;
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			try {
				Task task = super.checkTask(MapReduceSimMain.SIM_STEP);
				if (task != null)
					handleTask(task);
			} catch (MsgException e) { // e.printStackTrace(); timeUntilNextHeartbeat -= MapReduceSimMain.SIM_STEP; //
				Msg.info("waiting...");
			}
		}
	}
	public boolean hasMapSlots() {
		return numMapSlots > numMapRunning;
	}

	public boolean hasReduceSlots() {
		return numMapSlots > numReduceRunning;
	}

	protected WorkerProcess getMapperProcess(Host host, String name, WorkTask workTask) {
		return new SimpleMapperProcess(host, name, "Mapper for " + workTask.getID(), this, workTask);
	}

	protected WorkerProcess getReducerProcess(Host host, String name, WorkTask workTask) {
		return new SimpleReduceProcess(host, name, "Reducer for " + workTask.getID(), this, workTask);
	}

	protected void notifyMapFinish() {
		mapCount--;
	}

	protected void notifyReduceFinish() {
		mapCount--;
	}

	protected void handleTask(Task received) {
		Msg.info(this.getHost().getName() + " handling " + received);
		if (received instanceof WorkTask) {
			WorkTask workTask = (WorkTask) received;
			WorkerProcess process = null;
			switch (workTask.getType()) {
			case MAP:
				if (hasMapSlots()) {
					numMapRunning++;
					mapCount++;
					process = getMapperProcess(host, this.getHost().getName() + " Mapper " + mapCount, workTask);
				}
				break;
			case REDUCE:
				if (hasMapSlots()) {
					numReduceRunning++;
					reduceCount++;
					process = getReducerProcess(host, this.getHost().getName() + " Reducer " + reduceCount, workTask);
				}
				break;
			}
			if (process != null) {
				try {
					process.start();
				} catch (HostNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/** Boiler plate getters
	 * 
	 */
	public int getNumMapSlots(){
		return this.numMapSlots;
	}
	public int getNumReduceSlots(){
		return this.numReduceSlots;
	}
	public int getNumMapRunning(){
		return this.numMapRunning;
	}
	public int getNumReduceRunning(){
		return this.numReduceRunning;
	}

}
