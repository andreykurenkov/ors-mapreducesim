package mapreducesim.util;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

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
		// generateTaskrunnersVsRuntime(true);
		generateTaskrunnersVsTaskNum(true);

	}

	public static void generateTaskrunnersVsTaskNum(boolean read) throws IOException {
		SmartFile outFile = new SmartFile("./results/TaskrunnersVsTaskNumOut.csv");

		double[][] results = new double[15][15];

		if (outFile.exists() && read) {
			Scanner scan = new Scanner(outFile);
			scan.nextLine();
			for (int taskNum = 0; taskNum < 15; taskNum++) {
				for (int runnerIndex = 0; runnerIndex < 15; runnerIndex++) {
					String[] parts = scan.nextLine().split(",");
					double runtime = Double.parseDouble(parts[2]);
					results[runnerIndex][taskNum] = runtime;
				}
			}
			scan.close();
		} else {
			SimulationsRunner runner = new SimulationsRunner(outFile, "Power", "Number of TaskRunners");
			File platFile = FileUtil.getProjectFile("taskRunnerNumTest", "plat.xml");
			File deplFile = FileUtil.getProjectFile("taskRunnerNumTest", "depl.xml");
			File configFile = FileUtil.getProjectFile("taskRunnerNumTest", "config.xml");
			XMLDocument config = XMLDocument.parseDocument(configFile);

			String[] tasksNums = SimulationsRunner.getValueRange(50, 200, 10);
			for (int taskNum = 0; taskNum < 15; taskNum++) {
				XMLDocument depl = XMLDocument.parseDocument(deplFile);
				XMLDocument plat = XMLDocument.parseDocument(platFile);
				String tasksNum = tasksNums[taskNum];
				for (int runnerIndex = 0; runnerIndex < 15; runnerIndex++) {
					int num = runnerIndex + 3;
					String runnerName = "TaskTracker" + num;

					XMLElement newRunner = new XMLElement("process");
					newRunner.setAttribute("function", "mapreducesim.execution.TaskRunnerProcess");
					newRunner.setAttribute("host", runnerName);
					depl.getRoot().addChild(newRunner);

					XMLElement platRoot = plat.getRoot().getChildByName("AS");

					XMLElement newHost = new XMLElement("host");
					newHost.setAttribute("id", runnerName);
					newHost.setAttribute("power", "800");
					newHost.setAttribute("state", "ON");
					platRoot.addChild(newHost, 0);

					XMLElement link = new XMLElement("link_ctn");
					link.setAttribute("id", "link0");

					XMLElement newroute1 = new XMLElement("route");
					newroute1.setAttribute("src", runnerName);
					newroute1.setAttribute("dst", "JobTracker");
					newroute1.addChild(link);
					platRoot.addChild(newroute1);
					XMLElement newroute2 = new XMLElement("route");
					newroute2.setAttribute("src", runnerName);
					newroute2.setAttribute("dst", "Storage");
					newroute2.addChild(link);
					platRoot.addChild(newroute2);

					XMLElement jobMaker = config.getRoot().getChildByName("JobMaker");
					XMLElement job = jobMaker.getChildByName("job");
					job.setAttribute("numTask", tasksNum);
					job.setAttribute("numRunners", "" + num);

					results[runnerIndex][taskNum] = runner.runSimulation(plat, depl, config, new File("./out/out"
							+ runnerIndex + ".txt"), tasksNum, "" + num);
					if (results[runnerIndex][taskNum] == -1)
						return;
				}
			}
		}
		ArraysSurfaceModel model = new ArraysSurfaceModel(results, new double[] { 4, 50 }, new double[] { 1, 10 },
				new String[] { "Number of TaskRunners", "Number of Tasks", "Job Runtime" });
		GraphingFrame f = new GraphingFrame(model);

	}

	public static void generateTaskrunnersVsRuntime(boolean read) throws IOException {
		SmartFile outFile = new SmartFile("./results/TaskrunnersVsRuntime.csv");

		double[][] results = new double[10][20];
		if (outFile.exists() && read) {
			Scanner scan = new Scanner(outFile);
			scan.nextLine();
			for (int power = 0; power < 20; power++) {
				for (int runnerIndex = 0; runnerIndex < 10; runnerIndex++) {
					String[] parts = scan.nextLine().split(",");
					double runtime = Double.parseDouble(parts[2]);
					results[runnerIndex][power] = runtime;
				}
			}
			scan.close();
		} else {
			SimulationsRunner runner = new SimulationsRunner(outFile, "Power", "Number of TaskRunners");
			File platFile = FileUtil.getProjectFile("taskRunnerNumTest", "plat.xml");
			File deplFile = FileUtil.getProjectFile("taskRunnerNumTest", "depl.xml");
			File configFile = FileUtil.getProjectFile("taskRunnerNumTest", "config.xml");
			XMLDocument config = XMLDocument.parseDocument(configFile);

			String[] powers = SimulationsRunner.getValueRange(500, 1500, 50);
			for (int powerIndex = 0; powerIndex < 20; powerIndex++) {
				XMLDocument depl = XMLDocument.parseDocument(deplFile);
				XMLDocument plat = XMLDocument.parseDocument(platFile);
				String power = powers[powerIndex];
				for (int runnerIndex = 0; runnerIndex < 10; runnerIndex++) {
					int num = runnerIndex + 3;
					String runnerName = "TaskTracker" + num;

					XMLElement newRunner = new XMLElement("process");
					newRunner.setAttribute("function", "mapreducesim.execution.TaskRunnerProcess");
					newRunner.setAttribute("host", runnerName);
					depl.getRoot().addChild(newRunner);

					XMLElement platRoot = plat.getRoot().getChildByName("AS");

					XMLElement newHost = new XMLElement("host");
					newHost.setAttribute("id", runnerName);
					newHost.setAttribute("power", power);
					newHost.setAttribute("state", "ON");
					platRoot.addChild(newHost, 0);

					XMLElement link = new XMLElement("link_ctn");
					link.setAttribute("id", "link0");

					XMLElement newroute1 = new XMLElement("route");
					newroute1.setAttribute("src", runnerName);
					newroute1.setAttribute("dst", "JobTracker");
					newroute1.addChild(link);
					platRoot.addChild(newroute1);
					XMLElement newroute2 = new XMLElement("route");
					newroute2.setAttribute("src", runnerName);
					newroute2.setAttribute("dst", "Storage");
					newroute2.addChild(link);
					platRoot.addChild(newroute2);

					XMLElement jobMaker = config.getRoot().getChildByName("JobMaker");
					XMLElement job = jobMaker.getChildByName("job");
					job.setAttribute("numRunners", "" + num);

					results[runnerIndex][powerIndex] = runner.runSimulation(plat, depl, config, new File("./out/out"
							+ runnerIndex + ".txt"), power, "" + num);
					if (results[runnerIndex][powerIndex] == -1)
						return;

				}
			}
		}
		ArraysSurfaceModel model = new ArraysSurfaceModel(results, new double[] { 4, 500 }, new double[] { 1, 50 },
				new String[] { "Number of TaskRunners", "Power of TaskRunners", "Job Runtime" });
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
		XMLElement timer = config.getRoot().getChildByName("WorkTaskTimer");
		String[] heartbeats = SimulationsRunner.getValueRange(10, 1010, 50);
		String[] tasklengths = SimulationsRunner.getValueRange(10, 1010, 50);
		double[][] results = new double[20][20];
		for (int heatbeatIndex = 0; heatbeatIndex < 20; heatbeatIndex++) {
			String heartbeatStr = heartbeats[heatbeatIndex];
			heartbeat.setContentText(heartbeatStr);
			for (int tasklengthIndex = 0; tasklengthIndex < 20; tasklengthIndex++) {
				String tasklengthStr = tasklengths[tasklengthIndex];
				timer.setAttribute("Constant", tasklengthStr);
				results[heatbeatIndex][tasklengthIndex] = runner.runSimulation(platFile, deplFile, config, null,
						heartbeatStr, tasklengthStr);

			}
		}
		ArraysSurfaceModel model = new ArraysSurfaceModel(results, new double[] { 10, 10 }, new double[] { 50, 50 },
				new String[] { "Heartbeat", "Task Length", "Job Runtime" });
		GraphingFrame f = new GraphingFrame(model);
	}
}
