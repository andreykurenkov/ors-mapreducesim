package mapreducesim.storage;

/**
 * For testing.
 * 
 * @author Matthew O'Shaughnessy
 * 
 */
public class StorageMain {

	private static DataTree fs;
	private static DataTree top;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		FSBuilder fsbuild = new FSBuilder();
		fsbuild.createTopology();
		fs = fsbuild.getFS();
		top = fsbuild.getTopology();

	}

	public static DataTree getFS() {
		return fs;
	}

	public static DataTree getTopology() {
		return top;
	}

}
