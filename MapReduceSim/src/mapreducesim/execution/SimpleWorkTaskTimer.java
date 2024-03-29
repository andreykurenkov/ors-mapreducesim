package mapreducesim.execution;

import mapreducesim.core.SimConfig;
import mapreducesim.execution.tasks.WorkTask;
import mapreducesim.storage.KeyValuePairs;
import mapreducesim.util.xml.XMLElement;

import org.simgrid.msg.Host;
import org.simgrid.msg.Msg;

/**
 * Default implementation of WorkTaskTimer that simply divides the totalsize of data by the speed of the host and multiplies
 * by some coefficient. The coefficient can be defined as an attibute of the XMLElement, with "Map Coefficient" for map
 * operations and "Reduce Coefficient" for reduce operations. Coefficients are floating point numbers.
 * 
 * @author Andrey
 */
public class SimpleWorkTaskTimer extends WorkTaskTimer {
	private double mapWork, reduceWork, constant;

	/**
	 * Creates a simple timer without XML input that sets default values for coefficients.
	 */
	public SimpleWorkTaskTimer() {
		super(null);
		mapWork = reduceWork = 5;
		constant = -1;
	}

	/**
	 * Accepts the XML Element that specified it
	 * 
	 * @param input
	 */
	public SimpleWorkTaskTimer(XMLElement input) {
		super(input);
		constant = SimConfig.parseDoubleAttribute(input, "Constant", -1);
		mapWork = SimConfig.parseDoubleAttribute(input, "MapCoefficient", 5);
		reduceWork = SimConfig.parseDoubleAttribute(input, "ReduceCoefficient", 5);
		if (constant != -1)
			Msg.info("Using constant timer length " + constant);
	}

	@Override
	public double estimateComputeDuration(Host onHost, WorkTask task) {
		if (constant != -1)
			return constant;
		return task.WORK_AMOUNT / onHost.getSpeed();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mapreducesim.execution.WorkTaskTimer#estimateComputeDuration(org.simgrid .msg.Host,
	 * mapreducesim.storage.KeyValuePair)
	 */
	@Override
	public double estimateComputeDuration(Host onHost, WorkTask task, KeyValuePairs pairs) {
		if (constant != -1)
			return constant;
		if (task.TYPE == WorkTask.Type.MAP)
			return mapWork * pairs.getTotalSize() / onHost.getSpeed();
		else
			return reduceWork * pairs.getTotalSize() / onHost.getSpeed();

	}

}
