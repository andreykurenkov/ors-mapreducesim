package mapreducesim.execution.test;

import mapreducesim.execution.test.FileSplitter.InputSplit;

public abstract class KeyValueReader {
	protected InputSplit mySplit;
	
	public KeyValueReader(InputSplit split) {
		this.mySplit = split;
	}

	public abstract double getCurrentKey();

	public abstract double getCurrentValue();

	public abstract float getProgress();

	public abstract double nextKeyValue();

}
