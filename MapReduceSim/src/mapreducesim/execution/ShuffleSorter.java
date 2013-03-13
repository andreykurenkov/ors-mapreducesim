package mapreducesim.execution;

import java.util.List;
import java.util.Map;

import org.simgrid.msg.HostFailureException;
import org.simgrid.msg.TimeoutException;
import org.simgrid.msg.TransferFailureException;

import mapreducesim.core.ConfigurableClass;
import mapreducesim.scheduling.FileSplitter.InputSplit;
import mapreducesim.storage.KeyValuePairs;
import mapreducesim.util.xml.XMLElement;

/**
 * Simulation class that handle the shuffle/sort phase for reduce operations. Implemented to be configurable so as to allow
 * both simple and more detailed implementations.
 * 
 * @author Andrey Kurenkov
 * @version 1.0 Mar 7, 2013
 */
public abstract class ShuffleSorter extends ConfigurableClass {
	static {
		ConfigurableClass.addDefaultInstance(ShuffleSorter.class, new SimpleShuffleSorter());
	}

	/**
	 * Constructor to be reflexively called when this is instatntiated from XML configuration. Does nothing more than store
	 * the XML.
	 * 
	 * @param input
	 *            the XMLElement holding info about this sorter
	 */
	public ShuffleSorter(XMLElement input) {
		super(input);
	}

	/**
	 * Handles simulating the shuffle/sort phase for reduce tasks. Called at the beggining of reduce operations to simulate
	 * retriving and sorting the needed files.
	 * 
	 * @param split
	 *            the split on which the reduce operation is working.
	 * @param process
	 *            the process within which the reduce operation is happening
	 * @return The list of final keyvaluepairs for the reducer
	 * @throws TransferFailureException
	 *             typical Msg exception when handling Tasks
	 * @throws HostFailureException
	 *             typical Msg exception when handling Tasks
	 * @throws TimeoutException
	 *             typical Msg exception when handling Tasks
	 */
	public abstract List<KeyValuePairs> doShuffleSort(InputSplit split, WorkerProcess process)
			throws TransferFailureException, HostFailureException, TimeoutException;

}
