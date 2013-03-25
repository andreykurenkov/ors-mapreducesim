package mapreducesim.storage;

import java.util.List;

import org.simgrid.msg.Host;
import org.simgrid.msg.HostFailureException;
import org.simgrid.msg.Task;
import org.simgrid.msg.TimeoutException;
import org.simgrid.msg.TransferFailureException;

/**
 * Task sent out by StorageProcess to handle simulated transfers of files
 * 
 * @author Andrey Kurenkov
 * @version 1.0 Mar 12, 2013
 */
public class FileTransferTask extends Task {
	private int timeTaken;
	private List<FileBlock> transferFileBlocks;
	private boolean readDone;

	/**
	 * Constructor that creates the transfer task with the file blocks that will
	 * be or have been transferred
	 * 
	 * @param transferFileBlocks
	 *            the blocks to transfer
	 */
	public FileTransferTask(List<FileBlock> transferFileBlocks) {
		this.timeTaken = 0;
		this.transferFileBlocks = transferFileBlocks;
	}

	/**
	 * Constructor that creates the transfer task with null file blocks
	 * 
	 */
	public FileTransferTask() {
		this(null);
	}

	/**
	 * Finishes a read and overwrites the list of fileblocks to the parameter
	 * 
	 * @param readFileBlocks
	 *            new stored list of file blocks
	 */
	public void finishRead(List<FileBlock> readFileBlocks) {
		readDone = true;
		this.transferFileBlocks = readFileBlocks;
	}

	/**
	 * Getter for the timeTaken
	 * 
	 * @return the timeTaken
	 */
	public int getTimeTaken() {
		return timeTaken;
	}

	/**
	 * @param timeTaken
	 *            value of type int that timeTaken will be set to
	 */
	public void setTimeTaken(int timeTaken) {
		this.timeTaken = timeTaken;
	}

	/**
	 * Getter for the transferFileBlocks
	 * 
	 * @return the transferFileBlocks
	 */
	public List<FileBlock> getTransferFileBlocks() {
		return transferFileBlocks;
	}

	/**
	 * Getter for readDone
	 * 
	 * @return readDone
	 */
	public boolean isReadDone() {
		return readDone;
	}

	/**
	 * Simple Task to be sent to StorageProcess to simulate reading files
	 * 
	 * @author Andrey Kurenkov
	 * @version 1.0 Mar 12, 2013
	 */
	public static class ReadRequestTask extends Task {
		private DataLocation location;
		private String filename;
		private int offset;
		private int length;
		private boolean readDone;
		private String originMailbox;

		/**
		 * Constructor for a read request of a specified (offset; length) part
		 * of a file (filename)
		 * 
		 * @param filename
		 * @param offset
		 * @param length
		 * @param originMailbox
		 */
		public ReadRequestTask(String filename, int offset, int length,
				String originMailbox) {
			this.setFilename(filename);
			this.setOffset(offset);
			this.setLength(length);
			this.originMailbox = originMailbox;
		}

		/**
		 * Simple constuctor to set the location of the read
		 * 
		 * Extracts (filename, offset, length) from DataLocation input
		 * 
		 * @param location
		 * @param originMailbox
		 */
		public ReadRequestTask(DataLocation location, String originMailbox) {
			this.location = location;
			this.setFilename(location.getFilename());
			this.setOffset(location.getOffset());
			this.setLength(location.getLength());
			this.originMailbox = originMailbox;
		}

		/**
		 * Returns the locations requested to be read.
		 * 
		 * @return the location
		 */
		public DataLocation getFileLocation() {
			return location;
		}

		/**
		 * Getter for originMailbox
		 * 
		 * @return originMailbox
		 */
		public String getOriginMailbox() {
			return originMailbox;
		}

		/**
		 * Sets the read completion to true
		 */
		public void setReadDone() {
			readDone = true;
		}

		/**
		 * Returns status of read completion as boolean
		 * 
		 * @return true if finished, false otherwise
		 */
		public boolean isTransferDone() {
			return readDone;
		}

		/**
		 * Sends this task to StorageProcess via its constant mailbox
		 * 
		 * @throws TimeoutException
		 * @throws HostFailureException
		 * @throws TransferFailureException
		 */
		public void sendToStorage() throws TransferFailureException,
				HostFailureException, TimeoutException {
			this.send(StorageProcess.DEFAULT_STORAGE_MAILBOX);
		}

		public String getFilename() {
			return filename;
		}

		public void setFilename(String filename) {
			this.filename = filename;
		}

		public int getOffset() {
			return offset;
		}

		public void setOffset(int offset) {
			this.offset = offset;
		}

		public int getLength() {
			return length;
		}

		public void setLength(int length) {
			this.length = length;
		}
	}

	/**
	 * Simple task to simulate requests to write data to file.
	 * 
	 * @author Andrey Kurenkov
	 * @version 1.0 Mar 12, 2013
	 */
	public static class WriteRequestTask extends Task {
		private int timeTaken;
		private FileBlock fileBlock;

		/**
		 * Creates request with the fileblock to write
		 * 
		 * @param fileBlock
		 */
		public WriteRequestTask(FileBlock fileBlock) {
			this.timeTaken = 0;
			this.fileBlock = fileBlock;
		}

		/**
		 * Getter for timeTaken
		 * 
		 * @return timeTaken
		 */
		public int getTimeTaken() {
			return timeTaken;
		}

		/**
		 * @param timeTaken
		 *            value of type int that timeTaken will be set to
		 */
		public void setTimeTaken(int timeTaken) {
			this.timeTaken = timeTaken;
		}

		/**
		 * Getter for the fileBlock
		 * 
		 * @return the fileBlock
		 */
		public FileBlock getFileBlock() {
			return fileBlock;
		}

		/**
		 * Sends this task to StorageProcess via its constant mailbox
		 * 
		 * @throws TimeoutException
		 * @throws HostFailureException
		 * @throws TransferFailureException
		 */
		public void sendToStorage() throws TransferFailureException,
				HostFailureException, TimeoutException {
			System.out.println("Sending thing to "
					+ StorageProcess.DEFAULT_STORAGE_MAILBOX);
			this.send(StorageProcess.DEFAULT_STORAGE_MAILBOX);
		}

	}

}
