package mapreducesim.storage;

/**
 * 
 * Represents a split of a file 
 */

public class FileBlock {
	private File owner;
	private int index;
	private int size;
	private FileBlockLocation location;
	
	/**
	 * @param owner the represented file the FileBlock is a part of
	 * @param index the 
	 * @param size
	 */
	public FileBlock(File owner, int index, int size, FileBlockLocation location) {
		super();
		this.setOwner(owner);
		this.setIndex(index);
		this.setSize(size);
		this.setLocation(location);
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
