package mapreducesim.storage;

import java.util.ArrayList;

import org.simgrid.msg.Host;

/**
 * Represents a file in the file system.  Each file is divided into blocks (FileBlock)
 * of a splitSize defined for each job.  Files are represented and do not have a physical
 * location--these are FileBlocks, which have FileBlockLocations.
 */

public class File extends DataTreeNode {

	public class FileLocation {

	}

	private ArrayList<FileBlock> blocks;
	private static final int SPLIT_SIZE = 128;

	public File(DataTreeNode parent, String name) {
		super(parent, name);
		this.blocks = new ArrayList<FileBlock>();
		blocks.add(new FileBlock(this, 0, File.SPLIT_SIZE, new FileBlockLocation(0,0)));
	}

	public double calculateReadCost(Host requestor) {
		return 5;
	}

}

