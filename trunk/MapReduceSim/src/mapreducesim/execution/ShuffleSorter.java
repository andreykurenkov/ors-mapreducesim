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
 * @author Andrey Kurenkov
 * @version 1.0 Mar 7, 2013
 */
public abstract class ShuffleSorter extends ConfigurableClass {

	/**
	 * @param input
	 */
	public ShuffleSorter(XMLElement input) {
		super(input);
	}

	public abstract List<KeyValuePairs> doShuffleSort(InputSplit splitl, WorkerProcess process)
			throws TransferFailureException, HostFailureException, TimeoutException;

}
