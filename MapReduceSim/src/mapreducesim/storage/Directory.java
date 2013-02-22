package mapreducesim.storage;

/**
 * 
 * A special case of file that represents a directory.
 *
 */

public class Directory extends DataTreeNode {

	public Directory() { //root directory
		super();
	}
	public Directory(DataTreeNode parent, String name) {
		super(parent, name);
	}
	
	//Prints the contents of a directory
	public void ls() {
		if (this.getChildren().size()!=0) {		
			System.out.println("Contents of directory '" + this.getName() + "' (" + this.numChildren() + " items):");
			for (int i=0; i<this.getChildren().size(); i++) {
				System.out.println("\t" + this.getChildAtPosition(i).getName());
			}
		}
	}
}
