package mapreducesim.scheduling;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.simgrid.msg.Msg;

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

		if (this.input != null) {
			Msg.info("SimpleFileSplitter: input wasn't null.");
			if (input.getAttributes().containsKey("type")) {
				if (input.getAttributeValue("type").equals("random")) {
					// get the number of tasks to split the input into
					int N = job.getOriginalMapTasks().size();
					Msg.info("SimpleFileSplitter: N = " + N);
					final int OFFS_SIZE = 10000;
					for (int i = 0; i < N; i++) {
						// now for each map task, create an input split
						// TODO: in the future, ask Matt's code for this
						// information
						// to create an input split we need a list of
						// DataLocation
						List<DataLocation> dls = new LinkedList<DataLocation>();
						// for now, just one data location per map task
						// in the future, handle multiple
						DataLocation dl = new DataLocation("inputFile", i
								* OFFS_SIZE, OFFS_SIZE);
						dls.add(dl);
						// now create the input split and add to the result
						InputSplit is = new InputSplit(dls);
						toReturn.add(is);

					}
					return toReturn;
				}
			}
		}

		XMLElement in = SimConfig.getConfigurationElement(job.getName()
				+ " Job Input");
		if (in != null) {
			String mode = in.getAttributeValue("mode");
			if (mode != null && mode.equals("random")) {
				// TODO
			} else {
				for (XMLElement split : in.getElements()) {
					ArrayList<DataLocation> locations = new ArrayList<DataLocation>();
					for (XMLElement dataLoc : split.getElements()) {
						String name = dataLoc.getAttributeValue("filename");
						int offset = SimConfig.parseIntAttribute(dataLoc,
								"offset", -1);
						int length = SimConfig.parseIntAttribute(dataLoc,
								"length", -1);
						locations.add(new DataLocation(name, offset, length));// TODO:
						// handle
						// -1
					}
					toReturn.add(new InputSplit(locations));
				}
			}
		}
		return toReturn;
	}
}
