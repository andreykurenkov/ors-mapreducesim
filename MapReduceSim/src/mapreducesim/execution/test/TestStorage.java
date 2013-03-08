package mapreducesim.execution.test;

import mapreducesim.core.SimMain;
import mapreducesim.core.SimProcess;
import mapreducesim.storage.File;
import mapreducesim.storage.FileTransferTask;
import mapreducesim.storage.FileTransferTask.ReadRequestTask;
import mapreducesim.storage.FileTransferTask.WriteRequestTask;
import mapreducesim.storage.StorageProcess;

import org.simgrid.msg.Host;
import org.simgrid.msg.Msg;
import org.simgrid.msg.MsgException;
import org.simgrid.msg.Task;
import org.simgrid.msg.TimeoutException;

public class TestStorage extends SimProcess {

	public TestStorage(Host host, String name, String[] args) {
		super(host, name, args, StorageProcess.STORAGE_MAILBOX);
	}

	@Override
	public void main(String[] arg0) throws MsgException {
		while (!finished) {
			// get the next task from the storage interface mailbox
			try {
				Task currentTask = Task.receive(MAILBOX, 150);
				// handle task appropriately

				if (currentTask instanceof WriteRequestTask) { // write task
					// update the actual filesystem, etc.
					Msg.info("Writing file '" + ((WriteRequestTask) currentTask).getFile().getName() + "' at "
							+ this.getTimeElapsed());
					// currentTask.execute();
					// simulate the expense
				}

				if (currentTask instanceof ReadRequestTask) { // read task
					// update the actual filesystem, etc. (metadata for read)
					Msg.info("Reading file '" + ((ReadRequestTask) currentTask).getName() + "' at "
							+ this.getTimeElapsed());
					// simulate the expense
					// long costRemaining = 2; // dummy value...

					(new FileTransferTask(new File(null, "yay"))).send(((ReadRequestTask) currentTask).originMailbox);
				}
			} catch (TimeoutException e) {
				this.finish();
			}
		}
	}
}