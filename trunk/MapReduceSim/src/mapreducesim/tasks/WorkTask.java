package mapreducesim.tasks;

import java.util.List;

import mapreducesim.execution.SimpleMapperProcess;
import mapreducesim.execution.TaskTrackerProcess;
import mapreducesim.storage.File.FileLocation;

import org.simgrid.msg.Host;
import org.simgrid.msg.Task;
import org.simgrid.msg.Process;

public class WorkTask extends Task {
	public static enum Type {
		MAP, REDUCE
	}

	public final FileLocation[] NEEDED_FILES;
	public final long WORK_AMOUNT;
	public final Type TYPE;
	public FileLocation outout;
	public boolean finished;

	private static int mapCount;
	private static int reduceCount;
	private String id;

	public WorkTask(long workAmount, Type type, FileLocation... files) {
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
