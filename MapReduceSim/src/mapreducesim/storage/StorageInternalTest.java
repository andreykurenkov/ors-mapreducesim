package mapreducesim.storage;

public class StorageInternalTest {
	
	public static void main(String[] args) {
		System.out.print("StorageTest begin.\n\n");
		
		//Create the tree
		System.out.print("Creating datatree...");
		DataTree<DataTreeNode> tree = new DataTree<>();
		System.out.print("complete.\n");
		
		//Add the root node to the tree
		System.out.println("Adding a root node...");
		Directory rootElement = new Directory();
		tree.setRootElement(rootElement);
		System.out.println("Path to root: '" + tree.getRootElement().getPath() + "'");
		
		//Add two directories under root
		System.out.println("Adding two directories...");
		Directory dir1 = new Directory(rootElement, "Directory 1");
		tree.getRootElement().addChild(dir1);
		Directory dir2 = new Directory(rootElement, "Directory 2");
		tree.getRootElement().addChild(dir2);
		System.out.println("Path to this directory: '" + tree.getRootElement().getChildAtPosition(0).getPath() + "'");
		System.out.println("Path to this directory: '" + tree.getRootElement().getChildAtPosition(1).getPath() + "'");
		
		//Add some files to one of the directories
		System.out.println("Adding some files...");
		File file1 = new File(dir1, "file");
		File file2 = new File(dir1, "anotherfile");
		dir2.addChild(file1);
		dir2.addChild(file2);
		System.out.println("Path to this file: '" + file1.getPath() + "'");
		System.out.println("Path to this file: '" + file2.getPath() + "'");
		
		//Add a bunch more files
		dir1.addChild(new File(dir1, "uenatoshueoh"));
		dir1.addChild(new File(dir1, "utehoatsuh"));
		dir1.addChild(new File(dir1, "utnsehoastuh"));
		dir1.addChild(new File(dir1, "uehtoasthu"));
		
		//Print the contents of directories
		dir2.ls();
		dir1.ls();
		
		System.out.println("\nStorageTest end.");
		
	}
	
}
