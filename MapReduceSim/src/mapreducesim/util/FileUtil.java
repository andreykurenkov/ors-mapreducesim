package mapreducesim.util;

import java.io.File;
import java.net.URL;

public class FileUtil {
	private final static String PATH_PREFIX = ".." + File.separator + ".." + File.separator;

	public static File getProjectFile(String folder, String file) {
		String path = folder.replace("[\\|/]", "") + File.separator + file;
		return getProjectFile(path);
	}

	public static File getProjectFile(String path) {
		URL fileUrl = DataCollectionUtil.class.getResource(PATH_PREFIX + path);
		if (fileUrl != null) {
			return new File(fileUrl.getPath().replace("%20", " "));
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
}
