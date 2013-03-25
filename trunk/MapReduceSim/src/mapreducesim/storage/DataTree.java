package mapreducesim.storage;

import java.util.ArrayList;
import java.util.List;

import org.simgrid.msg.Msg;

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

	// TODO javadoc
	public List<Node> linearize() {
		List<Node> linearizedTree = new ArrayList<Node>();
		recursiveLinearize(getRoot(), linearizedTree);
		return linearizedTree;
	}

	public String toString() {
		return "DataTree(" + getRoot() + ")";
	}

	public void recursiveLinearize(Node recurseRoot, List<Node> linearizedTree) {
		linearizedTree.add(recurseRoot);
		for (Node child : recurseRoot.getChildren()) {
			recursiveLinearize(child, linearizedTree);
		}
	}

	/**
	 * Returns the first instance of a Node in the tree with the query name.
	 * Null if no such Node found.
	 * 
	 * @param query
	 * @return
	 */
	public Node get(String query) {
		List<Node> linearTree = linearize();
		// Msg.info("Linear tree: " + linearTree);
		for (Node potentialTarget : linearTree) {
			if (potentialTarget.getName().equals(query)) {
				return potentialTarget;
			}
		}
		return null;
	}
}