package mapreducesim.storage;

import java.util.ArrayList;
import java.util.List;

public class FSBuilder {
	private DataTree<Node> fs;
	private DataTree<Node> topology;

	public FSBuilder() {
		fs = new DataTree<Node>();
		topology = new DataTree<Node>();
	}

	/**
	 * Creates necessary directories and files to be able to execute the path.
	 * For files, size can be specified in parentheses.
	 * 
	 * e.g., '/dir1/file1(500)': creates file with name "file1" in the (created
	 * if necessary) directory in root with name "dir1" with size 500.
	 * 
	 * @param path
	 *            the path to create. See format example above.
	 */
	public void create(String path) {
		String current = "";
		String remaining = path.trim();
		List<String> tokenized = new ArrayList<String>(2);
		Node parent = null;
		int size = 0;

		parent = makeTree(); // create the tree and root

		while (remaining != "") {
			tokenized = tokenize(remaining, '/');
			current = tokenized.get(0);
			remaining = tokenized.get(1);
			if (remaining == "") { // this is the file, not a directory
				size = extractSize(current);
				// remove the size data
				current = current.substring(0, current.indexOf('('));
				@SuppressWarnings("unused")
				File generatedFile = new File(parent, current, size);
				// TODO
			} else { // this is a directory
				@SuppressWarnings("unused")
				Directory generatedDir = new Directory(parent, current);
			}
		}
	}

	/**
	 * Creates the DataTree container object and the root node in it. Returns
	 * the root node.
	 */
	public Directory makeTree() {
		// make the DataTree
		DataTree<Node> fs = new DataTree<Node>();
		// add the root node ("/")
		Directory fsRoot = new Directory();
		fs.setRoot(fsRoot);
		return (Directory) fs.getRoot();
	}

	/**
	 * Tokenizes the input string with the input delimiter.
	 * 
	 * @param path
	 * @param delimiter
	 * @return a List of two values: [0]-the string before the delimiter;
	 *         [1]-the string after the delimiter. The delimiter is eliminated.
	 */
	public List<String> tokenize(String path, char delimiter) {
		ArrayList<String> ret;
		int i = path.indexOf(delimiter);
		String before = path.substring(0, i);
		String after = path.substring(i + 1);
		ret = new ArrayList<String>(2);
		ret.add(before);
		ret.add(after);
		return ret;
	}

	public int extractSize(String str) {
		int i = str.indexOf('(');
		int extractedSize = Integer
				.parseInt(str.substring(i + 1, str.length()));
		return extractedSize;
	}

	/**
	 * Creates a test filesystem and topology. Diagram for the test topology
	 * this method implements: http://dl.dropbox.com/u/1267479/testtop.jpg
	 */
	public void createTestTopology() {
		// TODO Cleanup

		// 1. Create the tree for the filesystem
		fs.setRoot(new Directory());
		fs.getRoot().addChild(new Directory(fs.getRoot(), "dir1"));
		fs.getRoot().addChild(new Directory(fs.getRoot(), "dir2"));
		Directory dir = (Directory) fs.getRoot().getChild("dir1");
		dir.addChild(new File(dir, "file1", 200));
		dir.addChild(new File(dir, "file2", 80));
		dir = (Directory) fs.getRoot().getChild("dir2");
		dir.addChild(new File(dir, "file3", 256));
		dir.addChild(new File(dir, "file4", 270));

		// 2. Create the network topology.
		topology.setRoot(new Root());
		topology.getRoot().addChild(
				new Rack((Root) topology.getRoot(), "rack1"));
		topology.getRoot().addChild(
				new Rack((Root) topology.getRoot(), "rack2"));
		topology.getRoot().addChild(
				new Rack((Root) topology.getRoot(), "rack3"));
		Rack rack = (Rack) topology.getRoot().getChild("rack1");
		rack.addChild(new DataNode(rack, "dn1a"));
		rack.addChild(new DataNode(rack, "dn1b"));
		rack = (Rack) topology.getRoot().getChild("rack2");
		rack.addChild(new DataNode(rack, "dn2a"));
		rack.addChild(new DataNode(rack, "dn2b"));
		rack = (Rack) topology.getRoot().getChild("rack3");
		rack.addChild(new DataNode(rack, "dn3a"));
		rack.addChild(new DataNode(rack, "dn3b"));

		// 3. Creating the connection between fs and top: add splits to topology
		DataNode dn1a = (DataNode) topology.getRoot().getChild("rack1")
				.getChild("dn1a");
		DataNode dn1b = (DataNode) topology.getRoot().getChild("rack1")
				.getChild("dn1b");
		DataNode dn2a = (DataNode) topology.getRoot().getChild("rack1")
				.getChild("dn1a");
		DataNode dn2b = (DataNode) topology.getRoot().getChild("rack1")
				.getChild("dn1b");
		DataNode dn3a = (DataNode) topology.getRoot().getChild("rack1")
				.getChild("dn1a");
		DataNode dn3b = (DataNode) topology.getRoot().getChild("rack1")
				.getChild("dn1b");
		File file1 = (File) fs.getRoot().getChild("dir1").getChild("file1");
		File file2 = (File) fs.getRoot().getChild("dir1").getChild("file2");
		File file3 = (File) fs.getRoot().getChild("dir2").getChild("file3");
		File file4 = (File) fs.getRoot().getChild("dir2").getChild("file4");
		// f1[0..99]:
		ArrayList<DataNode> locations = new ArrayList<DataNode>(3);
		locations.add(dn1a);
		locations.add(dn1b);
		locations.add(dn2b);
		file1.getBlocks().get(0).setLocations(locations);
		// f1[100..199]:
		locations.clear();
		locations.add(dn1a);
		locations.add(dn1b);
		locations.add(dn3a);
		file1.getBlocks().get(1).setLocations(locations);
		// f2[0..79]:
		locations.clear();
		locations.add(dn1b);
		locations.add(dn2a);
		locations.add(dn2b);
		file2.getBlocks().get(0).setLocations(locations);
		// f3[0..127]:
		locations.clear();
		locations.add(dn2a);
		locations.add(dn3a);
		locations.add(dn3b);
		file3.getBlocks().get(0).setLocations(locations);
		// f3[128..255]:
		locations.clear();
		locations.add(dn1a);
		locations.add(dn1b);
		locations.add(dn3a);
		file3.getBlocks().get(1).setLocations(locations);
		// f4[0..89]:
		locations.clear();
		locations.add(dn1a);
		locations.add(dn3a);
		locations.add(dn3b);
		file4.getBlocks().get(0).setLocations(locations);
		// f4[90..179]:
		locations.clear();
		locations.add(dn2a);
		locations.add(dn3a);
		locations.add(dn3b);
		file4.getBlocks().get(1).setLocations(locations);
		// f4[180..269]:
		locations.clear();
		locations.add(dn1b);
		locations.add(dn2a);
		locations.add(dn2b);
		file4.getBlocks().get(2).setLocations(locations);
	}

	public DataTree<Node> getFS() {
		return fs;
	}

	public DataTree<Node> getTopology() {
		return topology;
	}
}
