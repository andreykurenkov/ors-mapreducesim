package mapreducesim.execution;

import mapreducesim.core.ConfigurableClass;
import mapreducesim.execution.tasks.WorkTask;
import mapreducesim.storage.KeyValuePairs;
import mapreducesim.util.xml.XMLElement;

import org.simgrid.msg.Host;

/**
 * A configurable class that estimates the time a given WorkTask will take.
 * 
 * @author Andrey Kurenkov
 * @version 1.0 Mar 1, 2013
 */
public abstract class WorkTaskTimer extends ConfigurableClass {
	protected XMLElement input;

	/**
	 * Constructor for WorkTaskTimer that gives values to the instance variables corresponding to the parameters.
	 * 
	 * @param input
	 */
	public WorkTaskTimer(XMLElement input) {
		super(input);
	}

	/**
	 * Estimates time cost of performing entire WorkTask
	 * 
	 * @param onHost
	 *            the host to estimate for
	 * @param task
	 *            the task
	 * @return time in seconds to perform the task
	 */
	public abstract double estimateComputeDuration(Host onHost, WorkTask task);

	/**
	 * Estimates time cost of performing a map or reduce task for the given collection of pairs
	 * 
	 * @param onHost
	 *            the host to estimate for
	 * @param task
	 *            the task pairs are part of
	 * @param pairs
	 *            information about the number of pairs and their size
	 * @return time in seconds to perform the computation for the pairs
	 */
	public abstract double estimateComputeDuration(Host onHost, WorkTask task, KeyValuePairs pairs);
}
