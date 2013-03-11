package mapreducesim.storage;

import java.util.List;

public class StorageInternalTest {

	public static void main(String[] args) {
		System.out.print("StorageTest begin.\n\n");

		// 1. Create the tree for the filesystem
		System.out.print("Creating datatree...");
		DataTree<Node> fs = new DataTree<Node>();
		System.out.print("complete.\n");
		// Add the root node to the tree
		System.out.println("Adding a root node...");
		Directory fsRoot = new Directory();
		fs.setRoot(fsRoot);
		System.out.println("Path to root: '" + fs.getRoot().getPath() + "'");

		// Add two directories under root
		System.out.println("Adding two directories...");
		Directory dir1 = new Directory(fs.getRoot(), "dir1");
		fs.getRoot().addChild(dir1);
		Directory dir2 = new Directory(fs.getRoot(), "dir2");
		fs.getRoot().addChild(dir2);
		System.out.println("Path to this directory: '" + dir1.getPath() + "'");
		System.out.println("Path to this directory: '" + dir2.getPath() + "'");
		// Add some Files to the directories
		System.out.println("Adding some Files...");
		File File1 = new File(dir1, "file1", 300);
		File File2 = new File(dir1, "file2", 300);
		dir2.addChild(File1);
		dir2.addChild(File2);
		System.out.println("Path to this File: '" + File1.getPath() + "'");
		System.out.println("Path to this File: '" + File2.getPath() + "'");
		dir1.addChild(new File(dir1, "filea", 500));
		dir1.addChild(new File(dir1, "fileb", 500));
		dir1.addChild(new File(dir1, "filec", 500));
		dir1.addChild(new File(dir1, "filed", 500));
		// Print the contents of directories
		dir2.ls();
		dir1.ls();

		// Test the contains function
		Directory dir3 = new Directory(dir2, "dir3");
		File File9 = new File(dir3, "fileX", 500);
		System.out.println("Directory 2 contains 'fileX':"
				+ dir2.contains(File9) + ".");

		// Test FSBuilder
		FSBuilder fsbuild = new FSBuilder();
		String str = "before/after";
		List<String> tokstr = fsbuild.tokenize(str, '/');
		System.out.println();
		System.out.println("Results: '" + tokstr.get(0) + "','" + tokstr.get(1)
				+ "'");

		System.out.println("\nStorageTest end.");

	}
}
