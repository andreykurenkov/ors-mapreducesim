package mapreducesim.execution;

import mapreducesim.scheduling.FileSplitter;
import mapreducesim.scheduling.FileSplitter.InputSplit;

/**
 * Simple class to convert an InputSplit into a series of Key/Values to work on. Modeled on Hadoop's RecordReader. Unlike
 * there, here no actual Keys/values are returned but instead an abstract representation of them. At the simplest level, a
 * double will be used to convey of the key/double.
 * 
 * @author Andrey Kurenkov
 * 
 */
// TODO: javadoc
public abstract class KeyValueReader<KeyType, ValueType> {
	protected InputSplit mySplit;

	/**
	 * Does nothing more than instantiate the reader's input split.
	 * 
	 * @param split
	 */
	public KeyValueReader(InputSplit split) {
		this.mySplit = split;
	}

	/**
	 * 
	 * @return
	 */
	public abstract KeyType getCurrentKey();

	/**
	 * 
	 * @return
	 */
	public abstract ValueType getCurrentValue();

	/**
	 * 
	 * @return
	 */
	public abstract float getProgress();

	/**
	 * 
	 * @return
	 */
	public abstract double readNextKeyValue();

}