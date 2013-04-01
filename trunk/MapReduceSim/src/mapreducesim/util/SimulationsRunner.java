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
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import mapreducesim.core.SimMain;
import mapreducesim.util.graphing.ArraysSurfaceModel;
import mapreducesim.util.graphing.GraphingFrame;
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
	private ArrayList<Double> runtimes;// stores record of past simulation runs,

	// up to simulationCount

	/**
	 * Test main method that runs two simulations
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		SimulationsRunner runner = new SimulationsRunner(new File("data.csv"), "heartbeat", "task length");
		File platFile = FileUtil.getProjectFile("plat.xml");
		File deplFile = FileUtil.getProjectFile("depl.xml");
		File configFile = FileUtil.getProjectFile("config.xml");
		// runner.runSimulation(platFile, deplFile, configFile, new
		// File("./logs/log " + runner.getSimulationCount()));
		XMLDocument config = XMLDocument.parseDocument(configFile);
		// runner.runSimulation(platFile, deplFile, config, new
		// File("./logs/log " + runner.getSimulationCount()));

		XMLElement heartbeat = config.getRoot().getChildByName("heartbeat");
		XMLElement tasklength = config.getRoot().getChildByName("tasklength");
		String[] heartbeats = getValueRange(10, 1010, 50);
		String[] tasklengths = getValueRange(10, 1010, 50);
		double[][] results = new double[20][20];
		for (int heatbeatIndex = 0; heatbeatIndex < 20; heatbeatIndex++) {
			String heartbeatStr = heartbeats[heatbeatIndex];
			heartbeat.setContentText(heartbeatStr);
			for (int tasklengthIndex = 0; tasklengthIndex < 20; tasklengthIndex++) {
				String tasklengthStr = tasklengths[tasklengthIndex];
				tasklength.setContentText(tasklengthStr);
				results[heatbeatIndex][tasklengthIndex] = runner.runSimulation(platFile, deplFile, config, null,
						heartbeatStr, tasklengthStr);

			}
		}
		ArraysSurfaceModel model = new ArraysSurfaceModel(results, new double[] { 10, 10 }, new double[] { 50, 50 },
				new String[] { "Heartbeat", "Task Length", "Job Runtime" });
		GraphingFrame f = new GraphingFrame(model);

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
	public double runSimulation(File platform, File deployment, XMLDocument configuration, File logOut, String... csvValues) {
		try {
			SmartFile tempFile = new SmartFile("tempConfiguration.xml");
			tempFile.createNewFile();// TODO: use boolean?
			String content = configuration.toRawXML(XMLNode.PRETTYFORMAT);
			tempFile.write(content, false);

			double time = runSimulation(platform, deployment, tempFile, logOut, csvValues);

			tempFile.delete();

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
	public double runSimulation(File platform, File deployment, XMLDocument configuration, File logOut) {
		return runSimulation(platform, deployment, configuration, logOut, (String[]) null);
	}

	static void copy(InputStream in, OutputStream out) throws IOException {
		while (true) {
			int c = in.read();
			if (c == -1)
				break;
			out.write((char) c);
		}
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

			File tempFile = new File("temp.txt");
			if (logOut == null)
				logOut = tempFile;

			Process process = builder.start();
			if (logOut != null) {

				// java 6 modifications by troy
				copy(process.getInputStream(), new FileOutputStream(logOut));
				copy(process.getErrorStream(), new FileOutputStream(logOut));
			}

			boolean done = false;
			// TODO: consider launching many Processes in parallel and then
			// reading results.
			double runtime = -1;
			String lastLine = null;
			Scanner scan = new Scanner(logOut);

			while (!done) {
				try {
					process.exitValue();
					done = true;
				} catch (Exception e) {
					done = false;
				}
			}

			while (scan.hasNextLine())
				lastLine = scan.nextLine();
			scan.close();
			if (tempFile != null)
				tempFile.delete();
			if (lastLine != null && lastLine.contains("Simulation time:"))
				runtime = Double.parseDouble(lastLine.substring(lastLine.indexOf(":") + 1));
			else
				throw new RuntimeException("The simulation did not complete succesfully. The last line read in was:\n"
						+ lastLine);

			runtimes.add(runtime);
			System.out.println("Finished simulation " + simulationCount + " at " + runtime);

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
			toReturn[counter++] = String.valueOf((int) val);
		}
		return toReturn;
	}
}
