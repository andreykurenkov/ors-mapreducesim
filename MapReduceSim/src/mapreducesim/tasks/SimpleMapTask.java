package mapreducesim.tasks;

import java.util.List;

import org.simgrid.msg.Host;
import org.simgrid.msg.Process;

import mapreducesim.core.SimFile.SimFileLocation;
import mapreducesim.execution.SimpleMapperProcess;
import mapreducesim.execution.TaskTrackerProcess;

public class SimpleMapTask extends WorkTask {

	public SimpleMapTask(List<SimFileLocation> files, long workAmount) {
		super(files, workAmount, Type.MAP);
	}

	public Process getExecutionProcess(Host host, String name, TaskTrackerProcess parent) {
		return new SimpleMapperProcess(host, name, parent, this);
	}

}
