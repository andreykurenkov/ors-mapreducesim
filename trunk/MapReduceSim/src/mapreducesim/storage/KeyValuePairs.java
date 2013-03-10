package mapreducesim.storage;

/**
 * Simple representation of a pair. Needed to keep track of keys (does not store actual value)
 * 
 * @author Andrey Kurenkov
 * @version 1.0 Mar 7, 2013
 */
public class KeyValuePairs {
	private String id;
	private int numPairs;
	private int sizeOfPair;

	private static int count;

	/**
	 * @param numPairs
	 * @param size
	 */
	public KeyValuePairs(String id, int numPairs, int size) {
		this.id = id;
		this.numPairs = numPairs;
		this.sizeOfPair = size;
		count++;
	}

	/**
	 * @param numPairs
	 * @param size
	 */
	public KeyValuePairs(int numPairs, int size) {
		this(Integer.toString(count), numPairs, size);
	}

	/**
	 * Getter for the key
	 * 
	 * @return the key
	 */
	public int getNumPairs() {
		return numPairs;
	}

	/**
	 * Getter for the valueSize
	 * 
	 * @return the valueSize
	 */
	public int getSizeOfPair() {
		return sizeOfPair;
	}

	/**
	 * 
	 * @return
	 */
	public int getTotalSize() {
		return sizeOfPair * numPairs;
	}
}
