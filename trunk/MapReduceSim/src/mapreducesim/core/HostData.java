package mapreducesim.core;

import java.util.ArrayList;

public class HostData {
	private ArrayList<SimFile> localFiles;
	private int totalMemory;
	private int memoryUsed;

	/**
	 * @param localFiles
	 * @param totalMemory
	 * @param memoryUsed
	 */
	public HostData(ArrayList<SimFile> files, int totalMemory, int memoryUsed) {
		this.localFiles = files;
		this.totalMemory = totalMemory;
		this.memoryUsed = memoryUsed;
	}

	public HostData() {
		this(new ArrayList<SimFile>(), 100000, 0);
	}

	public void addFile(SimFile toAdd) {
		localFiles.add(toAdd);
	}

	/**
	 * @return the totalMemory
	 */
	public int getTotalMemory() {
		return totalMemory;
	}

	/**
	 * @param totalMemory the totalMemory to set
	 */
	public void setTotalMemory(int totalMemory) {
		this.totalMemory = totalMemory;
	}
}
