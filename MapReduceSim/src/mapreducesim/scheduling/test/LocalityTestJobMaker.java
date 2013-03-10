package mapreducesim.scheduling.test;

import mapreducesim.scheduling.JobMaker;
import mapreducesim.scheduling.MapReduceJobSpecification;
import mapreducesim.util.xml.XMLDocument;
import mapreducesim.util.xml.XMLElement;
import mapreducesim.util.xml.XMLNode;

public class LocalityTestJobMaker extends JobMaker {

	static final int NUM_NODES = 100;
	static final double GAUSSIAN_WIDTH_MIN = 1;
	static final double GAUSSIAN_WIDTH_MAX = 50;
	static final double GAUSSIAN_CENTER = (NUM_NODES - 1) / 2.0;
	static final int NUM_SIMULATIONS = 100;

	int simIndex = 40;

	public LocalityTestJobMaker(XMLElement input) {
		super(input);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		XMLDocument xmld = getXMLConfig(0);
		System.out.println(xmld.toRawXML(XMLNode.PRETTYFORMAT));
	}

	public static XMLDocument getXMLConfig(int simIndex) {
		XMLElement root = new XMLElement("config");
		XMLElement JobMaker = new XMLElement("JobMaker");
		JobMaker.addChild(getJobSpecification(simIndex));
		root.addChild(JobMaker);
		return new XMLDocument(root);
	}

	public static XMLElement getJobSpecification(int simIndex) {
		// TODO Auto-generated method stub

		// get the width of the gaussian distribution to use for this simulation
		// instance
		double gaussWidth = (GAUSSIAN_WIDTH_MAX - GAUSSIAN_WIDTH_MIN)
				* simIndex / NUM_SIMULATIONS + GAUSSIAN_WIDTH_MIN;

		System.out.println("gausswidth = " + gaussWidth);

		// compute the normalized gaussian
		double[] gaussOut = new double[NUM_NODES];
		double[] gaussIn = new double[NUM_NODES];
		double cumSum = 0;
		for (int i = 0; i < NUM_NODES; i++) {
			gaussIn[i] = i;
			gaussOut[i] = NUM_NODES
					/ (gaussWidth * Math.sqrt(2 * Math.PI))
					* Math.exp(-(i - GAUSSIAN_CENTER) * (i - GAUSSIAN_CENTER)
							/ (2 * gaussWidth * gaussWidth));
			System.out.println("gaussOut[" + i + "] = " + gaussOut[i]);
			cumSum += gaussOut[i];
		}
		System.out.println("CumSum = " + cumSum);

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
		System.out.println("Total tasks: " + cTasks);

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

		System.out.println(jobEle.toRawXML(XMLNode.PRETTYFORMAT));

		return jobEle;
	}

	@Override
	public MapReduceJobSpecification getJob(String jobName) {
		return MapReduceJobSpecification.constructFromXML(LocalityTestJobMaker
				.getJobSpecification(simIndex++));
	}

}
