package mapreducesim.storage;

import java.util.List;

import org.simgrid.msg.Msg;

/**
 * 
 * An easy way of accessing methods for interfacing with storage.
 * 
 * @author Matthew O'Shaughnessy
 * 
 */
public class StorageInterface {
	private DataTree<Node> fs;
	private DataTree<Node> top;

	/**
	 * Constructor creates the fs/topology and loads it into StorageInterface
	 * private fields
	 */
	public StorageInterface() {
		FSBuilder fsbuild = new FSBuilder();
		fsbuild.createTestTopology();
		this.fs = fsbuild.getFS();
		this.top = fsbuild.getTopology();
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

	/**
	 * Add a new file to the filesystem and assign its blocks to DataNodes in
	 * the topology. The topology will be updated with three replicas of each
	 * block of the file assigned to DataBlocks, with two replicas on different
	 * DataNodes on the same rack, and the third replica on a separate rack
	 * (default Hadoop placement policy)
	 * 
	 * @param filename
	 * @param size
	 */
	public void addFile(String filename, int size) {
		// Add to the filesystem
		Directory parent = (Directory) fs.getRoot().getChild("dir1");
		File file = new File(parent, filename, size);
		fs.getRoot().getChild("dir1").addChild(file);
		// Place the splits in the topology
		file.placeSplits(top);
	}

}
