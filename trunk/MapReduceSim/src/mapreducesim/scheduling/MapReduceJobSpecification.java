package mapreducesim.scheduling;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.simgrid.msg.Msg;

import mapreducesim.core.ConfigurableClass;
import mapreducesim.core.SimConfig;
import mapreducesim.scheduling.TaskCacheEntry.Type;
import mapreducesim.util.xml.XMLElement;
import mapreducesim.util.xml.XMLNode;

/**
 * How an input job is specified in our framework. Can be constructed from the
 * config.xml file.
 * 
 * @author tdoneal
 * 
 */
public class MapReduceJobSpecification {

	private String name;
	private List<TaskCacheEntry> mapTasks;
	private List<TaskCacheEntry> reduceTasks;

	/**
	 * @param name
	 * @param mapTasks
	 * @param reduceTasks
	 */
	public MapReduceJobSpecification(String name,
			List<TaskCacheEntry> mapTasks, List<TaskCacheEntry> reduceTasks) {
		super();
		this.name = name;
		this.mapTasks = mapTasks;
		this.reduceTasks = reduceTasks;

	}

	/**
	 * Getter for the name
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter for the mapTasks
	 * 
	 * @return the mapTasks
	 */
	public List<TaskCacheEntry> getOriginalMapTasks() {
		return mapTasks;
	}

	/**
	 * Getter for the reduceTasks
	 * 
	 * @return the reduceTasks
	 */
	public List<TaskCacheEntry> getOriginalReduceTasks() {
		return reduceTasks;
	}

	/**
	 * This function looks through the config xml and creates a
	 * MapReduceJobSpecification object from it
	 * 
	 * @param jobName
	 * @return
	 */
	public static MapReduceJobSpecification constructFromXML(
			XMLElement jobListElement, String jobName) {

		if (SimConfig.CONFIG == null) {
			throw new RuntimeException(
					"Cannot construct job from xml because no config was specified");
		}

		try {
			// get the inputjobs element
			XMLElement inputJobs = jobListElement;

			// go through the jobs, looking for the appropriately named one
			List<XMLNode> nodes = inputJobs.getChildren();
			for (int i = 0; i < nodes.size(); i++) {
				XMLNode node = nodes.get(i);
				if (node instanceof XMLElement) {
					XMLElement enode = (XMLElement) node;
					if (enode.getAttributeValue("name").equals(jobName)) {
						// we found the appropriate job
						return constructFromXML(enode);
					}
				}
			}

			// job not found
			throw new RuntimeException("Job '" + jobName
					+ "' not found in xml.  Double-check your spelling");

		} catch (Exception e) {
			throw new RuntimeException(
					"Error parsing xml for input jobs.  Double-check your syntax",
					e);
		}

	}

	public static MapReduceJobSpecification constructFromXML(XMLElement jobXML) {

		List<TaskCacheEntry> maps = new ArrayList<TaskCacheEntry>();
		List<TaskCacheEntry> reduces = new ArrayList<TaskCacheEntry>();
		// extract job name
		String jobName = jobXML.getAttributeValue("name");

		String taskSpec = jobXML.getAttributeValue("taskSpecification");
		if (taskSpec.equalsIgnoreCase("full")) {
			// get a list of xml nodes containing each task
			List<XMLElement> taskNodes = jobXML.getChildrenByName("task");

			for (int i = 0; i < taskNodes.size(); i++) {
				XMLElement taskNode = taskNodes.get(i);
				TaskCacheEntry tce = TaskCacheEntry.constructFromXML(taskNode);
				if (tce.type == Type.MAP) {

					maps.add(tce);
				} else if (tce.type == Type.REDUCE) {
					reduces.add(tce);
				} else {
					throw new RuntimeException("Invalid type on task: "
							+ tce.type);
				}
			}

			MapReduceJobSpecification returnVal = new MapReduceJobSpecification(
					jobName, maps, reduces);

			// we need to get the input splits for this map task

			// TODO: annotate with input split data
			FileSplitter fs = ConfigurableClass
					.instantiateFromSimConfig(FileSplitter.class);
			List<InputSplit> iSplits = fs.getInputSlits(returnVal);
			Msg.info("input splits for job " + jobName + ":" + iSplits);

			if (returnVal.getOriginalMapTasks().size() != iSplits.size()) {
				throw new RuntimeException(
						"Number of input splits obtained from FileSplitter ("
								+ iSplits.size()
								+ ") didn't match number of map tasks ("
								+ returnVal.getOriginalMapTasks().size() + ")");
			}

			for (int i = 0; i < returnVal.getOriginalMapTasks().size(); i++) {
				TaskCacheEntry tce = returnVal.getOriginalMapTasks().get(i);
				tce.taskData = iSplits.get(i);
			}

			return returnVal;

		} else {
			throw new RuntimeException(
					"Task specifications other than 'full' not supported at this time");
		}

	}

	public String toString() {
		return "name=" + name + ", numMaps="
				+ this.getOriginalMapTasks().size() + ", numReduces="
				+ this.getOriginalReduceTasks().size();
	}

}
