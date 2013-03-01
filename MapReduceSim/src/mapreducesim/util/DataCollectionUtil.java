package mapreducesim.util;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.ImageIcon;

import org.simgrid.msg.Msg;

import java.io.Console;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import mapreducesim.core.SimMain;
import mapreducesim.util.xml.XMLDocument;
import mapreducesim.util.xml.XMLElement;
import mapreducesim.util.xml.XMLNode;

public class DataCollectionUtil {
	private SmartFile outputFile;
	private int simulationCount;

	public static void main(String[] args) throws IOException {
		DataCollectionUtil util = new DataCollectionUtil(new File("data.csv"));
		File platFile = FileUtil.getProjectFile("plat.xml");
		File deplFile = FileUtil.getProjectFile("depl.xml");
		File configFile = FileUtil.getProjectFile("config.xml");
		File outFile = new File("out.txt");
		util.runSimulation(platFile, deplFile, configFile, outFile);
		util.runSimulation(platFile, deplFile, configFile, outFile);

	}

	public DataCollectionUtil(File outputFile, String... customCSVCategories) throws IOException {
		if (outputFile == null)
			throw new NullPointerException("Output file cannot be null for data collector.");
		if (outputFile.exists()) {
			outputFile.delete();
			outputFile.createNewFile();
		} else
			outputFile.createNewFile();
		this.outputFile = new SmartFile(outputFile);
		String firstLine = "";
		for (String str : customCSVCategories)
			firstLine += str + ",";
		this.outputFile.write(firstLine + "runtime\n", true);
		simulationCount = 0;
	}

	public DataCollectionUtil(File outputFile) throws IOException {
		this(outputFile, "test number", "platform file", "deployment file", "configuration file");
	}

	public void runSimulation(XMLDocument platform, XMLDocument deployment, XMLDocument configuration, File logOut) {
		try {
			File directory = new File("");
			XMLDocument[] documents = { platform, deployment, configuration };
			String[] fileNames = { "tempPlatform.xml", "tempDeployment.xml", "tempConfiguration.xml" };
			SmartFile[] tempFiles = new SmartFile[3];
			String[] simArgs = new String[3];
			for (int i = 0; i < 3; i++) {
				tempFiles[i] = new SmartFile(directory, fileNames[i]);
				tempFiles[i].createNewFile();// TODO: use boolean?
				String content = documents[i].toRawXML(XMLNode.PRETTYFORMAT);
				tempFiles[i].write(content, false);
				simArgs[i] = tempFiles[i].getAbsolutePath();
			}

			runSimulation(tempFiles[0], tempFiles[1], tempFiles[2], logOut);

			for (int i = 0; i < 3; i++) {
				tempFiles[i].delete();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void runSimulation(File platform, File deployment, File configuration, File logOut) {
		runSimulation(platform, deployment, configuration, logOut, (String[]) null);
	}

	public void runSimulation(File platform, File deployment, File configuration, File logOut, String... csvProperties) {
		try {
			simulationCount++;
			String[] simArgs = { platform.getAbsolutePath(), deployment.getAbsolutePath(), configuration.getAbsolutePath() };
			ProcessBuilder builder = new ProcessBuilder("java", "-cp", ".:./java/simgrid.jar", "mapreducesim/core/SimMain",
					simArgs[0], simArgs[1], simArgs[2]);
			builder.directory(new File("./bin"));
			if (logOut != null) {
				builder.redirectOutput(logOut);
				builder.redirectError(logOut);
			}
			Process process = builder.start();
			SimMain.main(simArgs);
			double runtime = Msg.getClock();
			Msg.clean();
			/*
			 * Note: much faster to execute locally than to read file (at low level of simulation) boolean done = false;
			 * TODO: consider launching many Processes in parallel and then reading results. while (!done) { try {
			 * process.exitValue(); done = true; } catch (Exception e) { done = false; } } String lastLine = null; Scanner
			 * scan = new Scanner(logOut); while (scan.hasNextLine()) lastLine = scan.nextLine(); scan.close();
			 * System.out.println(lastLine); if (tempFile != null) tempFile.delete(); String runtime =
			 * "Error;runtime could not be found"; if (lastLine != null && lastLine.contains("Simulation time:")) runtime =
			 * lastLine.substring(lastLine.indexOf(":") + 1);
			 */
			if (csvProperties != null) {
				StringBuilder allValues = new StringBuilder();
				for (String str : csvProperties)
					allValues.append(str + ",");
				String out = allValues.toString() + runtime + "\n";
				outputFile.write(out, true);
			} else {
				StringBuilder allValues = new StringBuilder(simulationCount + ",");
				allValues.append(platform.getName() + ",");
				allValues.append(deployment.getName() + ",");
				allValues.append(configuration.getName() + ",");
				allValues.append(runtime + "\n");
				outputFile.write(allValues.toString(), true);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static XMLDocument[] getPermutations(XMLDocument doc, XMLElement toModify, String[] values) {
		String startValue = toModify.getContentText();
		XMLDocument[] docs = new XMLDocument[values.length];
		for (int i = 0; i < values.length; i++) {
			toModify.setContentText(values[i]);
			docs[i] = doc.deepCopy();
		}
		toModify.setContentText(startValue);
		return docs;
	}

	/**
	 * Gets a range of equally spaced int values in String format.
	 * 
	 * @param start
	 * @param end
	 * @param add
	 * @return
	 */
	public static String[] getValueRange(double start, double end, double add) {
		int addToLength = Math.abs(end - start) % add == 0 ? 1 : 0;
		String[] toReturn = new String[(int) Math.round(Math.abs((end - start) / add)) + addToLength];
		int counter = 0;
		for (double val = start; val >= start && val <= end; val += add) {
			toReturn[counter++] = String.valueOf(val);
		}
		return toReturn;
	}
}
