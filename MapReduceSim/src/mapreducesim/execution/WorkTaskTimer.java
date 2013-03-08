package mapreducesim.execution;

import mapreducesim.core.ConfigurableClass;
import mapreducesim.execution.tasks.WorkTask;
import mapreducesim.storage.KeyValuePair;
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

	public WorkTaskTimer(XMLElement input) {
		super(input);
	}

	public abstract double estimateComputeDuration(Host onHost, WorkTask task);

	public abstract double estimateComputeDuration(Host onHost, WorkTask task, KeyValuePair pair);
}
