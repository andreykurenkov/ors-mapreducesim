package mapreducesim.core;

import org.simgrid.msg.Host;

public class SimFile {
	private String name;

	public static class SimFileLocation {
		private String hostName;
		private String fileName;
		private int fileBlock;
	}
}
