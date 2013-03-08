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
	public void main(String[] args) throws TransferFailureException, HostFailureException, TimeoutException {
		while (!finished) {
			// get the next task from the storage interface mailbox
			Task currentTask = Task.receive(MAILBOX);
			// handle task appropriately

			if (currentTask instanceof WriteRequestTask) { // write task
				// update the actual filesystem, etc.
				Msg.info("Writing file '" + ((WriteRequestTask) currentTask).getFile().getName() + "' at "
						+ this.getTimeElapsed());
				// simulate the expense
				long costRemaining = 6; // dummy value for now.

				Msg.info("Finished writing file '" + ((WriteRequestTask) currentTask).getFile().getName() + "' at "
						+ Msg.getClock());

			}

			if (currentTask instanceof ReadRequestTask) { // read task
				// update the actual filesystem, etc. (metadata for read)
				Msg.info("Reading file '" + ((ReadRequestTask) currentTask).getName() + "' at " + this.getTimeElapsed());
				// simulate the expense
				long costRemaining = 2; // dummy value...

				Msg.info("Finished reading file '" + ((ReadRequestTask) currentTask).getName() + "' at "
						+ this.getTimeElapsed());
			}

		}
	}
}