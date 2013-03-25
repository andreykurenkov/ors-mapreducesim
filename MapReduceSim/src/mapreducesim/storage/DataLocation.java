package mapreducesim.storage;

import java.util.List;

/**
 * Simple storer of information needed to specify the data Map and Reduce tasks
 * need.
 * 
 * Simulates /hadoop/fs/BlockLocation.java
 * 
 * @author Andrey Kurenkov
 * 
 */

public class DataLocation {
	private String fileName;
	private String host; // host where the data is stored
	private List<DataNode> datanodes; // should be the same as hosts but with
										// DataNode objects
	private int offset;
	private int length;

	/**
	 * Simple constructor that sets the instance variables with no hosts
	 * defined.
	 * 
	 * @param file
	 * @param offset
	 * @param length
	 */
	public DataLocation(String file, int offset, int length) {
		super();
		this.fileName = file;
		this.offset = offset;
		this.length = length;
		this.host = "[no host defined]";
	}

	/**
	 * Constructor that sets instance variables including the list of hosts
	 * 
	 * @param file
	 * @param offset
	 * @param length
	 * @param hosts
	 */
	public DataLocation(String file, int offset, int length, String host,
			List<DataNode> datanodes) {
		super();
		this.fileName = file;
		this.offset = offset;
		this.length = length;
		this.host = host;
		this.setDatanodes(datanodes);
	}

	public String toString() {
		return "(file=" + fileName + ", offset=" + offset + ", length="
				+ length + ")";
	}

	/**
	 * Getter for the filename
	 * 
	 * @return the filename
	 */
	public String getFilename() {
		return fileName;
	}

	/**
	 * @param file
	 *            the new value of type File to make file
	 */
	public void setFile(String file) {
		this.fileName = file;
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

	/**
	 * Returns an array of String hostnames
	 * 
	 * @return String[]
	 */
	public String getHost() {
		if ((host == null) || (host.length() == 0)) {
			return new String();
		} else {
			return host;
		}
	}

	/**
	 * Set the hosts.
	 * 
	 * @param hosts
	 */
	public void setHosts(String host) {
		if (host == null) {
			this.host = new String();
		} else {
			this.host = host;
		}
	}

	public List<DataNode> getDatanodes() {
		return datanodes;
	}

	public void setDatanodes(List<DataNode> datanodes) {
		this.datanodes = datanodes;
	}

}
