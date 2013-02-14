package mapreducesim.core;

import java.util.ArrayList;

import mapreducesim.storage.File;

public class HostData {
	private ArrayList<File> localFiles;
	private int totalMemory;
	private int memoryUsed;

	/**
	 * @param localFiles
	 * @param totalMemory
	 * @param memoryUsed
	 */
	public HostData(ArrayList<File> files, int totalMemory, int memoryUsed) {
		this.localFiles = files;
		this.totalMemory = totalMemory;
		this.memoryUsed = memoryUsed;
	}

	public HostData() {
		this(new ArrayList<File>(), 100000, 0);
	}

	public void addFile(File toAdd) {
		localFiles.add(toAdd);
	}

	/**
	 * @return the totalMemory
	 */
	public int getTotalMemory() {
		return totalMemory;
	}

	/**
	 * @param totalMemory
	 *            the totalMemory to set
	 */
	public void setTotalMemory(int totalMemory) {
		this.totalMemory = totalMemory;
	}

	/**
	 * @return the memoryUsed
	 */
	public int getMemoryUsed() {
		return memoryUsed;
	}

}