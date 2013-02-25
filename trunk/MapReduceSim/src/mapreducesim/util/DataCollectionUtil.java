package mapreducesim.util;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.Arrays;

import javax.swing.ImageIcon;

import java.io.File;
import mapreducesim.core.MapReduceSimMain;
import mapreducesim.util.xml.XMLDocument;
import mapreducesim.util.xml.XMLElement;

public class DataCollectionUtil {
	private File baseConfig, basePlatform, baseDeployment;

	public static void main(String[] args) {
		String folder = "executiontest";
		String[] fileNames = { "plat.xml", "depl.xml", "config.xml" };
		DataCollectionUtil test = new DataCollectionUtil(folder, fileNames);
		test.runSimulations(null, null, null);
	}

	/**
	 * Adds the specified path to the java library path
	 * 
	 * @param pathToAdd
	 *            the path to add
	 * @throws Exception
	 */
	public static void addLibraryPath(String pathToAdd) throws Exception {
		final Field usrPathsField = ClassLoader.class.getDeclaredField("usr_paths");
		usrPathsField.setAccessible(true);

		// get array of paths
		final String[] paths = (String[]) usrPathsField.get(null);

		// check if the path to add is already present
		for (String path : paths) {
			if (path.equals(pathToAdd)) {
				return;
			}
		}

		// add the new path
		final String[] newPaths = Arrays.copyOf(paths, paths.length + 1);
		newPaths[newPaths.length - 1] = pathToAdd;
		usrPathsField.set(null, newPaths);
	}

	public DataCollectionUtil(String folder, String[] xmlFiles) {
		this.basePlatform = FileUtil.getProjectFile(folder, xmlFiles[0]);
		this.baseDeployment = FileUtil.getProjectFile(folder, xmlFiles[1]);
		this.baseConfig = FileUtil.getProjectFile(folder, xmlFiles[2]);
		System.out.println(basePlatform);
		System.out.println(baseConfig);
		System.out.println(baseDeployment);
	}

	public DataCollectionUtil(File basePlatform, File baseDeployment, File baseConfig) {
		this.basePlatform = basePlatform;
		this.baseDeployment = baseDeployment;
		this.baseConfig = baseConfig;
	}

	public void runSimulations(XMLElement[] toModify, String[][] modifiedValues, File outputDir) {
		try {
			MapReduceSimMain.main(new String[] { basePlatform.getAbsolutePath(), baseDeployment.getAbsolutePath(),
					baseConfig.getAbsolutePath() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
