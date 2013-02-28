package mapreducesim.execution.tasks;

import mapreducesim.scheduling.FileSplitter.InputSplit;
import mapreducesim.storage.DataLocation;
import mapreducesim.storage.File;
import mapreducesim.storage.FileBlockLocation;

import org.simgrid.msg.Task;

/**
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
	public File output;
	public boolean finished;

	private static int mapCount;
	private static int reduceCount;
	private String id;

	public WorkTask(double workAmount, Type type, InputSplit data) {
		this.WORK_AMOUNT = workAmount;
		this.NEEDED_DATA = data;
		this.TYPE = type;
		if (type == Type.MAP) {
			id = "Map Task " + (++mapCount);
		} else {
			id = "Reduce Task " + (++reduceCount);
		}
	}

	public String getID() {
		return id;
	}

	public Type getType() {
		return TYPE;
	}

	public String toString() {
		return id;
	}
}
