package mapreducesim.storage;

import java.util.ArrayList;
import java.util.List;

public class FSBuilder {

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
	// TODO: Test....
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
				// TODO: object name? Does it matter?
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
}
