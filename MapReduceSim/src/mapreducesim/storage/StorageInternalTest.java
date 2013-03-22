package mapreducesim.storage;

import java.util.List;

/**
 * An internal class for verifying the operation of the storage system.
 * 
 * @author matthew
 * 
 */
public class StorageInternalTest {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		System.out.println("Begin storage test\n\n");

		StorageInterface storage = new StorageInterface();

		List<FileBlock> blocks = storage.getBlocks("file1", 25, 50);
		List<DataNode> locations = storage.getLocations(blocks.get(0));

		// Set breakpoint here to verify fs/topology

		System.out.println("\n\nStorage test complete.");
	}
}
