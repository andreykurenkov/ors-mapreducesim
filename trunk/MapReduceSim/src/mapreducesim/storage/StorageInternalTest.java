package mapreducesim.storage;

public class StorageInternalTest {

	public static void main(String[] args) {
		System.out.print("StorageTest begin.\n\n");

		// Create the tree
		// System.out.print("Creating datatree...");
		// DataTree<DataTreeNode> tree = new DataTree<DataTreeNode>();
		// System.out.print("complete.\n");

		// Add the root node to the tree
		System.out.println("Adding a root node...");
		Directory dataTreeRoot = new Directory();
		// tree.setRootElement(rootElement);
		System.out.println("Path to root: '" + dataTreeRoot.getPath() + "'");

		// Add two directories under root
		System.out.println("Adding two directories...");
		Directory dir1 = new Directory(dataTreeRoot, "dir1");
		dataTreeRoot.addChild(dir1);
		Directory dir2 = new Directory(dataTreeRoot, "dir2");
		dataTreeRoot.addChild(dir2);
		System.out.println("Path to this directory: '"
				+ dataTreeRoot.getChildAtPosition(0).getPath() + "'");
		System.out.println("Path to this directory: '"
				+ dataTreeRoot.getChildAtPosition(1).getPath() + "'");

		// Add some Files to one of the directories
		System.out.println("Adding some Files...");
		File File1 = new File(dir1, "file1");
		File File2 = new File(dir1, "file2");
		dir2.addChild(File1);
		dir2.addChild(File2);
		System.out.println("Path to this File: '" + File1.getPath() + "'");
		System.out.println("Path to this File: '" + File2.getPath() + "'");

		// Add a bunch more Files
		dir1.addChild(new File(dir1, "filea"));
		dir1.addChild(new File(dir1, "fileb"));
		dir1.addChild(new File(dir1, "filec"));
		dir1.addChild(new File(dir1, "filed"));

		// Print the contents of directories
		dir2.ls();
		dir1.ls();

		// Test the contains function
		Directory dir3 = new Directory(dir2, "dir3");
		File File9 = new File(dir3, "fileX");
		System.out.println("Directory 2 contains 'fileX':"
				+ dir2.contains(File9) + ".");

		System.out.println("\nStorageTest end.");

	}
}
