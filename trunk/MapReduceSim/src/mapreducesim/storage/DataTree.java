package mapreducesim.storage;

/**
 * 
 * Holds the root element for a tree. Not really needed, but I'm not sure I want
 * to get rid of it yet.
 * 
 */
public class DataTree<T> {

	private DataTreeNode rootElement;

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
	public DataTree(DataTreeNode rootElement) {
		super();
		this.rootElement = rootElement;
	}

	/**
	 * setter for root element
	 * 
	 * @param rootElement
	 */
	public void setRootElement(DataTreeNode rootElement) {
		this.rootElement = rootElement;
	}

	/**
	 * getter for root element
	 * 
	 * @return DataTreeNode rootElement
	 */
	public DataTreeNode getRootElement() {
		return rootElement;
	}

}