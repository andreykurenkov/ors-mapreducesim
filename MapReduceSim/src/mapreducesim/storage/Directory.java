package mapreducesim.storage;

public class Directory extends DataTreeNode {

	public Directory() { //root directory
		super();
	}
	public Directory(DataTreeNode parent, String name) {
		super(parent, name);
	}
}
