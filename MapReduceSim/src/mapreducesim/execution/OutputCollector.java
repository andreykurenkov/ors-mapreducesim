package mapreducesim.execution;

import java.util.List;

import mapreducesim.scheduling.FileSplitter.InputSplit;
import mapreducesim.storage.KeyValuePairs;

/**
 * @author Andrey Kurenkov
 * @version 1.0 Mar 7, 2013
 */
public abstract class OutputCollector {

	public abstract double collectOutput(KeyValuePairs out);

	public abstract List<KeyValuePairs> getOutput();

	public abstract List<InputSplit> writeOutput();
}
