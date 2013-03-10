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
	public SimpleFileSplitter(XMLElement element) {
		super(element);
	}

	public SimpleFileSplitter() {
		this(null);
	}

	@Override
	public List<InputSplit> getInputSlits(MapReduceJobSpecification job) {
		// TODO Auto-generated method stub
		return null;
	}

}
