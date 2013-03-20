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
	private int speed; // Connection node and its parent in gbps

	/**
	 * Constructor defines the name of the DataBlock and its parent
	 * 
	 * @param parent
	 * @param name
	 */
	public DataNode(Rack parent, String name) {
		super(parent, name);
		setSpeed(5);
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

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getSpeed() {
		return speed;
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
	 * Calculates the chokepoint bandwidth between two nodes
	 * 
	 * @param node1
	 * @param node2
	 * @return
	 */
	public int speedBetween(Node node2) {
		Node n1, n2;
		int l1, l2;
		int minSpeed = 99999;
		n1 = this;
		n2 = node2;
		l1 = this.getLevel();
		l2 = node2.getLevel();

		// if node1 is deeper than node2,
		// climb up the branch of node1 until it's at the same level of node2
		while (l1 > l2 && l1 != 0) {
			if (n1.getSpeed() < minSpeed)
				minSpeed = n1.getSpeed();
			n1 = n1.getParent();
			--l1;
		}

		// if node2 is deeper than node2,
		// climb up the branch of node2 until it's at the same level of node1
		while (l2 > l1 && l2 != 0) {
			if (n2.getSpeed() < minSpeed)
				minSpeed = n2.getSpeed();
			--l2;
		}

		// now that node1 and node2 are on the same level (depth),
		// climb both up the branch until they have a common parent
		while (l1 != 0 && l2 != 0 && n1.getParent() != n2.getParent()) {
			n1 = n1.getParent();
			n2 = n2.getParent();
			if (n1.getSpeed() < minSpeed)
				minSpeed = n1.getSpeed();
			if (n2.getSpeed() < minSpeed)
				minSpeed = n2.getSpeed();
		}

		// account for the final step up after the parents match
		// TODO: might be missing one link somewhere in here; check when less
		// tired.

		return minSpeed;
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
