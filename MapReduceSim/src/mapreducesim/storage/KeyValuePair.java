package mapreducesim.storage;

/**
 * Simple representation of a pair. Needed to keep track of keys (does not store actual value)
 * 
 * @author Andrey Kurenkov
 * @version 1.0 Mar 7, 2013
 */
public class KeyValuePair {
	private String key;
	private int size;

	/**
	 * @param key
	 * @param size
	 */
	public KeyValuePair(String key, int size) {
		this.key = key;
		this.size = size;
	}

	/**
	 * Getter for the key
	 * 
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Getter for the valueSize
	 * 
	 * @return the valueSize
	 */
	public int getSize() {
		return size;
	}
}
