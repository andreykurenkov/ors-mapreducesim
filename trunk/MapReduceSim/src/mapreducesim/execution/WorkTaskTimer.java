package mapreducesim.execution;

import mapreducesim.core.ConfigurableClass;
import mapreducesim.execution.tasks.WorkTask;
import mapreducesim.util.xml.XMLElement;

import org.simgrid.msg.Host;

/**
 * A configurable class that estimates the time a given WorkTask will take.
 * 
 * @author Andrey Kurenkov
 * @version 1.0 Mar 1, 2013
 * @param <T>
 */
public abstract class WorkTaskTimer extends ConfigurableClass {
	protected XMLElement input;

	public WorkTaskTimer(XMLElement input) {
		super(input);
	}

	public abstract double estimateComputeDuration(Host onHost, WorkTask task);

	// or maybe public double getOperationDuration(Host onHost, T task, int keySize, int valueSize)?-
}
