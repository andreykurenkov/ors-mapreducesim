package mapreducesim.scheduling;

import java.util.List;

/**
 * Need to define how a hadoop job will be defined in our framework
 * @author tdoneal
 *
 */
public class HadoopJob {

	String name;
	List<HadoopTaskCacheEntry> mapTasks;
	List<HadoopTaskCacheEntry> reduceTasks;
	
	
	
}
