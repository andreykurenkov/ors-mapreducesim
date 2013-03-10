package mapreducesim.storage;

/**
 * 
 * Holds the root element for a tree. Not really needed, but I'm not sure I want
 * to get rid of it yet.
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
	public void setRootElement(Node rootElement) {
		this.rootElement = rootElement;
	}

	/**
	 * getter for root element
	 * 
	 * @return DataTreeNode rootElement
	 */
	public Node getRootElement() {
		return rootElement;
	}

}