package mapreducesim.storage;

/**
 * Represents the network location of a FileBlock. This data is used to
 * calculate the cost of a read/write operation. Immediately, this data only
 * includes a rack and dataNode IDs as strings that can be used to calculate
 * somewhat accurate costs. To read a file, the requester does not need to
 * retrieve all splits of a file--it can just access the splits it needs. For
 * now there are two "levels" of location: dataNode and rack. Rack is
 * higher-level. Hosts act as the data nodes so ultimately the location will be
 * based on these.
 */
public class FileBlockLocation {
	private int rack;
	private int dataNode;

	public FileBlockLocation(int rack, int dataNode) {
		this.setRack(rack);
		this.setDataNode(dataNode);
	}

	/**
	 * 
	 * @return an identifier of the FileBlock's rack. For now an int.
	 */
	public int getRack() {
		return rack;
	}

	public void setRack(int rack) {
		this.rack = rack;
	}

	/**
	 * 
	 * @return an identifier of the FileBlock's dataNode on the rack.
	 */
	public int getDataNode() {
		return dataNode;
	}

	public void setDataNode(int dataNode) {
		this.dataNode = dataNode;
	}
}
