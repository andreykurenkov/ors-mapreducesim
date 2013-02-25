package mapreducesim.execution.tasks;

import mapreducesim.storage.File;
import mapreducesim.storage.FileBlockLocation;

import org.simgrid.msg.Task;

public class WorkTask extends Task {
	public static enum Type {
		MAP, REDUCE
	}

	// TODO task attemptID
	public final FileBlockLocation[] NEEDED_FILES;
	
	public final double WORK_AMOUNT;
	public final Type TYPE;
	public File output;
	public boolean finished;

	private static int mapCount;
	private static int reduceCount;
	private String id;

	public WorkTask(double workAmount, Type type, FileBlockLocation... files) {
		this.WORK_AMOUNT = workAmount;
		this.NEEDED_FILES = files;
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
