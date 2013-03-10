package mapreducesim.scheduling;

import java.util.LinkedList;
import java.util.List;

/**
 * Need to define how a hadoop job will be defined in our framework
 * 
 * @author tdoneal
 * 
 */
public class MapReduceJob {

	private String name;
	private List<TaskCacheEntry> mapTasks;
	private List<TaskCacheEntry> reduceTasks;
	private JobStatus status;

	/**
	 * @param name
	 * @param mapTasks
	 * @param reduceTasks
	 */
	public MapReduceJob(String name, List<TaskCacheEntry> mapTasks,
			List<TaskCacheEntry> reduceTasks) {
		super();
		this.name = name;
		this.mapTasks = mapTasks;
		this.reduceTasks = reduceTasks;
		LinkedList<TaskCacheEntry> allTasks = new LinkedList(mapTasks);
		TaskPool pool = new TaskPool();
		allTasks.addAll(reduceTasks);
		for (TaskCacheEntry task : allTasks)
			pool.addTask(task, task.preferredNode);
		status = new JobStatus(name, pool);
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
	 * Getter for the status
	 * 
	 * @return the status
	 */
	public JobStatus getStatus() {
		return status;
	}

}
