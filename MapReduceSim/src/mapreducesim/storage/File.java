package mapreducesim.storage;

import java.util.ArrayList;

import org.simgrid.msg.Host;

/**
 * Represents a file in the file system.  Each file is divided into blocks (FileBlock)
 * of a splitSize defined for each job.
 */

public class File extends DataTreeNode {
	private Boolean isDirectory;
	private ArrayList<FileBlock> blocks;
	private static final int SPLIT_SIZE = 128;

	public File(DataTreeNode parent, String name) {
		super(parent, name);
		this.blocks = new ArrayList<FileBlock>();
		blocks.add(new FileBlock(this, 0, File.SPLIT_SIZE, new FileBlockLocation(0,0)));
	}

	public Boolean getIsDirectory() {
		return isDirectory;
	}

	public void setIsDirectory(Boolean isDirectory) {
		this.isDirectory = isDirectory;
	}

	public double calculateReadCost(Host requestor) {
		return 5;
	}

}