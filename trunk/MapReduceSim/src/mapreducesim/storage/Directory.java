package mapreducesim.storage;

/**
 * 
 * A special case of File that represents a directory.
 * 
 * @author Matthew O'Shaughnessy
 * 
 */

public class Directory extends DataTreeNode {

	/**
	 * No-args constructor represents the root directory
	 */
	public Directory() {
		super();
	}

	/**
	 * Creates a Directory object as a child of the specified parent with the
	 * specified name
	 * 
	 * @param parent
	 * @param name
	 */
	public Directory(DataTreeNode parent, String name) {
		super(parent, name);
	}

	/**
	 * Like its namesake, prints a list of the files in the current directory to
	 * the terminal, separated with newlines
	 */
	public void ls() {
		if (this.getChildren().size() != 0) {
			System.out.println("Contents of directory '" + this.getName()
					+ "' (" + this.numChildren() + " items):");
			for (int i = 0; i < this.getChildren().size(); i++) {
				System.out.println("\t" + this.getChildAtPosition(i).getName());
			}
		}
	}
}
