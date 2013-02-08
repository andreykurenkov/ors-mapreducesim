package mapreducesim.tasks;

import org.simgrid.msg.Host;
import org.simgrid.msg.Task;

import mapreducesim.core.HostData;
import mapreducesim.core.SimFile;
import mapreducesim.core.SimFile.SimFileLocation;

public class ReadTask extends Task {
	private int timeTaken;
	private SimFile readFile;
	private SimFileLocation location;
	private boolean readDone;

	public ReadTask(SimFileLocation location) {
		this.timeTaken = 0;
		this.location = location;
	}

	public int getTimeTaken() {
		return timeTaken;
	}

	public void setTimeTaken(int timeTaken) {
		this.timeTaken = timeTaken;
	}

	public SimFileLocation getLocation() {
		return location;
	}

	public void finishRead(SimFile readFile) {
		readDone = true;
		this.readFile = readFile;
		HostData hostData = (HostData) this.getSource().getData();
		hostData.addFile(readFile);
	}

	public SimFile getReadFile() {
		return readFile;
	}

	public boolean isReadDone() {
		return readDone;
	}
}
