package mapreducesim.scheduling;

import mapreducesim.util.xml.XMLElement;

/**
 * Simple, default implementation of JobMaker that just reads jobs from the xml Right now, only handles one job at a time,
 * but this can be extended, time permitting.
 * 
 * @author tdoneal
 * 
 */
public class SimpleJobMaker extends JobMaker {

	public SimpleJobMaker(XMLElement input) {
		super(input);
	}

	@Override
	public MapReduceJobSpecification getJob(String jobName) {
		return MapReduceJobSpecification.constructFromXML(this.input, jobName);
	}

}
