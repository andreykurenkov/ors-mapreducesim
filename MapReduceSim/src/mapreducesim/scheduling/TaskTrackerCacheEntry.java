package mapreducesim.scheduling;

public class TaskTrackerCacheEntry {
	private String mailbox;
	public int bestKnownMapSlotsAvailable;
	public int bestKnownReduceSlotsAvailable;

	public TaskTrackerCacheEntry(String mailbox, int bestKnownMapSlotsAvailable, int bestKnownReduceSlotsAvailable){
		this.mailbox = mailbox;
		this.bestKnownMapSlotsAvailable = bestKnownMapSlotsAvailable;
		this.bestKnownReduceSlotsAvailable = bestKnownReduceSlotsAvailable;
	}
	
	public String getMailbox(){
		return mailbox;
	}
	
}
