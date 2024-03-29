package mapreducesim.storage;

import java.util.ArrayList;
import java.util.List;

import mapreducesim.core.SimProcess;
import mapreducesim.scheduling.InputSplit;
import mapreducesim.scheduling.NotifyNoMoreTasks;
import mapreducesim.storage.FileTransferTask.ReadRequestTask;
import mapreducesim.storage.FileTransferTask.WriteRequestTask;

import org.simgrid.msg.Host;
import org.simgrid.msg.HostFailureException;
import org.simgrid.msg.Msg;
import org.simgrid.msg.Task;
import org.simgrid.msg.TimeoutException;
import org.simgrid.msg.TransferFailureException;

public class StorageProcess extends SimProcess {
	// private int filesize;
	public static String DEFAULT_STORAGE_MAILBOX = "Storage";
	public static DataTree fs;
	public static DataTree top;

	static {
		FSBuilder fsbuild = new FSBuilder();
		fsbuild.createTopology();
		fs = fsbuild.getFS();
		top = fsbuild.getTopology();
	}

	public StorageProcess(Host host, String name, String[] args) {
		super(host, name, args);
	}

	@Override
	public void main(String[] args) throws TransferFailureException,
			HostFailureException, TimeoutException {
		while (!finished) {
			// get the next task from the storage interface mailbox
			Task currentTask = Task.receive(MAILBOX, 10000);
			// handle task appropriately
			if (currentTask instanceof NotifyNoMoreTasks) {
				finish();
			}
			if (currentTask instanceof WriteRequestTask) { // write task
				// update the actual filesystem, etc.
				Msg.info("Writing file '"
						+ ((WriteRequestTask) currentTask).getFileBlock()
						+ "' at " + this.getTimeElapsed());
				// currentTask.execute();
				// simulate the expense
				Msg.info("Finished writing file '"
 + ((WriteRequestTask) currentTask).getName());
			}

			if (currentTask instanceof ReadRequestTask) { // read task
				// update the actual filesystem, etc. (metadata for read)
				Msg.info("Reading file '"
 + ((ReadRequestTask) currentTask).getName());
				// simulate the expense
				double readcost = 0.0;
				double blockreadcost = 0.0;
				String origin = ((ReadRequestTask) currentTask)
						.getOriginMailbox();
				int offset = ((ReadRequestTask) currentTask).getOffset();
				int length = ((ReadRequestTask) currentTask).getLength();
				String filename = ((ReadRequestTask) currentTask).getFilename();
				File file = (File) this.fs.get(filename);
				if (file == null) {
					Msg
							.info("Requested file does not exist in the filesystem.");
				} else {
					List<FileBlock> blocks = file.getNeededFileBlocks(
							(int) offset, length);
					DataNode originloc = (DataNode) this.top.get(origin);
					for (FileBlock b : blocks) {
						for (int i = 0; i < b.getLocations().size(); i++) {
							if (b.getLocation(i).equals(originloc)) {
								blockreadcost = 1; // on same DN
							} else if (b.getLocation(0).isOnSameRackAs(
									originloc)) {
								if (blockreadcost > 0.2) {
									blockreadcost = 2; // On same rack
								}
							} else {
								if (blockreadcost > 5) {
									blockreadcost = 5; // Not on same rack.
								}
							}
							readcost += blockreadcost;
						}
					}
					elapseTime(readcost);
				}
				((ReadRequestTask) currentTask).setReadDone();
				if (file != null)
					(new FileTransferTask(file.getNeededFileBlocks(offset, length))).send(origin);
				else
					(new FileTransferTask(new ArrayList<FileBlock>())).send(origin);// TODO: handle error cases
				// ArrayList<FileBlock> fakeRead = new ArrayList<FileBlock>();
				// fakeRead.add(new FileBlock(null, 50, new KeyValuePairs()));
				// (new FileTransferTask(fakeRead)).send(loc);
				Msg.info("Finished reading file '"
						+ ((ReadRequestTask) currentTask).getName() + "' at "
						+ this.getTimeElapsed());
			}

		}
	}

	public static void addFile(String filename, int size) {
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
	public static DataTree getTopology() {
		return top;
	}

	/**
	 * Extracts the host from each of the DataLocation s in the input InputSplit
	 * 
	 * @param input
	 * @return list of strings with host names
	 */
	public static List<String> getPreferredLocations(InputSplit input) {
		// Extract the locations for all the splits
		List<DataLocation> locations = input.getLocations();
		List<String> hosts = new ArrayList<String>();
		for (int i = 0; i < locations.size(); i++) {
			hosts.add(locations.get(i).getHost());
		}
		return hosts;
	}

	/**
	 * Very generic search to return any kind of created storage node--File,
	 * FileBlock, Rack, DataNode, etc. If none is found returns null. Caution:
	 * if more than one object with the same name field exists, only the last
	 * one of them found is returned.
	 * 
	 * @return the requested node
	 */
	public static Node get(String query) {
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
	public static List<FileBlock> getBlocks(String filename) {
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
	public static List<FileBlock> getBlocks(String filename, int offset,
			int length) {
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
	public static List<DataNode> getLocations(FileBlock block) {
		return block.getLocations();
	}
}