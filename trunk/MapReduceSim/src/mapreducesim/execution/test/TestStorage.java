package mapreducesim.execution.test;

import mapreducesim.core.MapReduceSimMain;
import mapreducesim.core.SimProcess;
import mapreducesim.interfaces.StorageInterface;
import mapreducesim.storage.File;
import mapreducesim.storage.FileTransferTask;
import mapreducesim.storage.FileTransferTask.ReadFileRequestTask;
import mapreducesim.storage.FileTransferTask.WriteFileRequestTask;

import org.simgrid.msg.Host;
import org.simgrid.msg.Msg;
import org.simgrid.msg.MsgException;
import org.simgrid.msg.Task;
import org.simgrid.msg.TimeoutException;

public class TestStorage extends SimProcess {

	public TestStorage(Host host, String name, String[] args) {
		super(host, name, args, StorageInterface.MAILBOX);
	}

	@Override
	public void main(String[] arg0) throws MsgException {
		while (!finished) {
			// get the next task from the storage interface mailbox
			try {
				Task currentTask = Task.receive(MAILBOX, 150);
				// handle task appropriately

				if (currentTask instanceof WriteFileRequestTask) { // write task
					// update the actual filesystem, etc.
					Msg.info("Writing file '" + ((WriteFileRequestTask) currentTask).getFile().getName() + "' at "
							+ this.getTimeElapsed());
					// currentTask.execute();
					// simulate the expense
				}

				if (currentTask instanceof ReadFileRequestTask) { // read task
					// update the actual filesystem, etc. (metadata for read)
					Msg.info("Reading file '" + ((ReadFileRequestTask) currentTask).getName() + "' at "
							+ this.getTimeElapsed());
					// simulate the expense
					long costRemaining = 2; // dummy value...
					while (costRemaining > 0) {
						costRemaining -= MapReduceSimMain.SIM_STEP;
					}
					(new FileTransferTask(new File(null, "yay"))).send(((ReadFileRequestTask) currentTask).originMailbox);
				}
			} catch (TimeoutException e) {
				this.finish();
			}
		}
	}
}