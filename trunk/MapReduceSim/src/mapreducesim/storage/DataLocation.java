package mapreducesim.storage;

/**
 * Simple storer of information needed to specify the data Map and Reduce tasks need.
 * 
 * @author Andrey Kurenkov
 * 
 */
public class DataLocation {
	private File file;
	private int offset;
	private int length;

	/**
	 * Simple constructor that sets the instance variables.
	 * 
	 * @param file
	 * @param offset
	 * @param length
	 */
	public DataLocation(File file, int offset, int length) {
		super();
		this.file = file;
		this.offset = offset;
		this.length = length;
	}

	/**
	 * Getter for the file
	 * 
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @param file
	 *            the new value of type File to make file
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * Getter for the offset
	 * 
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @param offset
	 *            the new value of type int to make offset
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * Getter for the length
	 * 
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @param length
	 *            the new value of type int to make length
	 */
	public void setLength(int length) {
		this.length = length;
	}
}
