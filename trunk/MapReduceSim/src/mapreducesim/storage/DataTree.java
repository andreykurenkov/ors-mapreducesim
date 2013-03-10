package mapreducesim.storage;

/**
 * 
 * Holds the root element for a data tree. Makes it easier to maintain the
 * seperation between the fs tree and topology tree.
 * 
 */
public class DataTree<T> {

	private Node rootElement;

	/**
	 * Constructor without defining the root
	 */
	public DataTree() {
		super();
	}

	/**
	 * Constructor defining the root
	 * 
	 * @param rootElement
	 */
	public DataTree(Node rootElement) {
		super();
		this.rootElement = rootElement;
	}

	/**
	 * setter for root element
	 * 
	 * @param rootElement
	 */
	public void setRoot(Node rootElement) {
		this.rootElement = rootElement;
	}

	/**
	 * getter for root element
	 * 
	 * @return DataTreeNode rootElement
	 */
	public Node getRoot() {
		return rootElement;
	}

}