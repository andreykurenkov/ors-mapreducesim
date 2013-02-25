package mapreducesim.execution;

import mapreducesim.execution.tasks.WorkTask;
import mapreducesim.util.xml.XMLElement;

import org.simgrid.msg.Host;

public abstract class WorkTaskTimer<T extends WorkTask> {
	protected XMLElement input;
	public WorkTaskTimer(XMLElement input){
		this.input=input;
	}
	
	public abstract double estimateComputeDuration(Host onHost, T task);

	// or maybe public double getOperationDuration(Host onHost, T task, int keySize, int valueSize)?-
}
