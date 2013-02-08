package mapreducesim.tasks;

import java.util.List;

import mapreducesim.core.SimFile.SimFileLocation;
import mapreducesim.execution.SimpleMapperProcess;
import mapreducesim.execution.TaskTrackerProcess;

import org.simgrid.msg.Host;
import org.simgrid.msg.Task;
import org.simgrid.msg.Process;

public abstract class WorkTask extends Task {
	public static enum Type {
		MAP, REDUCE, COMBINE
	}

	public final List<SimFileLocation> NEEDED_FILES;
	public final long WORK_AMOUNT;
	public final Type TYPE;

	public WorkTask(List<SimFileLocation> files, long workAmount, Type type) {
		this.WORK_AMOUNT = workAmount;
		this.NEEDED_FILES = files;
		this.TYPE = type;
	}

	public Type getType() {
		return TYPE;
	}

	public abstract Process getExecutionProcess(Host host, String name, TaskTrackerProcess parent);

}
