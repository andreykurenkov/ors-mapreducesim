package mapreducesim.execution;

import mapreducesim.execution.tasks.WorkTask;
import mapreducesim.storage.KeyValuePair;
import mapreducesim.util.xml.XMLElement;

import org.simgrid.msg.Host;

public class SimpleWorkTaskTimer extends WorkTaskTimer {
	private double mapWork, reduceWork;

	public SimpleWorkTaskTimer() {
		super(null);
	}

	public SimpleWorkTaskTimer(XMLElement input) {
		super(input);
		mapWork = reduceWork = 5;// TODO: change
	}

	@Override
	public double estimateComputeDuration(Host onHost, WorkTask task) {
		return task.WORK_AMOUNT / onHost.getSpeed();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mapreducesim.execution.WorkTaskTimer#estimateComputeDuration(org.simgrid.msg.Host,
	 * mapreducesim.storage.KeyValuePair)
	 */
	@Override
	public double estimateComputeDuration(Host onHost, WorkTask task, KeyValuePair pair) {
		if (task.TYPE == MAP)
			return mapWork * pair.getSize() / onHost.getSpeed();
		else
			return reduceWork * pair.getSize() / onHost.getSpeed();

	}

}
