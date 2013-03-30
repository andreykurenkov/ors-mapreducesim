package mapreducesim.scheduling.test;

import org.simgrid.msg.NativeException;

import mapreducesim.core.ConfigurableClass;
import mapreducesim.core.SimMain;
import mapreducesim.scheduling.JobMaker;
import mapreducesim.scheduling.MapReduceJobSpecification;
import mapreducesim.util.SmartFile;
import mapreducesim.util.xml.XMLDocument;
import mapreducesim.util.xml.XMLElement;
import mapreducesim.util.xml.XMLNode;

public class LocalityTestJobMaker extends JobMaker {

	static final int NUM_NODES = 100;
	static final double GAUSSIAN_WIDTH_MIN = 1;
	static final double GAUSSIAN_WIDTH_MAX = NUM_NODES;
	static final double GAUSSIAN_CENTER = (NUM_NODES - 1) / 2.0;
	static final int NUM_SIMULATIONS = 6;
	static final double TASKS_NODE_RATIO = 2;

	static int simIndex = 0;

	public LocalityTestJobMaker(XMLElement input) {
		super(input);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {

		String config_loc = "tests/schedulertest/locality/config.xml";
		String plat_loc = "tests/schedulertest/locality/plat.xml";
		String depl_loc = "tests/schedulertest/locality/depl.xml";

		// write the config, platform, and deployment files to disk, then run
		// the simulation

		for (simIndex = 0; simIndex < NUM_SIMULATIONS; simIndex++) {

			if (true) {
				new SmartFile(config_loc).write(wrapXMLHeader(getXMLConfig(
						simIndex).toRawXML(XMLNode.PRETTYFORMAT)), false);
				new SmartFile(plat_loc).write(wrapXMLHeader(getXMLPlatform(
						simIndex).toRawXML(XMLNode.PRETTYFORMAT)), false);
				new SmartFile(depl_loc).write(wrapXMLHeader(getXMLDeployment(
						simIndex).toRawXML(XMLNode.PRETTYFORMAT)), false);
			}

			try {
				SimMain.main(new String[] { plat_loc, depl_loc, config_loc });
			} catch (NativeException e) { // TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static String wrapXMLHeader(String xml) {
		return "<?xml version='1.0'?> <!DOCTYPE platform SYSTEM \"http://simgrid.gforge.inria.fr/simgrid.dtd\">"
				+ xml;
	}

	public static XMLDocument getXMLDeployment(int simIndex) {
		XMLElement root = new XMLElement("platform");
		root.setAttribute("version", "3");
		// create the processes

		// create the JobSubmitter
		XMLElement JobSubmitter = new XMLElement("process");
		JobSubmitter.setAttribute("host", "JobSubmitter");
		JobSubmitter.setAttribute("function",
				"mapreducesim.scheduling.test.JobSubmitterProcess");
		XMLElement arg = new XMLElement("argument");
		arg.setAttribute("value", "job0");
		JobSubmitter.addChild(arg);
		root.addChild(JobSubmitter);

		// create the storage process
		XMLElement storageProcess = new XMLElement("process");
		storageProcess.setAttribute("host", "Storage");
		storageProcess.setAttribute("function",
				"mapreducesim.storage.StorageProcess");
		root.addChild(storageProcess);

		// add the job tracker
		XMLElement jobTracker = new XMLElement("process");
		jobTracker.setAttribute("host", "JobTracker");
		jobTracker.setAttribute("function",
				"mapreducesim.scheduling.test.FIFOScheduler");
		root.addChild(jobTracker);

		// add all the task trackers
		for (int i = 0; i < NUM_NODES; i++) {
			XMLElement taskTr = new XMLElement("process");
			taskTr.setAttribute("host", "TaskTracker" + i);
			taskTr.setAttribute("function",
					"mapreducesim.execution.TaskRunnerProcess");
			root.addChild(taskTr);
		}

		return new XMLDocument(root);
	}

	public static XMLDocument getXMLPlatform(int simIndex) {
		XMLElement root = new XMLElement("platform");
		root.setAttribute("version", "3");
		XMLElement AS = new XMLElement("AS");
		AS.setAttribute("id", "main");
		AS.setAttribute("routing", "Full");
		// add the job tracker
		XMLElement jobTracker = new XMLElement("host");
		jobTracker.setAttribute("id", "JobTracker");
		jobTracker.setAttribute("power", "1");
		jobTracker.setAttribute("state", "ON");
		AS.addChild(jobTracker);

		// add the job submitter
		XMLElement jobSubmitter = new XMLElement("host");
		jobSubmitter.setAttribute("id", "JobSubmitter");
		jobSubmitter.setAttribute("power", "1");
		jobSubmitter.setAttribute("state", "ON");
		AS.addChild(jobSubmitter);

		// add the storage head node
		XMLElement storage = new XMLElement("host");
		storage.setAttribute("id", "Storage");
		storage.setAttribute("power", "1");
		storage.setAttribute("state", "ON");
		AS.addChild(storage);

		// add the task trackers
		for (int i = 0; i < NUM_NODES; i++) {
			XMLElement taskTracker = new XMLElement("host");
			taskTracker.setAttribute("id", "TaskTracker" + i);
			taskTracker.setAttribute("power", "1");
			taskTracker.setAttribute("state", "ON");
			AS.addChild(taskTracker);
		}

		// add the <link>
		XMLElement link = new XMLElement("link");
		link.setAttribute("id", "link0");
		link.setAttribute("bandwidth", "100000");
		link.setAttribute("latency", "0.1");
		AS.addChild(link);

		// add the routes (tree topology for now)
		for (int i = 0; i < NUM_NODES; i++) {
			XMLElement route = new XMLElement("route");
			route.setAttribute("src", "TaskTracker" + i);
			route.setAttribute("dst", "JobTracker");
			XMLElement link_ctn = new XMLElement("link_ctn");
			link_ctn.setAttribute("id", "link0");
			route.addChild(link_ctn);
			AS.addChild(route);
			route = new XMLElement("route");
			route.setAttribute("src", "TaskTracker" + i);
			route.setAttribute("dst", "Storage");
			link_ctn = new XMLElement("link_ctn");
			link_ctn.setAttribute("id", "link0");
			route.addChild(link_ctn);
			AS.addChild(route);

		}

		XMLElement route = new XMLElement("route");
		route.setAttribute("src", "JobSubmitter");
		route.setAttribute("dst", "JobTracker");
		XMLElement link_ctn = new XMLElement("link_ctn");
		link_ctn.setAttribute("id", "link0");
		route.addChild(link_ctn);
		AS.addChild(route);

		root.addChild(AS);
		return new XMLDocument(root);

	}

	public static XMLDocument getXMLConfig(int simIndex) {
		XMLElement root = new XMLElement("config");
		XMLElement JobMaker = new XMLElement("JobMaker");
		JobMaker.setAttribute(ConfigurableClass.CLASS_ATTRIBUTE_KEY,
				"mapreducesim.scheduling.SimpleJobMaker");
		JobMaker.addChild(getJobSpecification(simIndex));
		XMLElement fileSplitter = new XMLElement("FileSplitter");
		fileSplitter.setAttribute(ConfigurableClass.CLASS_ATTRIBUTE_KEY,
				"mapreducesim.scheduling.SimpleFileSplitter");
		fileSplitter.setAttribute("type", "random");
		root.addChild(fileSplitter);
		root.addChild(JobMaker);
		return new XMLDocument(root);
	}

	public static XMLElement getJobSpecification(int simIndex) {
		// TODO Auto-generated method stub

		// get the width of the gaussian distribution to use for this simulation
		// instance
		double gaussWidth = (GAUSSIAN_WIDTH_MAX - GAUSSIAN_WIDTH_MIN)
				* simIndex / NUM_SIMULATIONS + GAUSSIAN_WIDTH_MIN;

		// System.out.println("gausswidth = " + gaussWidth);

		// compute the normalized gaussian
		double[] gaussOut = new double[NUM_NODES];
		double[] gaussIn = new double[NUM_NODES];
		double cumSum = 0;
		for (int i = 0; i < NUM_NODES; i++) {
			gaussIn[i] = i;
			gaussOut[i] = TASKS_NODE_RATIO
					* NUM_NODES
					/ (gaussWidth * Math.sqrt(2 * Math.PI))
					* Math.exp(-(i - GAUSSIAN_CENTER) * (i - GAUSSIAN_CENTER)
							/ (2 * gaussWidth * gaussWidth));
			// System.out.println("gaussOut[" + i + "] = " + gaussOut[i]);
			cumSum += gaussOut[i];
		}
		// System.out.println("CumSum = " + cumSum);

		// compute the number of tasks at each location
		int[] numTasks = new int[NUM_NODES];
		int cTasks = 0;
		for (int i = 0; i < NUM_NODES; i++) {
			int c = (int) Math.round(gaussOut[i]);

			numTasks[i] = c;
			cTasks += c;

		}

		for (int i = 0; i < NUM_NODES; i++) {
			System.out.println("Tasks at node " + i + ": " + numTasks[i]);
		}
		// System.out.println("Total tasks: " + cTasks);

		// now to generate an xml element with this distribution
		XMLElement jobEle = new XMLElement("job");
		jobEle.setAttribute("name", "job0");
		jobEle.setAttribute("taskSpecification", "full");

		// add all the tasks
		for (int i = 0; i < NUM_NODES; i++) {
			for (int j = 0; j < numTasks[i]; j++) {
				XMLElement taskEle = new XMLElement("task");
				taskEle.setAttribute("type", "map");
				taskEle.setAttribute("preferredLocation", "TaskTracker" + i);
				jobEle.addChild(taskEle);
			}
		}

		// System.out.println(jobEle.toRawXML(XMLNode.PRETTYFORMAT));

		return jobEle;
	}

	@Override
	public MapReduceJobSpecification getJob(String jobName) {
		return MapReduceJobSpecification.constructFromXML(LocalityTestJobMaker
				.getJobSpecification(simIndex++));
	}

}
