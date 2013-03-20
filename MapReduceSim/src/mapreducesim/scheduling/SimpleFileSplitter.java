package mapreducesim.scheduling;

import java.util.ArrayList;
import java.util.List;

import mapreducesim.core.SimConfig;
import mapreducesim.storage.DataLocation;
import mapreducesim.util.xml.XMLElement;
import mapreducesim.util.xml.XMLNode;

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
		ArrayList<InputSplit> toReturn = new ArrayList<InputSplit>();
		XMLElement in = SimConfig.getConfigurationElement(job.getName() + " Job Input");
		if (in != null) {
			String mode = in.getAttributeValue("mode");
			if (mode != null && mode.equals("random")) {
				// TODO
			} else {
				for (XMLElement split : in.getElements()) {
					ArrayList<DataLocation> locations = new ArrayList<DataLocation>();
					for (XMLElement dataLoc : split.getElements()) {
						String name = dataLoc.getAttributeValue("filename");
						int offset = SimConfig.parseIntAttribute(dataLoc, "offset", -1);
						int length = SimConfig.parseIntAttribute(dataLoc, "length", -1);
						locations.add(new DataLocation(name, offset, length));// TODO: handle -1
					}
					toReturn.add(new InputSplit(locations));
				}
			}
		}
		return toReturn;
	}
}
