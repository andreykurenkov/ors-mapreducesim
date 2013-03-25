package mapreducesim.storage;

import java.util.ArrayList;
import java.util.List;

import org.simgrid.msg.*;

import mapreducesim.core.SimMain;
import mapreducesim.core.SimProcess;
import mapreducesim.scheduling.InputSplit;
import mapreducesim.storage.FileTransferTask.*;

public class StorageProcess extends SimProcess {
	// private int filesize;
	public static String STORAGE_MAILBOX = "Storage";
	public static StorageProcess instance;
	public DataTree<Node> fs;
	public DataTree<Node> top;

	public StorageProcess(Host host, String name, String[] args) {
		super(host, name, args, STORAGE_MAILBOX);
		instance = this;
		FSBuilder fsbuild = new FSBuilder();
		fsbuild.createTestTopology();
		this.fs = fsbuild.getFS();
		this.top = fsbuild.getTopology();
	}

	@Override
	public void main(String[] args) throws TransferFailureException,
			HostFailureException, TimeoutException {
		while (!finished) {
			// get the next task from the storage interface mailbox
			Task currentTask = Task.receive(MAILBOX);
			// handle task appropriately

			if (currentTask instanceof WriteRequestTask) { // write task
				// update the actual filesystem, etc.
				Msg.info("Writing file '"
						+ ((WriteRequestTask) currentTask).getFileBlock()
						+ "' at " + this.getTimeElapsed());
				currentTask = (WriteRequestTask) currentTask;

				long costRemaining = 6; // dummy value for now.
				// TODO: use elapseTime
				Msg.info("Finished writing file '"
						+ ((WriteRequestTask) currentTask).getFileBlock()
						+ "' at " + Msg.getClock());

			}

			if (currentTask instanceof ReadRequestTask) { // read task
				// TODO combine interfaces...
				Msg.info("Reading file '"
						+ ((ReadRequestTask) currentTask).getName() + "' at "
						+ this.getTimeElapsed());

				// Get the information needed from the task
				String filename = ((ReadRequestTask) currentTask).getFilename();
				int offset = ((ReadRequestTask) currentTask).getOffset();
				int length = ((ReadRequestTask) currentTask).getLength();
				String origin = ((ReadRequestTask) currentTask)
						.getOriginMailbox();

				// Get the blocks needed
				// DataTree<Node> fs = StorageMain.getFS();
				// DataTree<Node> top = StorageMain.getTopology();

				// Increment the read count
				// ((File) fs.get(filename)).incrementReads();

				// Get the closest block
				// int speed = loc1.speedBetween(loc2);
				// int size = loc1.get().getSize();
				// long readCost = size / ( speed * 2^20 );
				// elapseTime(readCost);

				Msg.info("Finished reading file '"
						+ ((ReadRequestTask) currentTask).getName() + "' at "
						+ this.getTimeElapsed());
			}

		}
	}

	public void addFile(String filename, int size) {
		File newfile = new File(fs.getRoot(), filename, size);
		fs.getRoot().addChild(newfile);
		newfile.placeSplits(top);
	}

	/**
	 * Returns the entire topology. To get an individual element, first get the
	 * root element (e.g., root = getTopology().getRoot()), then you can climb
	 * down the tree to get the element you want (e.g.,
	 * root.getChild("rack1").getChild("dn1a")). It's probably easier just to
	 * use the get(String nodeName) method to get a specific element.
	 * 
	 * @return The DataTree of the topology
	 */
	public DataTree<Node> getTopology() {
		return top;
	}

	/*
	 * public List<String> getPreferredLocations(InputSplit input) {
	 * List<DataLocation> locations = new ArrayList<DataLocation>(3);
	 * DataLocation loc0 = input.getLocations().get(0); DataLocation loc1 =
	 * input.getLocations().get(1); DataLocation loc2 =
	 * input.getLocations().get(2); locations.add(loc0); locations.add(loc1);
	 * locations.add(loc2); return locations; }
	 */

	/**
	 * Very generic search to return any kind of created storage node--File,
	 * FileBlock, Rack, DataNode, etc. If none is found returns null. Caution:
	 * if more than one object with the same name field exists, only the last
	 * one of them found is returned.
	 * 
	 * @return the requested node
	 */
	public Node get(String query) {
		Node result;
		result = fs.get(query);
		result = top.get(query);
		return result;
	}

	/**
	 * Returns a list of all fileblocks for the file
	 * 
	 * @param filename
	 *            the unique filename of the file to get blocks for
	 * @return a list of the fileblocks for the file
	 */
	public List<FileBlock> getBlocks(String filename) {
		// get the requested file
		Node file = fs.get(filename);
		// validate the file retrieved (non-null, is file)
		if (file == null || !(file.isFile())) {
			Msg.info("No file with name " + filename + "found."); // error
			return null;
		} else {
			return ((File) file).getBlocks();
		}
	}

	/**
	 * Returns a list of all fileblocks in a file needed for the given offset
	 * and length.
	 * 
	 * Example: If file is size 300 and split size is 100, it has three blocks:
	 * (0)[0..99], (1)[100..199], and (3)[200..299]. getBlocks(filename, 50,
	 * 100) would return blocks (0) and (1).
	 * 
	 * @param filename
	 * @param offset
	 * @param length
	 * @return a list of the blocks representing the specified region of the
	 *         file.
	 */
	public List<FileBlock> getBlocks(String filename, int offset, int length) {
		// get the requested file
		Node file = fs.get(filename);
		// validate the file retrieved (non-null, is file)
		if (file == null || !(file.isFile())) {
			Msg.info("No file with name " + filename + "found."); // error
			return null;
		} else {
			return ((File) file).getNeededFileBlocks((double) offset, length);
		}
	}

	/**
	 * Hadoop stores three replicas of each FileBlock on three separate
	 * DataNodes. Returns a list of DataNodes where a replica of the FileBlock
	 * lives.
	 * 
	 * Example: Use getBlocks(...) to get a list of FileBlocks. For each
	 * FileBlock, this method can be called to get a list of the three
	 * DataBlocks replicas are on.
	 * 
	 * @param block
	 *            the FileBlock to find locality information on
	 * @return a list of the DataNodes a replica of this FileBlock is on
	 */
	public List<DataNode> getLocations(FileBlock block) {
		return block.getLocations();
	}
}