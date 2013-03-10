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
	private FileBlockLocation location;
	private List<KeyValuePair> pairs;

	/**
	 * Default constructor.
	 * 
	 * @param owner
	 *            the represented file the FileBlock is a part of
	 * @param index
	 *            the index of the FileBlock. For example, if a File is 400MB
	 *            and the split size is 128MB, the FileBlock containing 256-384
	 *            would be at index 2
	 * @param pairs
	 *            The KeyValue that are part of this FileBlock
	 */
	public FileBlock(File owner, int index, FileBlockLocation location,
			List<KeyValuePair> pairs) {
		this.owner = owner;
		this.index = index;
		this.location = location;
		this.pairs = pairs;
		for (KeyValuePair pair : pairs)
			size += pair.getSize();
	}

	/**
	 * Default constructor.
	 * 
	 * @param owner
	 *            the represented file the FileBlock is a part of
	 * @param index
	 *            the index of the FileBlock. For example, if a File is 400MB
	 *            and the split size is 128MB, the FileBlock containing 256-384
	 *            would be at index 2
	 * @param size
	 *            Will be the same SPLIT_SIZE unless it is the last split. E.g.,
	 *            in a 400MB File with SPLIT_SIZE=128, the final FileBlock size
	 *            will be 16.
	 */
	public FileBlock(File owner, int index, int size, FileBlockLocation location) {
		this.setOwner(owner);
		this.setIndex(index);
		this.setSize(size);
		this.setLocation(location);
	}

	/**
	 * The pair to add to this FileBlock
	 * 
	 * @param pair
	 *            the pair to add
	 */
	public void addKeyValue(KeyValuePair pair) {
		this.pairs.add(pair);
	}

	/**
	 * Getter for the pairs
	 * 
	 * @return the pairs
	 */
	public List<KeyValuePair> getPairs() {
		return pairs;
	}

	/**
	 * @param pairs
	 *            value of type ArrayList<KeyValuePair> that pairs will be set
	 *            to
	 */
	public void setPairs(ArrayList<KeyValuePair> pairs) {
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

	public FileBlockLocation getLocation() {
		return location;
	}

	public void setLocation(FileBlockLocation location) {
		this.location = location;
	}
}
