package mapreducesim.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a file in the file system. Each file is divided into blocks (FileBlock) of a split size defined for each job.
 * Files are represented and do not have a physical location--these are FileBlocks, which have FileBlockLocations.
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
	 * Don't use this constructor (for testing only)--files must have a parent directory.
	 */
	public File(String name) {
		super();
		this.setName(name);
	}

	public List<FileBlock> getBlocks() {
		return blocks;
	}

	public void addFileBlock(FileBlock toAdd) {
		toAdd.setOwner(this);
		toAdd.setIndex(blocks.size());
		blocks.add(toAdd);
		size += toAdd.getSize();
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
	 * Used to validate if a retrieved fs object with the given filename is actually a file
	 */
	public boolean isFile() {
		return isFile;
	}

	/**
	 * Input: a newly-created file with a size and a set split_size
	 * 
	 * Output: blocks field populated with FileBlock s.
	 * 
	 * Example: new file created with size 300MB, SPLIT_SIZE=128 -- this.blocks[0] (size=128) -- this.blocks[1] (size=128) --
	 * this.blocks[2] (size=44)
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
			// placeSplits();
			remainingSize -= currentSize;
			splitNumber++;
		}
	}

	/**
	 * Simple policy to place the blocks of a file onto DataNodes in the topology
	 * 
	 * @param top
	 *            the topology
	 */
	public void placeSplits(DataTree<Node> top) {
		Random random = new Random();

		// get the number of racks
		int numRacks = ((Root) top.getRoot()).getNumRacks();

		for (FileBlock block : this.blocks) {
			// choose one rack to be the host for two of the replicas
			int rack1Index = random.nextInt(numRacks);
			// choose another rack to be the host for the third
			int rack2Index = random.nextInt(numRacks);
			while (rack1Index == rack2Index) { // make sure they aren't the same
				rack2Index = random.nextInt(numRacks);
			}
			// get the DataNodes to put a replica of this block on
			Rack rack1 = (Rack) top.getRoot().getChildAtPosition(rack1Index);
			Rack rack2 = (Rack) top.getRoot().getChildAtPosition(rack2Index);
			DataNode node1 = (DataNode) rack1.getChildAtPosition(0);
			DataNode node2 = (DataNode) rack1.getChildAtPosition(1);
			DataNode node3 = (DataNode) rack2.getChildAtPosition(0);
			// place the replicas
			block.addLocations(node1, node2, node3);
		}

	}

	/**
	 * With an input of offset and length, returns a list of the necessary fileblocks.
	 * 
	 * Example: With split size 128, offset=100,length=100, returns blocks[0] and blocks[1]
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
