package mapreducesim.execution.tasks;

import mapreducesim.scheduling.InputSplit;
import mapreducesim.scheduling.MapReduceJobSpecification;
import mapreducesim.storage.DataLocation;
import mapreducesim.storage.File;
import mapreducesim.storage.FileBlockLocation;

import org.simgrid.msg.Task;

/**
 * A task holding information needed to do a Map or Reduce task to be sent from the Scheduler to any of TaskRunner processes.
 * 
 * 
 * @author Andrey Kurenkov
 * 
 */
public class WorkTask extends Task {
	public static enum Type {
		MAP, REDUCE
	}

	// TODO task attemptID
	public final InputSplit NEEDED_DATA;

	public final double WORK_AMOUNT;
	public final Type TYPE;
	public final MapReduceJobSpecification JOB;
	public boolean finished;

	private static int mapCount;
	private static int reduceCount;
	private String id;

	/**
	 * Constructor that sets up the needed information
	 * 
	 * 
	 * @param workAmount
	 * @param type
	 * @param data
	 */
	public WorkTask(double workAmount, MapReduceJobSpecification job, Type type, InputSplit data) {
		this.WORK_AMOUNT = workAmount;
		this.NEEDED_DATA = data;
		this.JOB = job;
		this.TYPE = type;
		if (type == Type.MAP) {
			id = "Map Task " + (++mapCount);
		} else {
			id = "Reduce Task " + (++reduceCount);
		}
	}

	/**
	 * Getter for id
	 * 
	 * @return ID
	 */
	public String getID() {
		return id;
	}

	/**
	 * Getter for type
	 * 
	 * @return TYPE
	 */
	public Type getType() {
		return TYPE;
	}

	@Override
	public String toString() {
		return id + (NEEDED_DATA == null ? "" : (", data=" + NEEDED_DATA));
	}
}
