package mapreducesim.scheduling;

import java.util.List;

import mapreducesim.scheduling.FileSplitter.InputSplit;
import mapreducesim.storage.FileBlock;
import mapreducesim.storage.FileBlockLocation;

public class TaskCacheEntry {

	public static enum Type {
		MAP, REDUCE;
	}

	public InputSplit taskData;

	public static class Status {
		public String taskTrackerRunningOn = null;
		public StatusType statusType = StatusType.NOTSTARTED;
	}

	public static enum StatusType {
		NOTSTARTED, ASSIGNED, COMPLETED;
	}

	public String preferredLocation = null;
	public final Type type;
	public Status status;

	public TaskCacheEntry(String preferredLocation, Type type, StatusType initialStatus) {
		this.preferredLocation = preferredLocation;
		this.type = type;
		this.status = new Status();
		this.status.statusType = initialStatus;
	}

	public TaskCacheEntry(Type type, StatusType initialStatus) {
		this(null, type, initialStatus);
	}
}
