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
		this.size = size;
		this.blocks = new ArrayList<FileBlock>();
		makeSplits();
	}

	/**
	 * Don't use this constructor--files must have a parent directory.
	 */
	public File(String name) {
		super();
		this.setName(name);
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
			if (remainingSize > File.SPLIT_SIZE)
				currentSize = File.SPLIT_SIZE;
			else
				currentSize = remainingSize;
			currentSplit = new FileBlock(this, splitNumber, currentSize);
			this.blocks.add(currentSplit);
			// TODO: Assign blocks to DataNodes
			remainingSize -= currentSize;
			splitNumber++;
		}
	}
}
