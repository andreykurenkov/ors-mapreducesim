package mapreducesim.util;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
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

/**
 * This class holds several static and concrete methods that make it easier to run series of simulations with slightly varied
 * configurations and collect data for all simulations. It provides the ability to store the logs of simulations to file and
 * also to create an agreggate CSV holding results from multiple simulations.
 * 
 * @author Andrey Kurenkov
 * @version 1.0 Mar 1, 2013
 */
public class SimulationsRunner {
	private SmartFile outputFile;// csv values written to this file
	private int simulationCount;// how many simulation were ran with this object
	private ArrayList<Double> runtimes;// stores record of past simulation runs, up to simulationCount

	/**
	 * Test main method that runs two simulations
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		SimulationsRunner util = new SimulationsRunner(new File("data.csv"));
		File platFile = FileUtil.getProjectFile("plat.xml");
		File deplFile = FileUtil.getProjectFile("depl.xml");
		File configFile = FileUtil.getProjectFile("config.xml");
		File outFile = new File("out.txt");
		util.runSimulation(platFile, deplFile, configFile, outFile);
		util.runSimulation(platFile, deplFile, configFile, outFile);

	}

	/**
	 * Constructor that sets up the file to write the CSV output to and takes in the categories to be used written on the
	 * first line of that file. It is important to note that the runtime of a simulation is always recorded and used as the
	 * last field.
	 * 
	 * @param outputFile
	 *            the file CSV-formatted results should be output to
	 * @param customCSVCategories
	 *            the set of categories for the CSV file - any strings containing no commas and not being runtime
	 * @throws IOException
	 *             if the outputfile is null, this exception is thrown
	 */
	public SimulationsRunner(File outputFile, String... customCSVCategories) throws IOException {
		if (outputFile == null)
			throw new NullPointerException("Output file cannot be null for data collector.");
		if (outputFile.exists()) {
			outputFile.delete();
			outputFile.createNewFile();
		} else
			outputFile.createNewFile();
		this.outputFile = new SmartFile(outputFile);
		String firstLine = "";
		runtimes = new ArrayList<Double>();
		for (String str : customCSVCategories)
			firstLine += str + ",";
		this.outputFile.write(firstLine + "runtime\n", true);
		simulationCount = 0;
	}

	/**
	 * Uses "test number", "platform file", "deployment file", "configuration file" as the default CSV categories.
	 * 
	 * @param outputFile
	 *            the file CSV-formatted results should be output to
	 * @throws IOException
	 *             if the outputfile is null, this exception is thrown
	 */
	public SimulationsRunner(File outputFile) throws IOException {
		this(outputFile, "test number", "platform file", "deployment file", "configuration file");
	}

