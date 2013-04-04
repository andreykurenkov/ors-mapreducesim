package mapreducesim.scheduling;

import java.util.Arrays;
import java.util.List;

import mapreducesim.util.xml.XMLElement;

import org.simgrid.msg.Msg;

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

	public List<String> preferredLocations = null;
	public final Type type;
	public Status status;

	public TaskCacheEntry(List<String> preferredLocation, Type type,
			StatusType initialStatus) {

		taskData = new InputSplit();
		this.preferredLocations = preferredLocation;
		this.type = type;
		this.status = new Status();
		this.status.statusType = initialStatus;
	}

	public TaskCacheEntry(Type type, StatusType initialStatus) {
		this(null, type, initialStatus);
	}

	public TaskCacheEntry(List<String> preferredLocation, Type type) {
		this(preferredLocation, type, StatusType.NOTSTARTED);
	}

	public String toString() {
		return this.type + " " + this.status
				+ (this.taskData == null ? "" : (", data = " + this.taskData))
				+ ", preferredLocation = " + this.preferredLocations;
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

		// get preferred location
		String preferredLoc;
		if (taskNode.getAttributes().keySet().contains("preferredLocation")) {
			preferredLoc = taskNode.getAttributeValue("preferredLocation");
		} else {
			throw new RuntimeException(
					"If constructing task from xml, xml tag must contain preferred location");
		}
		TaskCacheEntry tce = new TaskCacheEntry(Arrays.asList(preferredLoc),
				type, StatusType.NOTSTARTED);
		Msg.info("Returning constructed task cache entry: " + tce);
		// construct and return
		return tce;
	}
}
