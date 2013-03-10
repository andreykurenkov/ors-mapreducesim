package mapreducesim.storage;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a file in the file system. Each file is divided into blocks
 * (FileBlock) of a split size defined for each job. Files are represented and
 * do not have a physical location--these are FileBlocks, which have
 * FileBlockLocations.
 * 
 * @author Matthew O'Shaughnessy
 */

public class File extends Node {

	public class FileLocation {

	}

	private List<FileBlock> blocks;
	private static final int SPLIT_SIZE = 128;

	/**
	 * Default constructor
	 * 
	 * @param parent
	 * @param name
	 */
	public File(Node parent, String name) {
		super(parent, name);
		this.blocks = new ArrayList<FileBlock>();
		blocks.add(new FileBlock(this, 0, File.SPLIT_SIZE,
				new FileBlockLocation(0, 0)));
	}

	/**
	 * Don't use this constructor--files must have a parent directory.
	 */
	public File(String name) {
		super();
		this.setName(name);
	}
}
