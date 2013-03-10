package mapreducesim.storage;

import java.util.List;

import org.simgrid.msg.Msg;

/**
 * Represents a DataNode object in the topology. Contains FileBlocks (splits)
 * 
 * @author matthew
 * 
 */
public class DataNode extends Node {

	private List<FileBlock> blocks;
	private int capacity;

	/**
	 * Constructor defines the name of the DataBlock and its parent
	 * 
	 * @param parent
	 * @param name
	 */
	public DataNode(Rack parent, String name) {
		super(parent, name);
	}

	public List<FileBlock> getBlocks() {
		return blocks;
	}

	public FileBlock getBlockAtPosition(int position) {
		return this.blocks.get(position);
	}

	public void addBlock(FileBlock block) {
		this.blocks.add(block);
	}

	public void addBlockAtPosition(FileBlock block, int position) {
		this.blocks.add(position, block);
	}

	public void addBlocks(List<FileBlock> blocksToAdd) {
		for (int i = 0; i < blocksToAdd.size(); i++) {
			this.blocks.add(blocksToAdd.get(i));
		}
	}

	public void removeBlock(FileBlock block) {
		this.blocks.remove(block);
	}

	public void removeBlock(File fileToRemove, int indexToRemove) {
		FileBlock currentBlock;

		for (int i = 0; i < this.blocks.size(); i++) {
			currentBlock = this.blocks.get(i);
			if (currentBlock.getOwner() == fileToRemove
					&& currentBlock.getIndex() == indexToRemove) {
				this.blocks.remove(i);
			}
		}
	}

	public void removeBlock(int position) {
		this.blocks.remove(position);
	}

	public void setBlocks(List<FileBlock> blocks) {
		this.blocks = blocks;
	}

	/**
	 * Returns the number of blocks present on this DataNode.
	 * 
	 * @return int
	 */
	public int numBlocks() {
		return this.blocks.size();
	}

	public int getCapacity() {
		return this.capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	/**
	 * Calculates the amount of free space on the DataNode by iterating through
	 * the blocks and subtracting each block's size from the DataNode capacity.
	 * 
	 * @return amount of free space
	 */
	public int freeSpace() {
		int usedSpace = 0;
		for (int i = 0; i < numBlocks(); i++) {
			usedSpace += this.blocks.get(i).getSize();
		}
		return (capacity - usedSpace);
	}

	/**
	 * Prints the FileSplits on this DataNode to the terminal.
	 */
	public void printContentsToTerminal() {
		System.out.println("Contents of DataBlock '" + this.getName()
				+ "': (format: File:Split#)");
		for (int i = 0; i < blocks.size(); i++) {
			System.out.print(this.getBlockAtPosition(i).getOwner().getName()
					+ ":" + this.getBlockAtPosition(i).getIndex() + ";");
		}
		System.out.print("\b\b\n");
	}

	/**
	 * Prints the FileSplits present on this DataNode.
	 */
	public void printContents() {
		Msg.info("Contents of DataBlock '" + this.getName()
				+ "': (format: File:Split#)\n\t");
		for (int i = 0; i < blocks.size(); i++) {
			Msg.info(this.getBlockAtPosition(i).getOwner().getName() + ":"
					+ this.getBlockAtPosition(i).getIndex() + ";");
		}
		Msg.info("\b\b\n");
	}

	/**
	 * Returns the parent of the DataNode. For non-nested DataNodes.
	 * 
	 * @return (Rack) the parent of this DataNode.
	 */
	public Rack getRack() {
		return (Rack) this.getParent();
	}

	/**
	 * Is this DataNode a child of the same rack as the comparison DataNode?
	 * 
	 * @param node2
	 *            the comparison node
	 * @return true if on same rack. False if not.
	 */
	public boolean isOnSameRackAs(DataNode node2) {
		if (this.getRack() == node2.getRack())
			return true;
		else
			return false;
	}

	/**
	 * For nested DataNodes. Shouldn't be necessary right now.
	 */
	/*
	 * public Rack getRack() { Node currentNode = this; // climb until the
	 * parent is a Rack. while (!(currentNode instanceof Rack)) { currentNode =
	 * currentNode.getParent(); } return (Rack) currentNode; }
	 */
}
