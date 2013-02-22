package mapreducesim.storage;

/**
 * 
 * Holds the root element for a tree.  Not really needed,
 * but I'm not sure I want to get rid of it yet.
 *
 */
public class DataTree<T> {
	
	private DataTreeNode rootElement;
	
	public DataTree() {
		super();
	}
	public DataTree(DataTreeNode rootElement) {
		super();
		this.rootElement = rootElement;
	}
	
	public void setRootElement(DataTreeNode rootElement) {
		this.rootElement = rootElement;
	}
	public DataTreeNode getRootElement() {
		return rootElement;
	}
	
}