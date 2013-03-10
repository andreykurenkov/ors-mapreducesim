package mapreducesim.scheduling;

import java.util.List;

import mapreducesim.core.ConfigurableClass;
import mapreducesim.scheduling.FileSplitter.InputSplit;
import mapreducesim.storage.FileBlock;
import mapreducesim.storage.FileBlockLocation;
import mapreducesim.util.xml.XMLElement;

public class TaskCacheEntry {

	public static enum Type {
		MAP, REDUCE;
	}

	public InputSplit taskData;

	public static class Status {
		public String taskTrackerRunningOn = null;
		public StatusType statusType = StatusType.NOTSTARTED;

		public String toString() {
			return "status " + statusType + " on " + taskTrackerRunningOn;
		}
	}

	public static enum StatusType {
		NOTSTARTED, ASSIGNED, COMPLETED;
	}

	public String preferredNode = null;
	public final Type type;
	public Status status;

	public TaskCacheEntry(String preferredLocation, Type type, StatusType initialStatus) {

		taskData = new InputSplit();
		this.preferredNode = preferredLocation;
		this.type = type;
		this.status = new Status();
		this.status.statusType = initialStatus;
	}

	public TaskCacheEntry(Type type, StatusType initialStatus) {
		this(null, type, initialStatus);
	}

	public String toString() {
		return this.type + " " + this.status;
	}

	public static TaskCacheEntry constructFromXML(XMLElement taskNode) {
		String typeStr = taskNode.getAttributeValue("type");
		Type type = null;
		if (typeStr.equalsIgnoreCase("map")) {
			type = Type.MAP;
		} else if (typeStr.equalsIgnoreCase("reduce")) {
			type = Type.REDUCE;
		} else {
			throw new RuntimeException("Invalid type on task node: " + typeStr);
		}
		// TODO: construct input splits

		// get the FileSplitter
		FileSplitter fs = ConfigurableClass.instantiateFromSimConfig(FileSplitter.class);

		// use the filesplitter to get the input split

		// get preferred location
		String preferredLoc = taskNode.getAttributeValue("preferredLocation");

		// construct and return
		return new TaskCacheEntry(preferredLoc, type, StatusType.NOTSTARTED);
	}
}
