package mapreducesim.scheduling;

import java.util.List;

import mapreducesim.core.ConfigurableClass;
import mapreducesim.storage.DataLocation;
import mapreducesim.storage.FileBlockLocation;
import mapreducesim.util.xml.XMLElement;

/**
 * Responsible for generating the set of input locations that map operations will be performed on. Modeled on Hadoop's
 * InputFormat.
 * 
 * @author Andrey Kurenkov
 * 
 */
public abstract class FileSplitter extends ConfigurableClass {
	static {
		ConfigurableClass.addDefaultInstance(FileSplitter.class, new SimpleFileSplitter());
	}

	/**
	 * 
	 * @param input
	 */
	public FileSplitter(XMLElement input) {
		super(input);
	}

	/**
	 * Gets the splits of processing data for the given Job.
	 * 
	 * @param job
	 *            the job to get processing data for.
	 * @return List<InputSplits> to work on in this simulation
	 */
	public abstract List<InputSplit> getInputSlits(MapReduceJobSpecification job);

	/**
	 * Hadoop-esque class to store locations of data to be processed by a Mapper.
	 * 
	 * @author Andrey Kurenkov
	 * 
	 */
	public static class InputSplit {
		private DataLocation[] locations;
		private int size;

		/**
		 * Simple constructor that instantiates locations.
		 * 
		 * @param locations
		 */
		public InputSplit(DataLocation[] locations) {
			this.locations = locations;
			for (DataLocation loc : locations)
				size += loc.getLength();
		}

		/**
		 * Empty constructor that creates an inputsplit containing no data locations.
		 */
		public InputSplit() {
			locations = new DataLocation[0];
			size = 0;
		}

		/**
		 * Getter for the locations
		 * 
		 * @return the locations
		 */
		public DataLocation[] getLocations() {
			return locations;
		}

		/**
		 * Getter for the size
		 * 
		 * @return the size
		 */
		public int getSize() {
			return size;
		}

	}
}
