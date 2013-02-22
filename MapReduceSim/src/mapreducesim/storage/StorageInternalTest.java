package mapreducesim.storage;

public class StorageInternalTest {
	
	public static void main(String[] args) {
		System.out.print("StorageTest begin.\n\n");
		
		//Create the tree
		//System.out.print("Creating datatree...");
		//DataTree<DataTreeNode> tree = new DataTree<DataTreeNode>();
		//System.out.print("complete.\n");
		
		//Add the root node to the tree
		System.out.println("Adding a root node...");
		Directory dataTreeRoot = new Directory();
		//tree.setRootElement(rootElement);
		System.out.println("Path to root: '" + dataTreeRoot.getPath() + "'");
		
		//Add two directories under root
		System.out.println("Adding two directories...");
		Directory dir1 = new Directory(dataTreeRoot, "(dir)Best Piano Concertos");
		dataTreeRoot.addChild(dir1);
		Directory dir2 = new Directory(dataTreeRoot, "(dir)Best Viola Conceros");
		dataTreeRoot.addChild(dir2);
		System.out.println("Path to this directory: '" + dataTreeRoot.getChildAtPosition(0).getPath() + "'");
		System.out.println("Path to this directory: '" + dataTreeRoot.getChildAtPosition(1).getPath() + "'");
		
		//Add some Files to one of the directories
		System.out.println("Adding some Files...");
		File File1 = new File(dir1, "(file)Bartok");
		File File2 = new File(dir1, "(file)Walton");
		dir2.addChild(File1);
		dir2.addChild(File2);
		System.out.println("Path to this File: '" + File1.getPath() + "'");
		System.out.println("Path to this File: '" + File2.getPath() + "'");
		
		//Add a bunch more Files
		dir1.addChild(new File(dir1, "(file)Liszt A Major"));
		dir1.addChild(new File(dir1, "(file)Rachmaninov c minor"));
		dir1.addChild(new File(dir1, "(file)Brahms B-flat Major"));
		dir1.addChild(new File(dir1, "(file)Prokofiev g minor"));
		
		//Print the contents of directories
		dir2.ls();
		dir1.ls();

		
		System.out.println("\nStorageTest end.");
		
	}
	
}
