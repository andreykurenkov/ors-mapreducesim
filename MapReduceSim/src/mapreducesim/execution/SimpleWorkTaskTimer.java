package mapreducesim.execution;

import mapreducesim.execution.tasks.WorkTask;
import mapreducesim.util.xml.XMLElement;

import org.simgrid.msg.Host;

public class SimpleWorkTaskTimer extends WorkTaskTimer<WorkTask> {

	public SimpleWorkTaskTimer(XMLElement input) {
		super(input);
	}

	@Override
	public double estimateComputeDuration(Host onHost, WorkTask task) {
		return task.WORK_AMOUNT / onHost.getSpeed();
	}

}