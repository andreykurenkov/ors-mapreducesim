package mapreducesim.storage;

import org.simgrid.msg.Host;
import org.simgrid.msg.Task;

public class FileTransferTask extends Task {
	private int timeTaken;
	private File transferFile;
	private boolean readDone;

	public FileTransferTask(File transferFile) {
		this.timeTaken = 0;
		this.transferFile = transferFile;
	}

	public int getTimeTaken() {
		return timeTaken;
	}

	public void setTimeTaken(int timeTaken) {
		this.timeTaken = timeTaken;
	}

	public void finishRead(File readFile) {
		readDone = true;
		this.transferFile = readFile;
	}

	public File getTransferFile() {
		return transferFile;
	}

	public boolean isTransferDone() {
		return readDone;
	}

	public static class ReadFileRequestTask extends Task {
		private DataLocation location;
		private boolean readDone;
		public final String originMailbox;

		public ReadFileRequestTask(DataLocation location, String origin) {
			this.location = location;
			originMailbox = origin;
		}

		public DataLocation getFileLocation() {
			return location;
		}

		public boolean isTransferDone() {
			return readDone;
		}
	}

	public static class WriteFileRequestTask extends Task {
		private int timeTaken;
		private File file;
		private Host destination;

		public WriteFileRequestTask(File file, Host destination) {
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

		public File getFile() {
			return file;
		}

		public Host getDestination() {
			return destination;
		}

	}

}
