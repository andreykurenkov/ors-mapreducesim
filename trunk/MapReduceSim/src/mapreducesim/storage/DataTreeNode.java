package mapreducesim.storage;

import java.util.ArrayList;

public class DataTreeNode {
	private String name;
	private ArrayList<DataTreeNode> children;
	private DataTreeNode parent;
	private Boolean isRoot;
	
	//basic constructors
	public DataTreeNode() {
		super();
		isRoot = true;
		name = "/";
	}
	public DataTreeNode(DataTreeNode parent, String name) {
		super();
		this.parent = parent;
		this.name = name;
		isRoot = false;
	}
	
	//methods for adding and removing children from this node.
	public void setChildren(ArrayList<DataTreeNode> children) {
		this.children = children;
	}
	public void addChild(DataTreeNode child) {
		if (this.children == null) {
			this.children = new ArrayList<DataTreeNode>();
		}
		children.add(child);
	}	
	public void addChildAtPosition(DataTreeNode child, int position) {
		children.add(position, child);
	}
	public void removeChildNumber(int childNumberToRemove) {
		children.remove(childNumberToRemove);
	}
	public DataTreeNode getChildAtPosition(int position) {
		return children.get(position);
	}
	public ArrayList<DataTreeNode> getChildren() {
		if (this.children == null) {
			return new ArrayList<DataTreeNode>();
		}
		return this.children;
	}
	public void setParent(DataTreeNode parent) {
		this.parent = parent;
	}
	public DataTreeNode getParent() {
		return parent;
	}
	public void makeRoot() {
		this.isRoot = true;
	}
	public void unmakeRoot() {
		this.isRoot = false;
	}
	public Boolean isRoot() {
		return isRoot();
	}
	public String getName() {
		return name;
	}
	public int numChildren() {
		return children.size();
	}
	public String getPath() {
		if (this.isRoot) return "/";
		else {
			return (this.getParent().getPath() + name + "/");
		}
	}
	public void ls() {
		if (children.size()!=0) {		
			System.out.println("Contents of directory '" + name + "' (" + this.numChildren() + " items):");
			for (int i=0; i<children.size(); i++) {
				System.out.println("\t" + this.getChildAtPosition(i).getName());
			}
		}
	}
	
}