	/**
	 * Given XML documents, creates temporary files with their content and runs a simulation, the log of which is written to
	 * logOut. The runSimulation that uses Files is called from this method once temporary files are created.At the end of
	 * the simulation, a row is added to this instance's set output file in CVS format.
	 * 
	 * @param platform
	 *            the SimGrid platform XML document
	 * @param deployment
	 *            the SimGrid deployment XML document
	 * @param configuration
	 *            the native Simulator configuration XML document
	 * @param logOut
	 *            file to write log to. Permitted to be null (in which case log will not be stored).
	 * @param csvValues
	 *            The values of the CSV for this simulation (not including runtime, which will appended as the last element
	 *            of the row)
	 * @return double which is the runtime of the simulation or -1 if problem was encountered
	 */
	public double runSimulation(XMLDocument platform, XMLDocument deployment, XMLDocument configuration, File logOut,
			String... csvValues) {
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

			double time = runSimulation(tempFiles[0], tempFiles[1], tempFiles[2], logOut, csvValues);

			for (int i = 0; i < 3; i++) {
				tempFiles[i].delete();
			}
			return time;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/***
	 * Given XML documents, creates temporary files with their content and runs a simulation, the log of which is written to
	 * logOut. The runSimulation that uses Files is called from this method once temporary files are created.At the end of
	 * the simulation, a row is added to this instance's set output file in CVS format.
	 * 
	 * This method passes null for cvsValues, which leads to the default CVS values ("test number", "platform file",
	 * "deployment file", "configuration file") being recorded.
	 * 
	 * @param platform
	 *            the SimGrid platform XML document
	 * @param deployment
	 *            the SimGrid deployment XML document
	 * @param configuration
	 *            the native Simulator configuration XML document
	 * @param logOut
	 *            file to write log to. Permitted to be null (in which case log will not be stored).
	 * @return double which is the runtime of the simulation or -1 if problem was encountered
	 */
	public double runSimulation(XMLDocument platform, XMLDocument deployment, XMLDocument configuration, File logOut) {
		return runSimulation(platform, deployment, configuration, logOut, (String[]) null);
	}

	/**
	 * Given Files holding valid XML values, begins a new Process which runs SimMain, the out and errors streams of which
	 * (including the simgrid log) are redirected to logOut. A simulation is also run locally to retrieve the runtime of the
	 * simulation, by simply calling SimMain.main, an. At the end of the simulation, a row is added to this instance's set
	 * output file in CVS format.
	 * 
	 * @todo: larger simulations may require more efficient code with parallel processes and no local simulation
	 * 
	 * @param platform
	 *            the SimGrid platform XML file
	 * @param deployment
	 *            the SimGrid deployment XML file
	 * @param configuration
	 *            the native Simulator configuration XML file
	 * @param logOut
	 *            file to write log to. Permitted to be null (in which case log will not be stored).
	 * @param csvValues
	 *            The values of the CSV for this simulation (not including runtime, which will appended as the last element
	 *            of the row)
	 * @return double which is the runtime of the simulation or -1 if problem was encountered
	 */
	public double runSimulation(File platform, File deployment, File configuration, File logOut, String... csvValues) {
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
			runtimes.add(runtime);
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
			if (csvValues != null) {
				StringBuilder allValues = new StringBuilder();
				for (String str : csvValues)
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
			return runtime;

		} catch (Exception e) {
			e.printStackTrace();
		}
		runtimes.add(-1.0);
		return -1;
	}

	/**
	 * Given Files holding valid XML values, begins a new Process which runs SimMain, the out and errors streams of which
	 * (including the simgrid log) are redirected to logOut. A simulation is also run locally to retrieve the runtime of the
	 * simulation, by simply calling SimMain.main, an. At the end of the simulation, a row is added to this instance's set
	 * output file in CVS format.
	 * 
	 * This method passes null for cvsValues, which leads to the default CVS values ("test number", "platform file",
	 * "deployment file", "configuration file") being recorded.
	 * 
	 * @param platform
	 *            the SimGrid platform XML file
	 * @param deployment
	 *            the SimGrid deployment XML file
	 * @param configuration
	 *            the native Simulator configuration XML file
	 * @param logOut
	 *            file to write log to. Permitted to be null (in which case log will not be stored).
	 * @return double which is the runtime of the simulation or -1 if problem was encountered
	 */
	public double runSimulation(File platform, File deployment, File configuration, File logOut) {
		return runSimulation(platform, deployment, configuration, logOut, (String[]) null);
	}

	/**
	 * Returns the time a certain simulation run by this SimulationsRunner took.
	 * 
	 * @param index
	 *            the number of the simulation
	 * @return the time the wanted simulation took, or -1 if index larger than count or simulation had error
	 */
	public double getRuntime(int index) {
		if (index < simulationCount) {
			runtimes.get(index);
		}
		return -1;
	}

	/**
	 * Getter for simulation count
	 * 
	 * @return the simulation count
	 */
	public int getSimulationCount() {
		return simulationCount;
	}

	/**
	 * Generates a set of deep-copy permutations of a single XMLDocument in which only one XMLElement differs. The provided
	 * XMLElement must be a reference gotten from the provided XMLDocument for this to work.
	 * 
	 * @param doc
	 *            the XMLDocument to generate permutation of
	 * @param toModify
	 *            the element to change values for - must be referencing an element that the XMLDocument also stored in a
	 *            reference
	 * @param values
	 *            the values to give to the provided XMLElement
	 * @return array of size values.length holding permutations of XMLDocument only differing by toModify having the given
	 *         values
	 */
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
	 * Gets a range of equally spaced double values as String to help generate permutation of XLM files. Data validation is
	 * performed so invalid input should not break a program but will return bad results.
	 * 
	 * @param start
	 *            the min value
	 * @param end
	 *            the max value
	 * @param the
	 *            step value by which to move from start to end
	 * @return array holding all multiples of start+add*n which are >=start and <=end
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
