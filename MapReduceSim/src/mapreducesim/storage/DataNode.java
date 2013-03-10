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

	public int numBlocks() {
		return this.blocks.size();
	}

	public void printContentsToTerminal() {
		System.out.println("Contents of DataBlock '" + this.getName()
				+ "': (format: File:Split#)");
		for (int i = 0; i < blocks.size(); i++) {
			System.out.print(this.getBlockAtPosition(i).getOwner().getName()
					+ ":" + this.getBlockAtPosition(i).getIndex() + ";");
		}
		System.out.print("\b\b\n");
	}

	public void printContents() {
		Msg.info("Contents of DataBlock '" + this.getName()
				+ "': (format: File:Split#)\n\t");
		for (int i = 0; i < blocks.size(); i++) {
			Msg.info(this.getBlockAtPosition(i).getOwner().getName() + ":"
					+ this.getBlockAtPosition(i).getIndex() + ";");
		}
		Msg.info("\b\b\n");
	}
}
