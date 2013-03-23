package mapreducesim.storage;

/**
 * An internal class for verifying the operation of the storage system.
 * 
 * @author matthew
 * 
 */
public class StorageInternalTest {

	public static void main(String[] args) {
		System.out.println("Begin storage test\n\n");

		StorageInterface storage = new StorageInterface();

		storage.addFile("newfile", 500);

		// Set breakpoint here to verify fs/topology

		System.out.println("\n\nStorage test complete.");
	}
}
