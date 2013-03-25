package mapreducesim.storage;

import java.util.ArrayList;

/**
 * The basic unit of a data tree. A tree can be represented by a DataTree
 * object, which basically just points to the root Node, or on its own by its
 * root node. The Node has a name and has an array of children and a single
 * parent defined by the constructor. To hold data it must be extended (e.g.,
 * File/Directory)
 * 
 * Was DataTreeNode.
 * 
 * @author Matthew O'Shaughnessy
 */

// TODO Exception handling

public class Node {
	private String name;
	private ArrayList<Node> children;
	private Node parent;
	private boolean isRoot;
	protected boolean isFile;
	private int level;

	/**
	 * No-args constructor creates the root
	 */
	public Node() {
		super();
		isRoot = true;
		isFile = false;
		this.level = 0;
		name = "/";
	}

	public String toString() {
		return this.name + "(" + this.children + ")";
	}

	/**
	 * Creates a node as a child of specified node with specified name
	 * 
	 * @param parent
	 *            if parent is null then treated as root
	 * @param name
	 */
	public Node(Node parent, String name) {
		super();
		this.parent = parent;
		this.name = name;

		if (parent != null) {
			this.level = parent.getLevel() + 1;
			isRoot = false;
		} else {
			level = 0;
			isRoot = true;
		}
	}

	/**
	 * Replaces all current children with a new array of children.
	 * 
	 * @param children
	 */
	public void setChildren(ArrayList<Node> children) {
		this.children = children;
	}

	/**
	 * Appends a child to the node.
	 * 
	 * @param child
	 */
	public void addChild(Node child) {
		if (this.children == null) {
			this.children = new ArrayList<Node>();
		}
		children.add(child);
	}

	/**
	 * Adds a child to the node at specified position
	 * 
	 * @param child
	 * @param position
	 */
	public void addChildAtPosition(Node child, int position) {
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
	public Node getChildAtPosition(int position) {
		return children.get(position);
	}

	/**
	 * Returns an ArrayList of all children
	 * 
	 * @return ArrayList<DataTreeNode>
	 */
	public ArrayList<Node> getChildren() {
		if (this.children == null) {
			return new ArrayList<Node>();
		}
		return this.children;
	}

	/**
	 * Sets the parent of this node
	 * 
	 * @param parent
	 */
	public void setParent(Node parent) {
		this.parent = parent;
	}

	/**
	 * Returns the parent of this node
	 * 
	 * @return DataTreeNode
	 */
	public Node getParent() {
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
	public int distanceBetween(Node node1, Node node2) {
		Node n1, n2;
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

	public boolean directlyContains(Node query) {
		for (int i = 0; i < this.children.size(); i++) {
			// TODO
		}
		return false;
	}

	/**
	 * Recursively checks itself and children and returns if this DataTreeNode
	 * is a parent of the query DataTreeNode
	 * 
	 * @param query
	 * @return Is the query a child of this node?
	 */
	public boolean contains(Node query) {
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

	public int getSpeed() {
		if (this instanceof DataNode) {
			return ((DataNode) this).getSpeed();
		} else if (this instanceof Rack) {
			return ((Rack) this).getSpeed();
		} else
			return 0; // TODO: Error (not topology object)
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

	/**
	 * Allows the user to retrieve a child node with the specified name. Returns
	 * null if no such child node exists.
	 * 
	 * @param name
	 * @return
	 */
	public Node getChild(String query) {
		for (int i = 0; i < this.numChildren(); i++) {
			if (this.getChildAtPosition(i).getName().equals(query)) {
				return this.getChildAtPosition(i);
			}
		}
		return null;
	}

	public Node getChildRecursive(String query) {
		// TODO
		return null;
	}

	/**
	 * For debugging purposes. Prints the names of the children to the console.
	 */
	public void printContents() {
		for (Node child : getChildren()) {
			System.out.println(child.name);
		}
	}

	public boolean isFile() {
		return false;
	}
}
