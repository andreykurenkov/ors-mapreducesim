package mapreducesim.storage;

import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
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
	 * Returns a list of all fileblocks for the given filename
	 * 
	 * @param filename
	 * @return
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
	 * @param filename
	 * @param offset
	 * @param length
	 * @return
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
	 * Returns a list of DataNodes where a replica of the FileBlock lives.
	 * 
	 * @param block
	 * @return
	 */
	public List<DataNode> getLocations(FileBlock block) {
		return block.getLocations();
	}
}
