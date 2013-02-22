package mapreducesim.storage;

/**
 * 
 * Represents a split of a represented file.
 */

public class FileBlock {
	private File owner;
	private int index;
	private int size;
	private FileBlockLocation location;
	
	/**
	 * @param owner the represented file the FileBlock is a part of
	 * @param index the index of the FileBlock.  For example, if a File is
	 * 				400MB and the split size is 128MB, the FileBlock containing
	 * 				256-384 would be at index 2
	 * @param size  Will be the same SPLIT_SIZE unless it is the last split. E.g.,
	 * 				in a 400MB File with SPLIT_SIZE=128, the final FileBlock size
	 * 				will be 16.
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
