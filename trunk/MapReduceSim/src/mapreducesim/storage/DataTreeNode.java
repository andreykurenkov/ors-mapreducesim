package mapreducesim.storage;

import java.util.ArrayList;

/**
 * The basic unit of a data tree. A tree can be represented by a DataTree
 * object, which basically just points to the root DataTreeNode, or on its own
 * by its root node. The DataTreeNode has a name and has an array of children
 * and a single parent defined by the constructor. To hold data it must be
 * extended (e.g., File/Directory)
 * 
 * @author Matthew O'Shaughnessy
 */

// TODO Exception handling

public class DataTreeNode {
	private String name;
	private ArrayList<DataTreeNode> children;
	private DataTreeNode parent;
	private Boolean isRoot;
	private int level; //

	/**
	 * No-args constructor creates the root
	 */
	public DataTreeNode() {
		super();
		isRoot = true;
		this.level = 0;
		name = "/";
	}

	/**
	 * Creates a node as a child of specified node with specified name
	 * 
	 * @param parent
	 * @param name
	 */
	public DataTreeNode(DataTreeNode parent, String name) {
		super();
		this.parent = parent;
		this.name = name;
		isRoot = false;
		this.level = parent.getLevel() + 1;
	}

	/**
	 * Replaces all current children with a new array of children.
	 * 
	 * @param children
	 */
	public void setChildren(ArrayList<DataTreeNode> children) {
		this.children = children;
	}

	/**
	 * Appends a child to the node.
	 * 
	 * @param child
	 */
	public void addChild(DataTreeNode child) {
		if (this.children == null) {
			this.children = new ArrayList<DataTreeNode>();
		}
		children.add(child);
	}

	/**
	 * Adds a child to the node at specified position
	 * 
	 * @param child
	 * @param position
	 */
	public void addChildAtPosition(DataTreeNode child, int position) {
		children.add(position, child);
	}

	/**
	 * Removes the child at the specified position
	 * 
	 * @param childNumberToRemove
	 */
	public void removeChildNumber(int childNumberToRemove) {
		children.remove(childNumberToRemove);
	}

	/**
	 * Returns the child at the specified position
	 * 
	 * @param position
	 * @return DataTreeNode
	 */
	public DataTreeNode getChildAtPosition(int position) {
		return children.get(position);
	}

	/**
	 * Returns an ArrayList of all children
	 * 
	 * @return ArrayList<DataTreeNode>
	 */
	public ArrayList<DataTreeNode> getChildren() {
		if (this.children == null) {
			return new ArrayList<DataTreeNode>();
		}
		return this.children;
	}

	/**
	 * Sets the parent of this node
	 * 
	 * @param parent
	 */
	public void setParent(DataTreeNode parent) {
		this.parent = parent;
	}

	/**
	 * Returns the parent of this node
	 * 
	 * @return DataTreeNode
	 */
	public DataTreeNode getParent() {
		if (this.isRoot) {
			return null;
		} else {
			return parent;
		}
	}

	/**
	 * Calculates the distance between two nodes in steps.
	 * 
	 * @param node1
	 * @param node2
	 * @return an integer with the number of steps between node1 and node2
	 */
	public int distanceBetween(DataTreeNode node1, DataTreeNode node2) {
		DataTreeNode n1, n2;
		int l1, l2;
		int distance = 0;
		n1 = node1;
		n2 = node2;
		l1 = node1.getLevel();
		l2 = node2.getLevel();

		// if node1 is deeper than node2,
		// climb up the branch of node1 until it's at the same level of node2
		while (l1 > l2 && l1 != 0) {
			n1 = n1.getParent();
			--l1;
			++distance;
		}

		// if node2 is deeper than node2,
		// climb up the branch of node2 until it's at the same level of node1
		while (l2 > l1 && l2 != 0) {
			n2 = n2.getParent();
			--l2;
			++distance;
		}

		// now that node1 and node2 are on the same level (depth),
		// climb both up the branch until they have a common parent
		while (l1 != 0 && l2 != 0 && n1.getParent() != n2.getParent()) {
			n1 = n1.getParent();
			n2 = n2.getParent();
			distance += 2;
		}

		// account for the final step up after the parents match
		distance += 2;

		return distance;
	}

	/**
	 * Recursively checks itself and children and returns if this DataTreeNode
	 * is a parent of the query DataTreeNode
	 * 
	 * @param query
	 * @return Is the query a child of this node?
	 */
	public boolean contains(DataTreeNode query) {
		// checks itself
		if (this.equals(query)) {
			return true;
		}
		// recursive through the children
		for (int i = 0; i < this.numChildren(); i++) {
			if (this.getChildAtPosition(i).contains(query)) {
				return true;
			}
		}
		// if query not found
		return false;
	}

	/**
	 * Compares using a name
	 * 
	 * @param query
	 * @return
	 */
	public boolean contains(String query) {
		// checks itself
		if (this.getName() == query) {
			return true;
		}
		// recursive through the children
		for (int i = 0; i < this.numChildren(); i++) {
			if (this.getChildAtPosition(i).contains(query)) {
				return true;
			}
		}
		// if query not found
		return false;
	}

	/**
	 * Makes this node the root. Changes the name to "/"
	 */
	public void makeRoot() {
		this.isRoot = true;
		this.setName(name);
	}

	/**
	 * Changes this node from a root node to a normal node. Alters the name to
	 * the specified new name
	 * 
	 * @param name
	 */
	public void unmakeRoot(String name) {
		this.isRoot = false;
		this.setName(name);
	}

	/**
	 * Returns true if this node is the root
	 * 
	 * @return Boolean
	 */
	public Boolean isRoot() {
		return isRoot();
	}

	/**
	 * Sets the name of this node
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the name of this node
	 * 
	 * @return String
	 */
	public String getName() {
		return name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * Returns the number of children the node has
	 * 
	 * @return int
	 */
	public int numChildren() {
		if (this.children == null) {
			return 0;
		}
		return children.size();
	}

	/**
	 * Recursively creates a string of the path to this node. Ends when it finds
	 * the root
	 * 
	 * @return String
	 */
	public String getPath() {
		if (this.isRoot)
			return "/";
		else {
			return (this.getParent().getPath() + name + "/");
		}
	}

}
