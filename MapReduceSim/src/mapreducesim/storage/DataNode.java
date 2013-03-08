package mapreducesim.storage;

import java.util.ArrayList;

/**
 * 
 * Special case of Node that represents a DataNode.
 * 
 * @author Matthew O'Shaughnessy
 * 
 */
public class DataNode extends Node {
	private ArrayList<FileBlock> blocks;

	public DataNode(Rack parent, String name) {
		super(parent, name);
	}

	public FileBlock getBlockAtPosition(int position) {
		return this.blocks.get(position);
	}

	/**
	 * 
	 * Removes a FileBlock with the given file "owner" and index as part of that
	 * file from the contents of the DataNode. For example, if you want to
	 * delete the third split of file "filea," it would be defined in those
	 * terms.
	 * 
	 * @param fileToRemove
	 *            The file the split wanted to remove is part of
	 * @param indexToRemove
	 *            The index the split wanted to remove is (together, these
	 *            define a FileBlock)
	 */
	public void removeBlock(File fileToRemove, int indexToRemove) {
		for (int i = 0; i < blocks.size(); i++) {
			if (blocks.get(i).getOwner() == fileToRemove
					&& blocks.get(i).getIndex() == indexToRemove) {
				blocks.remove(i);
			}
		}
	}

	public void printContents() {
		System.out.println("Contents of DataBlock" + this.getName() + ":");
		System.out.println("(format: 'File:Split#')");
		for (int i = 0; i < this.blocks.size(); i++) {
			System.out.print(this.getBlockAtPosition(i).getOwner().getName()
					+ ":" + this.getBlockAtPosition(i).getIndex() + ", ");
		}
		System.out.print("\b\b\n");
	}

	public void addBlock(FileBlock block) {
		this.blocks.add(block);
	}

	public void addBlocks(ArrayList<FileBlock> blocksToAdd) {
		for (int i = 0; i < blocksToAdd.size(); i++) {
			this.blocks.add(blocksToAdd.get(i));
		}
	}

	public ArrayList<FileBlock> getBlocks() {
		return blocks;
	}

	public void setBlocks(ArrayList<FileBlock> blocks) {
		this.blocks = blocks;
	}

}
