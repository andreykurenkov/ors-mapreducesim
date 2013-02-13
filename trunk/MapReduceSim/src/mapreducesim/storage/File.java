package mapreducesim.storage;

import java.util.List;

public class File extends DataTreeNode{
	private String uri; //A unique file identifier
	private Boolean isDirectory;
	private List<Location> blockReplicaLocations; //a list of locations of replicas
	private int[] accessTimes; //a list of the times file was accessed
	private List<Location> accessorLocations; //a list of the hosts accessing the locations.
	
	public File(DataTreeNode parent, String name) {
		super(parent, name);
	}
	public List<Location> getBlockReplicaLocations() {
		return blockReplicaLocations;
	}
	public void setBlockReplicaLocations(List<Location> blockReplicaLocations) {
		this.blockReplicaLocations = blockReplicaLocations;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public int[] getAccessTimes() {
		return accessTimes;
	}
	public void setAccessTimes(int[] accessTimes) {
		this.accessTimes = accessTimes;
	}
	public void addAccessTime(int currentTime) {
		this.accessTimes[this.accessTimes.length+1] = currentTime;
	}
	public List<Location> getAccessorLocations() {
		return this.accessorLocations;
	}
	public void setAccessorLocations(List<Location> accessorLocations) {
		this.accessorLocations = accessorLocations;
	}
	public void addAccessorLocation(Location accessorLocation) {
		this.accessorLocations.add(accessorLocation);
	}
	public Boolean getIsDirectory() {
		return isDirectory;
	}
	public void setIsDirectory(Boolean isDirectory) {
		this.isDirectory = isDirectory;
	}
	public double calculateReadCost(Location requestor) {
		return 0.01;
	}	
}
