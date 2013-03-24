package mapreducesim.scheduling;

import java.util.ArrayList;
import java.util.List;

import mapreducesim.storage.DataLocation;

/**
 * Hadoop-esque class to store locations of data to be processed by a
 * Mapper.
 * 
 * @author Andrey Kurenkov
 * 
 */
public class InputSplit {
	private List<DataLocation> locations;
	private int size;

	public String toString() {
		return "InputSplit:" + locations.toString();
	}

	/**
	 * Simple constructor that instantiates locations.
	 * 
	 * @param locations
	 */
	public InputSplit(List<DataLocation> locations) {
		this.locations = locations;
		for (DataLocation loc : locations)
			size += loc.getLength();
	}

	/**
	 * Empty constructor that creates an inputsplit containing no data
	 * locations.
	 */
	public InputSplit() {
		locations = new ArrayList<DataLocation>();
		size = 0;
	}

	/**
	 * Getter for the locations
	 * 
	 * @return the locations
	 */
	public List<DataLocation> getLocations() {
		return locations;
	}

	/**
	 * Getter for the size
	 * 
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

}