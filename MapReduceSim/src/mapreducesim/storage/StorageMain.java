package mapreducesim.storage;

/**
 * For testing.
 * 
 * @author Matthew O'Shaughnessy
 * 
 */
public class StorageMain {

	private static DataTree<Node> fs;
	private static DataTree<Node> top;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		FSBuilder fsbuild = new FSBuilder();
		fsbuild.createTestTopology();
		fs = fsbuild.getFS();
		top = fsbuild.getTopology();

	}

	public static DataTree<Node> getFS() {
		return fs;
	}

	public static DataTree<Node> getTopology() {
		return top;
	}

}
