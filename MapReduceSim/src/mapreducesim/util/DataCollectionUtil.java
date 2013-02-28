package mapreducesim.util;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.Arrays;

import javax.swing.ImageIcon;

import java.io.File;
import mapreducesim.core.SimMain;
import mapreducesim.util.xml.XMLDocument;
import mapreducesim.util.xml.XMLElement;

public class DataCollectionUtil {
	private File baseConfig, basePlatform, baseDeployment;
	private int simulationCount;

	public static void main(String[] args) {
		String folder = "executiontest";
		String[] fileNames = { "plat.xml", "depl.xml", "config.xml" };
		DataCollectionUtil test = new DataCollectionUtil(folder, fileNames);
		test.runSimulations(null, null, null);
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
			SimMain.main(new String[] { basePlatform.getAbsolutePath(), baseDeployment.getAbsolutePath(),
					baseConfig.getAbsolutePath() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
