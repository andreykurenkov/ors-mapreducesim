package mapreducesim.scheduling;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.simgrid.msg.Task;

public class TaskPool {

	private List<HadoopTaskCacheEntry> tasks;
	private Map<String,List<HadoopTaskCacheEntry>> internal_mapByNode;
	private Map<HadoopTaskCacheEntry,String> internal_mapByTask;
	
	public TaskPool(){
		tasks = new LinkedList<HadoopTaskCacheEntry>();
		internal_mapByNode = new HashMap<String,List<HadoopTaskCacheEntry>>();
		internal_mapByTask = new HashMap<HadoopTaskCacheEntry,String>();
	}
	
	public List<HadoopTaskCacheEntry> getByPreferredLocation(String preferredLocation){
		return internal_mapByNode.get(preferredLocation);
	}
	
	public HadoopTaskCacheEntry getArbitrary(){
		return tasks.get(0);
	}
	
	public String getPreferredLocation(HadoopTaskCacheEntry t){
		return internal_mapByTask.get(t);
	}
	
	public void addTask(HadoopTaskCacheEntry t, String preferredLocation){
		tasks.add(t);
		List<HadoopTaskCacheEntry> l = internal_mapByNode.get(preferredLocation);
		if (l==null){
			l = new LinkedList<HadoopTaskCacheEntry>();
			internal_mapByNode.put(preferredLocation, l);
		}else {
			l.add(t);
		}
		internal_mapByTask.put(t, preferredLocation);
	}
	
	public void removeTask(HadoopTaskCacheEntry t){
		tasks.remove(t);
		String preferredLocation = internal_mapByTask.get(t);
		internal_mapByNode.get(preferredLocation).remove(t);
		internal_mapByTask.remove(t);
	}
	
	
}
