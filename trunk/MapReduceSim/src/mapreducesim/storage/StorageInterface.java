package mapreducesim.storage;

/**
 * 
 * An easy way of accessing methods for interfacing with storage.
 * 
 * @author Matthew O'Shaughnessy
 * 
 */
public class StorageInterface {
	private DataTree fs;
	private DataTree top;

	/**
	 * Constructor creates the fs/topology and loads it into StorageInterface
	 * private fields
	 */
	public StorageInterface() {
		FSBuilder fsbuild = new FSBuilder();
		fsbuild.createTestTopology();
		this.fs = fsbuild.getFS();
		this.top = fsbuild.getTopology();
	}

	/**
	 * Very generic search to return any kind of created storage node--File,
	 * FileBlock, Rack, DataNode, etc. If none is found returns null. Caution:
	 * if more than one object with the same name field exists, only the last
	 * one of them found is returned!
	 * 
	 * @return
	 */
	public Node get(String query) {
		Node result;
		result = fs.get(query);
		result = top.get(query);
		return result;
	}

	// I can add other methods you might need here so there's a unified/simple
	// interface without having to cut through the clutter of all the other
	// storage stuff. Just let me know what methods you need for interfacing and
	// I can link to them here or create them as needed.

}
