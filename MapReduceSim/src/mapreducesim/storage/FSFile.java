package mapreducesim.storage;

import org.simgrid.msg.Host;

/*
 * The FSFile is a traditional file in the filesystem.  It is broken into DataBlock splits, but the FSFile object represents the entire file as it is is the filesystem.
 */

public class FSFile extends DataTreeNode {
	private Boolean isDirectory;
	
	public FSFile(DataTreeNode parent, String name) {
		super(parent, name);
	}
	public Boolean getIsDirectory() {
		return isDirectory;
	}
	public void setIsDirectory(Boolean isDirectory) {
		this.isDirectory = isDirectory;
	}
	public double calculateReadCost(Host requestor) {
		return 0.01;
	}	
}