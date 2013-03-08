package mapreducesim.execution;

import java.util.List;

import mapreducesim.scheduling.FileSplitter.InputSplit;
import mapreducesim.storage.KeyValuePair;

/**
 * @author Andrey Kurenkov
 * @version 1.0 Mar 7, 2013
 */
public abstract class OutputCollector {

	public abstract double collectOutput(KeyValuePair out);

	public abstract List<InputSplit> writeOutput();
}
