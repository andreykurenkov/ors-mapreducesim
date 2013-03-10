package mapreducesim.scheduling;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import mapreducesim.scheduling.TaskCacheEntry.StatusType;
import mapreducesim.scheduling.TaskCacheEntry.Type;

import org.simgrid.msg.Task;

public class TaskPool {

	private List<TaskCacheEntry> tasks;
	private Map<String, List<TaskCacheEntry>> internal_mapByNode;
	private Map<TaskCacheEntry, String> internal_mapByTask;

	public TaskPool() {
		tasks = new LinkedList<TaskCacheEntry>();
		internal_mapByNode = new HashMap<String, List<TaskCacheEntry>>();
		internal_mapByTask = new HashMap<TaskCacheEntry, String>();
	}

	public List<TaskCacheEntry> getByPreferredLocation(String preferredLocation) {
		return internal_mapByNode.get(preferredLocation);
	}

	public List<TaskCacheEntry> getAsList() {
		return tasks;
	}

	public TaskCacheEntry getArbitrary() {
		return tasks.get(0);
	}

	public String getPreferredLocation(TaskCacheEntry t) {
		return internal_mapByTask.get(t);
	}

	public void addTask(TaskCacheEntry t, String preferredLocation) {
		tasks.add(t);
		List<TaskCacheEntry> l = internal_mapByNode.get(preferredLocation);
		if (l == null) {
			l = new LinkedList<TaskCacheEntry>();
			internal_mapByNode.put(preferredLocation, l);
		} else {
			l.add(t);
		}
		internal_mapByTask.put(t, preferredLocation);
	}

	public void removeTask(TaskCacheEntry t) {
		tasks.remove(t);
		String preferredLocation = internal_mapByTask.get(t);
		internal_mapByNode.get(preferredLocation).remove(t);
		internal_mapByTask.remove(t);
	}

	public int getNumNotStartedMap() {
		int c = 0;
		for (int i = 0; i < this.tasks.size(); i++) {
			TaskCacheEntry t = tasks.get(i);
			if (t.status.statusType == StatusType.NOTSTARTED
					&& t.type == Type.MAP) {
				c++;
			}
		}
		return c;
	}

	public int getNumNotStartedReduce() {
		int c = 0;
		for (int i = 0; i < this.tasks.size(); i++) {
			TaskCacheEntry t = tasks.get(i);
			if (t.status.statusType == StatusType.NOTSTARTED
					&& t.type == Type.REDUCE) {
				c++;
			}
		}
		return c;
	}

}
