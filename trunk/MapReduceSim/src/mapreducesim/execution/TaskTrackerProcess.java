package mapreducesim.execution;

import java.lang.reflect.InvocationTargetException;

import mapreducesim.core.MapReduceSimMain;
import mapreducesim.core.SimProcess;
import mapreducesim.execution.tasks.HeartbeatTask;
import mapreducesim.execution.tasks.WorkTask;
import mapreducesim.interfaces.JobTrackerInterface;
import mapreducesim.util.ReflectionUtil;
import mapreducesim.util.SafeParsing;
import mapreducesim.util.xml.XMLDocument;
import mapreducesim.util.xml.XMLElement;

import org.simgrid.msg.Host;
import org.simgrid.msg.HostNotFoundException;
import org.simgrid.msg.Msg;
import org.simgrid.msg.MsgException;
import org.simgrid.msg.Task;

@SuppressWarnings("unchecked")
public class TaskTrackerProcess extends SimProcess {
	private int numMapSlots, numMapRunning;
	private int numReduceSlots, numReduceRunning;

	private int mapCount, reduceCount;

	private int timeUntilNextHeartbeat;

	private static String CONFIG_TIMER = "TaskTimer";
	private static WorkTaskTimer workTimer;
	static {
		String name = null;
		XMLDocument config;
		XMLElement timer = null;
		Msg.info("test static init");
		try {
		config = MapReduceSimMain.getConfig();
		if (config!=null){
		timer = config.getRoot().getChildByName(CONFIG_TIMER);
		name = timer.getAttributeValue("name");
		} else{
			Msg.info("no name or timer info found. using defaults");
			name ="defaultName";
			timer = new XMLElement(CONFIG_TIMER);
		}
		} catch (Exception e){
			Msg.info(e.getMessage());
		}
		try {
			workTimer = ReflectionUtil.attemptConstructorCallAndCast(WorkTaskTimer.class, Class.forName(name), timer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Msg.info("done static init");
	}

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
				Task task = checkTask(MapReduceSimMain.SIM_STEP);
				if (task != null)
					handleTask(task);
			} catch (MsgException e) { // e.printStackTrace();
				timeUntilNextHeartbeat -= MapReduceSimMain.SIM_STEP;
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
		Msg.info(this.getHost().getName() + " handling " + received+" of class "+received.getClass().getSimpleName());
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
