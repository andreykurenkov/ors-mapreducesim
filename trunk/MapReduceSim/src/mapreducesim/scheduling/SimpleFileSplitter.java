package mapreducesim.scheduling;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import mapreducesim.core.SimConfig;
import mapreducesim.storage.DataLocation;
import mapreducesim.storage.DataNode;
import mapreducesim.storage.FileBlock;
import mapreducesim.storage.KeyValuePairs;
import mapreducesim.storage.StorageProcess;
import mapreducesim.util.xml.XMLElement;

import org.simgrid.msg.Msg;

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
			if (input.getAttributes().containsKey("type")) {
				if (input.getAttributeValue("type").equals("random")) {

					// testing setup using a b.s. file

					// get the topology and print out
					Msg.info("topology = " + StorageProcess.getTopology());
					List<DataNode> data = StorageProcess.getTopology().getDataNodes();
					// get the number of tasks to split the input into
					int N = job.getOriginalMapTasks().size();
					Msg.info("SimpleFileSplitter: N = " + N);
					for (int i = 0; i < N; i++) {
						// now for each map task, create an input split
						// to create an input split we need a list of
						// DataLocation
						List<DataLocation> dls = new LinkedList<DataLocation>();
						DataNode randomData = data.get((int) (Math.random() * data.size()));
						FileBlock block = new FileBlock(null, i, new KeyValuePairs(64, 1));// fake random FileBlock
						if (randomData.getBlocks().size() > 0) {// If blocks are actually being created
						int blockNum = (int) (Math.random() * randomData.getBlocks().size());
							block = randomData.getBlockAtPosition(blockNum);
						}
						// for now, just one data location per map task
						// in the future, handle multiple
						if (block.getOwner() != null)
							dls.add(new DataLocation(block.getOwner().getName(), block.getOffset(), block.getSize()));
						else
							dls.add(new DataLocation("Fake file", block.getOffset(), block.getSize()));
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
