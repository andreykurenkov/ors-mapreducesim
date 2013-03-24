package mapreducesim.scheduling;

import java.util.List;

import mapreducesim.core.ConfigurableClass;
import mapreducesim.storage.FileBlockLocation;
import mapreducesim.util.xml.XMLElement;

/**
 * Responsible for generating the set of input locations that map operations
 * will be performed on. Modeled on Hadoop's InputFormat.
 * 
 * @author Andrey Kurenkov
 * 
 */
public abstract class FileSplitter extends ConfigurableClass {
	static {
		ConfigurableClass.addDefaultInstance(FileSplitter.class,
				new SimpleFileSplitter());
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
	 * 
	 * 
	 * @return List<InputSplits> to work on in this simulation
	 */
	public abstract List<InputSplit> getInputSlits(MapReduceJobSpecification job);
}
