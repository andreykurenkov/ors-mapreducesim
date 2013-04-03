package mapreducesim.util;

import java.io.File;
import java.io.IOException;

import mapreducesim.util.graphing.ArraysSurfaceModel;
import mapreducesim.util.graphing.GraphingFrame;
import mapreducesim.util.xml.XMLDocument;
import mapreducesim.util.xml.XMLElement;

/**
 * Simple class to store static methods that run tests and generate results
 * 
 * @author Andrey Kurenkov
 * 
 */
public class DataGenerator {

	/**
	 * Test main method that runs two simulations
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		generateHeartbeatvsTasklengthResults();

	}

	public static void generateTaskrunnerVsRuntime() throws IOException {
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
		String[] heartbeats = SimulationsRunner.getValueRange(10, 1010, 50);
		String[] tasklengths = SimulationsRunner.getValueRange(10, 1010, 50);
		double[][] results = new double[20][20];
		for (int heatbeatIndex = 0; heatbeatIndex < 20; heatbeatIndex++) {
			String heartbeatStr = heartbeats[heatbeatIndex];
			heartbeat.setContentText(heartbeatStr);
			for (int tasklengthIndex = 0; tasklengthIndex < 20; tasklengthIndex++) {
				String tasklengthStr = tasklengths[tasklengthIndex];
				tasklength.setContentText(tasklengthStr);
				results[heatbeatIndex][tasklengthIndex] = runner.runSimulation(platFile, deplFile, config, new File(
						"./out/out" + (heatbeatIndex + tasklengthIndex) + ".txt"), heartbeatStr, tasklengthStr);

			}
		}
		ArraysSurfaceModel model = new ArraysSurfaceModel(results, new double[] { 10, 10 }, new double[] { 50, 50 },
				new String[] { "Heartbeat", "Task Length", "Job Runtime" });
		GraphingFrame f = new GraphingFrame(model);

	}

	public static void generateHeartbeatvsTasklengthResults() throws IOException {
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
		String[] heartbeats = SimulationsRunner.getValueRange(10, 1010, 50);
		String[] tasklengths = SimulationsRunner.getValueRange(10, 1010, 50);
		double[][] results = new double[20][20];
		for (int heatbeatIndex = 0; heatbeatIndex < 20; heatbeatIndex++) {
			String heartbeatStr = heartbeats[heatbeatIndex];
			heartbeat.setContentText(heartbeatStr);
			for (int tasklengthIndex = 0; tasklengthIndex < 20; tasklengthIndex++) {
				String tasklengthStr = tasklengths[tasklengthIndex];
				tasklength.setContentText(tasklengthStr);
				results[heatbeatIndex][tasklengthIndex] = runner.runSimulation(platFile, deplFile, config, new File(
						"./out/out"
						+ (heatbeatIndex + tasklengthIndex)
						+ ".txt"),
						heartbeatStr, tasklengthStr);

			}
		}
		ArraysSurfaceModel model = new ArraysSurfaceModel(results, new double[] { 10, 10 }, new double[] { 50, 50 },
				new String[] { "Heartbeat", "Task Length", "Job Runtime" });
		GraphingFrame f = new GraphingFrame(model);
	}
}
