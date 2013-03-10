package mapreducesim.scheduling;

import mapreducesim.core.ConfigurableClass;
import mapreducesim.util.xml.XMLElement;

/**
 * Class that will produce Jobs to run on the simulator
 * 
 * @author Andrey Kurenkov
 * @version 1.0 Mar 7, 2013
 */
public abstract class JobMaker extends ConfigurableClass {
	static {
		ConfigurableClass.addDefaultInstance(JobMaker.class, new SimpleJobMaker(null));
	}

	/**
	 * The XML element from which to make the object (see ConfigurableClass)
	 * 
	 * @param input
	 */
	public JobMaker(XMLElement input) {
		super(input);
	}

	public abstract MapReduceJobSpecification getJob(String jobName);

}
