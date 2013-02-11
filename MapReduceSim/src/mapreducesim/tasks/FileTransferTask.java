package mapreducesim.tasks;

import org.simgrid.msg.Host;
import org.simgrid.msg.Task;

import mapreducesim.core.HostData;
import mapreducesim.core.SimFile;
import mapreducesim.core.SimFile.SimFileLocation;

public class FileTransferTask extends Task {
	private int timeTaken;
	private SimFile transferFile;
	private SimFileLocation location;
	private boolean readDone;

	public FileTransferTask(SimFileLocation location) {
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
		this.transferFile = readFile;
		HostData hostData = (HostData) this.getSource().getData();
		hostData.addFile(readFile);
	}

	public SimFile getTransferFile() {
		return transferFile;
	}

	public boolean isTransferDone() {
		return readDone;
	}

	public static class ReadFileRequestTask extends Task {
		private SimFileLocation location;
		private boolean readDone;

		public ReadFileRequestTask(SimFileLocation location) {
			this.location = location;
		}

		public SimFileLocation getLocation() {
			return location;
		}

		public boolean isTransferDone() {
			return readDone;
		}
	}

	public static class WriteFileRequestTask extends Task {
		private int timeTaken;
		private SimFile file;
		private Host destination;

		public WriteFileRequestTask(SimFile file, Host destination) {
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

	}

}
