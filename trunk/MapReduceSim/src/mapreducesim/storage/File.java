package mapreducesim.storage;

import java.util.ArrayList;

/**
 * Represents a file in the file system. Each file is divided into blocks
 * (FileBlock) of a split size defined for each job. Files are represented and
 * do not have a physical location--these are FileBlocks, which have
 * FileBlockLocations.
 * 
 * @author Matthew O'Shaughnessy
 */

public class File extends Node {

	private ArrayList<FileBlock> blocks;
	private static final int SPLIT_SIZE = 128;
	private int size;

	/**
	 * Default constructor
	 * 
	 * @param parent
	 * @param name
	 */
	public File(Node parent, String name) {
		super(parent, name);
		this.blocks = new ArrayList<FileBlock>();
		split();
	}

	public void split() {
		int i = 0;
		for (int written = 0; written < size; written += File.SPLIT_SIZE) {
			FileBlock split = new FileBlock(this, i, File.SPLIT_SIZE,
					new FileBlockLocation(0, 0));
			this.blocks.add(split);
			i++;
		}
	}

	public ArrayList<FileBlock> getBlocks() {
		return blocks;
	}
}
