package mapreducesim.storage;

import java.util.List;

/**
 * An internal class for verifying the operation of the storage system.
 * 
 * @author matthew
 * 
 */
public class StorageInternalTest {

	public static void main(String[] args) {
		System.out.println("Begin storage test\n\n");

		System.out.println("Creating the test topology");
		FSBuilder fsbuild = new FSBuilder();
		fsbuild.createTestTopology();
		DataTree<Node> fs = fsbuild.getFS();
		DataTree<Node> topology = fsbuild.getTopology();

		// Set breakpoint here to verify fs/topology

		Node result = fs.get("file4");

		System.out.println("\n\nStorage test complete.");
	}
}
