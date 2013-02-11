package mapreducesim.scheduling;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.simgrid.msg.Task;

public class TaskPool {

	private List<HadoopTask> tasks;
	private Map<String,List<HadoopTask>> internal_mapByNode;
	private Map<HadoopTask,String> internal_mapByTask;
	
	public TaskPool(){
		tasks = new LinkedList<HadoopTask>();
		internal_mapByNode = new HashMap<String,List<HadoopTask>>();
		internal_mapByTask = new HashMap<HadoopTask,String>();
	}
	
	public List<HadoopTask> getByPreferredLocation(String preferredLocation){
		return internal_mapByNode.get(preferredLocation);
	}
	
	public String getPreferredLocation(HadoopTask t){
		return internal_mapByTask.get(t);
	}
	
	public void addTask(HadoopTask t, String preferredLocation){
		tasks.add(t);
		List<HadoopTask> l = internal_mapByNode.get(preferredLocation);
		if (l==null){
			l = new LinkedList<HadoopTask>();
			internal_mapByNode.put(preferredLocation, l);
		}else {
			l.add(t);
		}
		internal_mapByTask.put(t, preferredLocation);
	}
	
	public void removeTask(HadoopTask t){
		tasks.remove(t);
		String preferredLocation = internal_mapByTask.get(t);
		internal_mapByNode.get(preferredLocation).remove(t);
		internal_mapByTask.remove(t);
	}
	
	
}
