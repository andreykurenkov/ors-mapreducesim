package mapreducesim.storage;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Represents a split of a represented file.
 */

public class FileBlock {
	private File owner;
	private int index;
	private int size;
	private int numReads;
	private int offset;
	private int length;
	private List<DataNode> locations;
	private KeyValuePairs pairs;

	/**
	 * Default constructor.
	 * 
	 * @param owner
	 *            the represented file the FileBlock is a part of
	 * @param index
	 *            the index of the FileBlock. For example, if a File is 400MB
	 *            and the split size is 128MB, the FileBlock containing 256-384
	 *            would be at index 2
	 * @param locations
	 *            DataNode locations associated with this file block
	 * @param pairs
	 *            The KeyValuePairs that are part of this FileBlock
	 */
	public FileBlock(File owner, int index, List<DataNode> locations,
			KeyValuePairs pairs) {
		this.owner = owner;
		this.index = index;
		this.locations = locations;
		this.pairs = pairs;
		this.numReads = 0;
		if (pairs != null)
			size = pairs.getNumPairs() * pairs.getSizeOfPair();
	}

	/**
	 * Constructor that sets DataNodes to null
	 * 
	 * @param owner
	 *            the represented file the FileBlock is a part of
	 * @param index
	 *            the index of the FileBlock. For example, if a File is 400MB
	 *            and the split size is 128MB, the FileBlock containing 256-384
	 *            would be at index 2
	 * @param pairs
	 *            The KeyValuePairs that are part of this FileBlock
	 */
	public FileBlock(File owner, int index, KeyValuePairs pairs) {
		this(owner, index, null, pairs);
	}

	/**
	 * @param file
	 * @param splitNumber
	 * @param currentSize
	 */
	public FileBlock(File file, int splitNumber, int currentSize) {
		this(file, splitNumber, new ArrayList<DataNode>(), new KeyValuePairs(
				currentSize / 16, currentSize));
		this.size = currentSize;
	}

	/**
	 * Getter for the pairs
	 * 
	 * @return the pairs
	 */
	public KeyValuePairs getPairs() {
		return pairs;
	}

	/**
	 * @param pairs
	 *            value of type KeyValuePair that pairs will be set to
	 */
	public void setPairs(KeyValuePairs pairs) {
		this.pairs = pairs;
	}

	public File getOwner() {
		return owner;
	}

	public void setOwner(File owner) {
		this.owner = owner;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public List<DataNode> getLocations() {
		return locations;
	}

	public DataNode getLocation(int index) {
		return locations.get(index);
	}

	public void setLocations(List<DataNode> locations) {
		this.locations = locations;
	}

	public void addLocation(DataNode location) {
		this.locations.add(location);
		if (!location.getBlocks().contains(this))
			location.addBlock(this);
	}

	public void addLocations(DataNode l1, DataNode l2, DataNode l3) {
		addLocation(l1);
		addLocation(l2);
		addLocation(l3);
	}

	public void incrementReads() {
		numReads++;
	}

	public int getNumReads() {
		return numReads;
	}

	public int getOffset() {
		return (getIndex() * File.getSplitSize());
	}

	public int numReplications() {
		if (this.locations == null) {
			return 0;
		} else
			return this.locations.size();
	}
}