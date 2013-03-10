package mapreducesim.storage;

/**
 * 
 * A special case of File that represents a directory.
 * 
 * @author Matthew O'Shaughnessy
 * 
 */

public class Directory extends Node {

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
	public Directory(Node parent, String name) {
		super(parent, name);
	}

	public void addFile(File file) {
		super.addChild(file);
	}

	public void removeDirectory(Directory dirToRemove) {
		for (int i = 0; i < this.getChildren().size(); i++) {
			if (this.getChildAtPosition(i) == dirToRemove) {
				this.removeChildNumber(i);
			}
		}
	}

	public void removeFile(File fileToRemove) {
		for (int i = 0; i < this.getChildren().size(); i++) {
			if (this.getChildAtPosition(i) == fileToRemove) {
				this.removeChildNumber(i);
			}
		}
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
