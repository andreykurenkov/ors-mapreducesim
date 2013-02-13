package mapreducesim.storage;

import java.util.List;

public class NameNode<DataBlock> {
	private String nameNodeData; //TODO what should this be?
	private List<DataNode<DataBlock>> children;
	
	public NameNode() {
		super();
	}
	
	public NameNode(String nameNodeDataIn) {
		this.nameNodeData = nameNodeDataIn;
	}
	public NameNode(String nameNodeDataIn, List<DataNode<DataBlock>> childrenIn) {
		this.nameNodeData = nameNodeDataIn;
		this.children = childrenIn;
	}
	
	/*public List<DataNode> toList() {
	}*/
	
	public String toSting() {
		return "NameNode"; //TODO
	}

	
	
}
