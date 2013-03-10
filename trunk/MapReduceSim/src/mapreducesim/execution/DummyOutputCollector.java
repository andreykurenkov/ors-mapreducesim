package mapreducesim.execution;

import java.util.List;

import mapreducesim.scheduling.FileSplitter.InputSplit;
import mapreducesim.storage.KeyValuePairs;

/**
 * @author Andrey Kurenkov
 * @version 1.0 Mar 7, 2013
 */
public class DummyOutputCollector extends OutputCollector {

	/*
	 * (non-Javadoc)
	 * 
	 * @see mapreducesim.execution.OutputCollector#collectOutput(mapreducesim.storage.KeyValuePair)
	 */
	@Override
	public double collectOutput(KeyValuePairs out) {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mapreducesim.execution.OutputCollector#writeOutput()
	 */
	@Override
	public List<InputSplit> writeOutput() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mapreducesim.execution.OutputCollector#getOutput()
	 */
	@Override
	public List<KeyValuePairs> getOutput() {
		return null;
	}

}
