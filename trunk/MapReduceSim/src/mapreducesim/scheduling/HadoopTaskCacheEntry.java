package mapreducesim.scheduling;

import java.util.List;

public class HadoopTaskCacheEntry {
	
	public static enum Type{
		MAP,REDUCE;
	}
	
	public static class Status {
		public String taskTrackerRunningOn = null;
		public StatusType statusType = StatusType.NOTSTARTED;
	}
	
	public static enum StatusType{
		NOTSTARTED,RUNNING,COMPLETED;
	}

	String preferredLocation = null;
	public final Type TYPE;
	public Status STATUS;
	public HadoopTaskCacheEntry(String preferredLocation,Type type){
		this.preferredLocation = preferredLocation;
		TYPE = type;
	}
	public HadoopTaskCacheEntry(Type type){
		this(null,type);
	}
}
