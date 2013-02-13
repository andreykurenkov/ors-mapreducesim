package mapreducesim.storage;

import java.util.List;

public class DataNode<DataBlock> {
	public DataBlock data;
	public List<DataNode<DataBlock>> children;
	
	public DataNode() {
		super();
	}
	public DataNode(DataBlock dataIn) {
		this.data = dataIn;
	}
	
	public void setChildren(List<DataNode<DataBlock>> childrenIn) {
		this.children = childrenIn;
	}
	//http://sujitpal.blogspot.com/2006/05/java-data-structure-generic-tree.html
	
}