package mapreducesim.storage;

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