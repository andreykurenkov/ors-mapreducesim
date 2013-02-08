package mapreducesim.tasks;

import mapreducesim.core.HostData;
import mapreducesim.core.SimFile;
import mapreducesim.core.SimFile.SimFileLocation;

import org.simgrid.msg.Host;
import org.simgrid.msg.Task;

public class WriteTask extends Task {
	private int timeTaken;
	private SimFile file;
	private boolean writeDone;
	private Host destination;

	public WriteTask(SimFile file, Host destination) {
		this.timeTaken = 0;
		this.file = file;
		this.destination = destination;
	}

	public int getTimeTaken() {
		return timeTaken;
	}

	public void setTimeTaken(int timeTaken) {
		this.timeTaken = timeTaken;
	}

	public SimFile getFile() {
		return file;
	}

	public Host getDestination() {
		return destination;
	}

	public void finishWrite() {
		writeDone = true;
		HostData hostData = (HostData) destination.getData();
		hostData.addFile(file);
	}

	public boolean isWriteDone() {
		return writeDone;
	}
}
