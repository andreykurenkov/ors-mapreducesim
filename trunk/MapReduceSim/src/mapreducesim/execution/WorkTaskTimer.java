package mapreducesim.execution;

import mapreducesim.execution.tasks.WorkTask;
import mapreducesim.util.xml.XMLElement;

import org.simgrid.msg.Host;

public abstract class WorkTaskTimer {
	protected XMLElement input;
	public WorkTaskTimer(XMLElement input){
		this.input=input;
	}
	
	public abstract double estimateComputeDuration(Host onHost, WorkTask task);
}
