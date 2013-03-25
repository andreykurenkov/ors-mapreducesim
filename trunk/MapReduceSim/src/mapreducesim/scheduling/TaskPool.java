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

	public TaskPool() {
		tasks = new LinkedList<TaskCacheEntry>();
	}

	public List<TaskCacheEntry> getAsList() {
		return tasks;
	}

	public TaskCacheEntry getArbitrary() {
		return tasks.get(0);
	}

	public void addTask(TaskCacheEntry t) {
		tasks.add(t);

	}

	public void removeTask(TaskCacheEntry t) {
		tasks.remove(t);

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
