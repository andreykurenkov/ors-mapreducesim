package mapreducesim.storage;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a file in the file system. Each file is divided into blocks
 * (FileBlock) of a split size defined for each job. Files are represented and
 * do not have a physical location--these are FileBlocks, which have
 * FileBlockLocations.
 * 
 * @author Matthew O'Shaughnessy
 */

public class File extends Node {

	private int size;
	private int numReads;
	private List<FileBlock> blocks;
	private static final int SPLIT_SIZE = 128;

	/**
	 * Default constructor
	 * 
	 * @param parent
	 * @param name
	 */
	public File(Node parent, String name, int size) {
		super(parent, name);
		this.numReads = 0;
		this.size = size;
		this.blocks = new ArrayList<FileBlock>();
		this.isFile = true;
		makeSplits();
	}

	/**
	 * Don't use this constructor (for testing only)--files must have a parent
	 * directory.
	 */
	public File(String name) {
		super();
		this.setName(name);
	}

	public List<FileBlock> getBlocks() {
		return blocks;
	}

	public static int getSplitSize() {
		return SPLIT_SIZE;
	}

	/**
	 * Increments the counter for the number of times this file has been read.
	 */
	public void incrementReads() {
		numReads++;
	}

	public int getNumReads() {
		return numReads;
	}

	/**
	 * Used to validate if a retrieved fs object with the given filename is
	 * actually a file
	 */
	public boolean isFile() {
		return isFile;
	}

	/**
	 * Input: a newly-created file with a size and a set split_size
	 * 
	 * Output: blocks field populated with FileBlock s.
	 * 
	 * Example: new file created with size 300MB, SPLIT_SIZE=128 --
	 * this.blocks[0] (size=128) -- this.blocks[1] (size=128) -- this.blocks[2]
	 * (size=44)
	 */
	public void makeSplits() {
		int splitNumber = 0;
		int currentSize = 0;
		int remainingSize = this.size;
		FileBlock currentSplit;

		while (remainingSize > 0) {
			if (remainingSize > File.getSplitSize())
				currentSize = File.getSplitSize();
			else
				currentSize = remainingSize;
			currentSplit = new FileBlock(this, splitNumber, currentSize);
			this.blocks.add(currentSplit);
			// TODO: Assign blocks to DataNodes
			remainingSize -= currentSize;
			splitNumber++;
		}
	}

	/**
	 * With an input of offset and length, returns a list of the necessary
	 * fileblocks.
	 * 
	 * Example: With split size 128, offset=100,length=100, returns blocks[0]
	 * and blocks[1]
	 * 
	 * @param offset
	 * @param length
	 * @return
	 */
	public List<FileBlock> getNeededFileBlocks(double offset, int length) {
		int firstBlockIndex = (int) Math.floor(offset / SPLIT_SIZE);
		int lengthRemaining = ((int) offset + length) - SPLIT_SIZE;
		int lastBlockIndex = firstBlockIndex;
		while (lengthRemaining > 0) {
			lengthRemaining -= SPLIT_SIZE;
			lastBlockIndex++;
		}
		int arraySize = lastBlockIndex - firstBlockIndex + 1;
		List<FileBlock> toReturn = new ArrayList<FileBlock>(arraySize);
		for (int i = firstBlockIndex; i <= lastBlockIndex; i++) {
			toReturn.add(blocks.get(i));
		}
		return toReturn;
	}
}
