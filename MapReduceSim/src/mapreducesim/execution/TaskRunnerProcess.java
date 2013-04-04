package mapreducesim.execution;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mapreducesim.core.ConfigurableClass;
import mapreducesim.core.SimConfig;
import mapreducesim.core.SimProcess;
import mapreducesim.execution.tasks.HeartbeatTask;
import mapreducesim.execution.tasks.WorkTask;
import mapreducesim.scheduling.NotifyNoMoreTasks;
import mapreducesim.scheduling.SchedulerProcess;
import mapreducesim.storage.FileBlock;
import mapreducesim.util.SafeParsing;

import org.simgrid.msg.Host;
import org.simgrid.msg.HostNotFoundException;
import org.simgrid.msg.Msg;
import org.simgrid.msg.MsgException;
import org.simgrid.msg.Task;

/**
 * A parallel to Hadoop's TaskTracker, this communicates with Scheduler (which is similar to JobTracker) and handles the
 * running of map/reduce tasks.
 * 
 * @author Andrey Kurenkov
 * @version 1.0 Mar 1, 2013
 */
public class TaskRunnerProcess extends SimProcess {
	private int numMapSlots, numMapRunning;
	private int numReduceSlots, numReduceRunning;

	private int mapCount, reduceCount;

	private double timeUntilNextHeartbeat;

	private Map<WorkTask, List<FileBlock>> completed;

	private static WorkTaskTimer workTimer;
	static {
		workTimer = ConfigurableClass.instantiateFromSimConfig(WorkTaskTimer.class, new SimpleWorkTaskTimer());
		Msg.info("Static init finished.");
	}

	protected static WorkTaskTimer getTimer() {
		return workTimer;
	}

	/**
	 * Default constructor for Processes needs to be used as part of framework.
	 * 
	 * @param host
	 * @param name
	 * @param args
	 */
	public TaskRunnerProcess(Host host, String name, String[] args) {
		super(host, name, args);

	}

	@Override
	public void main(String[] args) {
		if (args.length > 0)
			numMapSlots = SafeParsing.safeIntParse(args[0], 2, "args[0] (int numMap) wrong format for TaskTracker at "
					+ this.getHost());
		else {
			// use default map slots from config.xml
			numMapSlots = Integer.parseInt(SimConfig.getElementText("TaskTrackerDefaultMapSlots", "3"));

		}

		if (args.length > 1)
			numReduceSlots = SafeParsing.safeIntParse(args[1], 2, "args[1] (int numReduce) wrong format for TaskTracker at "
					+ this.getHost());
		else {
			// use default reduce slots from config.xml
			numReduceSlots = Integer.parseInt(SimConfig.getElementText("TaskTrackerDefaultReduceSlots", "3"));

		}

		timeUntilNextHeartbeat = Math.random() * 10;// A bit of randomness with startup
		completed = new HashMap<WorkTask, List<FileBlock>>();
		while (!finished) {
			try {
				if (timeUntilNextHeartbeat <= 0) {
					Msg.info("Sending heartbeat to " + SchedulerProcess.SCHEDULER_MAILBOX);
					(new HeartbeatTask(this, completed)).send(SchedulerProcess.SCHEDULER_MAILBOX);

					completed = new HashMap<WorkTask, List<FileBlock>>();// reset completed list
					timeUntilNextHeartbeat = SchedulerProcess.getHeartbeatInterval();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			try {
				Task task = checkTask(SchedulerProcess.getHeartbeatInterval());
				if (task != null)
					handleTask(task);
			} catch (MsgException e) { // e.printStackTrace();
				timeUntilNextHeartbeat -= SchedulerProcess.getHeartbeatInterval();
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
		return new SimpleReduceProcess(host, name, "Reducer for " + workTask.getID(), this, workTask,
				new SimpleShuffleSorter());
	}

	protected void notifyMapFinish(WorkTask task, List<FileBlock> output) {
		completed.put(task, output);
		numMapRunning--;
	}

	protected void notifyReduceFinish(WorkTask task, List<FileBlock> output) {
		completed.put(task, output);
		numReduceRunning--;
	}

	protected void handleTask(Task received) {
		Msg.info(this.getHost().getName() + " handling " + received + " of class " + received.getClass().getSimpleName());
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
					workTask.setComputeDuration(workTimer.estimateComputeDuration(this.getHost(), workTask));
					process.start();
				} catch (HostNotFoundException e) {
					e.printStackTrace();
				}
			}
		} else if (received instanceof NotifyNoMoreTasks) {
			// we have received a notification that there are no more tasks to
			// run
			if (numMapRunning == 0 && numReduceRunning == 0)
				this.finish();
		}
	}

	/**
	 * Boiler plate getters
	 * 
	 */
	public int getNumMapSlots() {
		return this.numMapSlots;
	}

	public int getNumReduceSlots() {
		return this.numReduceSlots;
	}

	public int getNumMapRunning() {
		return this.numMapRunning;
	}

	public int getNumReduceRunning() {
		return this.numReduceRunning;
	}

}
