package mapreducesim.scheduling;

import java.util.List;

import mapreducesim.util.xml.XMLElement;

/**
 * The default file splitter implementation
 * 
 * @author tdoneal
 * 
 */
public class SimpleFileSplitter extends FileSplitter {

	public SimpleFileSplitter() {
		// no additional config
		super(null);
	}

	@Override
	public List<InputSplit> getInputSlits(MapReduceJobSpecification job) {
		// TODO Auto-generated method stub
		return null;
	}

}
