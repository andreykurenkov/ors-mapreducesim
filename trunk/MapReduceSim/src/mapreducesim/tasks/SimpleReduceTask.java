package mapreducesim.tasks;

import java.util.List;

import mapreducesim.core.SimFile.SimFileLocation;
import mapreducesim.execution.SimpleMapperProcess;
import mapreducesim.execution.SimpleReduceProcess;
import mapreducesim.execution.TaskTrackerProcess;
import mapreducesim.tasks.WorkTask.Type;

import org.simgrid.msg.Host;
import org.simgrid.msg.Process;

public class SimpleReduceTask extends WorkTask {

	public SimpleReduceTask(List<SimFileLocation> files, long workAmount) {
		super(files, workAmount, Type.REDUCE);
	}

	public Process getExecutionProcess(Host host, String name, TaskTrackerProcess parent) {
		return new SimpleReduceProcess(host, name, parent, this);
	}

}
