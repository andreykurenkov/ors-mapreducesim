package mapreducesim.core;

import org.simgrid.msg.Host;

//TODO: define FileBlock?
public class SimFile {
	private String name;
	private static int fileCount;
	private int size;

	public SimFile(String name, int size) {
		this.name = name;
		this.size = size;
		fileCount++;
	}

	public SimFile(int size) {
		this("File " + fileCount, size);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the sizeInMemory
	 */
	public int getSize() {
		return size;
	}

	public static class SimFileLocation {
		private String hostName;
		private String fileName;
		private int fileBlock;

		/**
		 * @param hostName
		 * @param fileName
		 * @param fileBlock
		 */
		public SimFileLocation(String hostName, String fileName, int fileBlock) {
			this.hostName = hostName;
			this.fileName = fileName;
			this.fileBlock = fileBlock;
		}

	}
}
