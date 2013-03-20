package mapreducesim.storage;

import org.simgrid.msg.*;

import mapreducesim.core.SimMain;
import mapreducesim.core.SimProcess;
import mapreducesim.storage.FileTransferTask.*;

public class StorageProcess extends SimProcess {
	// private int filesize;
	public static String STORAGE_MAILBOX = "Storage";

	public StorageProcess(Host host, String name, String[] args) {
		super(host, name, args, STORAGE_MAILBOX);
	}

	@Override
	public void main(String[] args) throws TransferFailureException,
			HostFailureException, TimeoutException {
		while (!finished) {
			// get the next task from the storage interface mailbox
			Task currentTask = Task.receive(MAILBOX);
			// handle task appropriately

			if (currentTask instanceof WriteRequestTask) { // write task
				// update the actual filesystem, etc.
				Msg.info("Writing file '"
						+ ((WriteRequestTask) currentTask).getFileBlock()
						+ "' at " + this.getTimeElapsed());
				// simulate the expense
				long costRemaining = 6; // dummy value for now.
				// TODO: use elapseTime
				Msg.info("Finished writing file '"
						+ ((WriteRequestTask) currentTask).getFileBlock()
						+ "' at " + Msg.getClock());

			}

			if (currentTask instanceof ReadRequestTask) { // read task
				// TODO combine interfaces...
				Msg.info("Reading file '"
						+ ((ReadRequestTask) currentTask).getName() + "' at "
						+ this.getTimeElapsed());
				DataLocation loc1 = ((ReadRequestTask) currentTask)
						.getFileLocation();
				// origin = ((ReadRequestTask) currentTask).getOriginMailbox();
				// loc1.incrementReads();
				// int speed = loc1.speedBetween(loc2);
				// int size = loc1.get().getSize();
				// long readCost = size / ( speed * 2^20 );
				// elapseTime(readCost);

				Msg.info("Finished reading file '"
						+ ((ReadRequestTask) currentTask).getName() + "' at "
						+ this.getTimeElapsed());
			}

		}
	}
}