package mapreducesim.execution.test;

import java.util.ArrayList;

import mapreducesim.core.SimProcess;
import mapreducesim.scheduling.NotifyNoMoreTasks;
import mapreducesim.storage.FileBlock;
import mapreducesim.storage.FileTransferTask;
import mapreducesim.storage.FileTransferTask.ReadRequestTask;
import mapreducesim.storage.FileTransferTask.WriteRequestTask;
import mapreducesim.storage.KeyValuePairs;

import org.simgrid.msg.Host;
import org.simgrid.msg.Msg;
import org.simgrid.msg.MsgException;
import org.simgrid.msg.Task;
import org.simgrid.msg.TimeoutException;

public class TestStorage extends SimProcess {
	private double constantCost;
	private int keyValueNum;
	private int keyValueSize;

	public TestStorage(Host host, String name, String[] args) {
		super(host, name, args);
		constantCost = args.length > 0 ? Double.parseDouble(args[0]) : 0.5;
		keyValueNum = args.length > 1 ? Integer.parseInt(args[1]) : 64;
		keyValueSize = args.length > 2 ? Integer.parseInt(args[2]) : 1;
	}

	@Override
	public void main(String[] arg0) throws MsgException {
		while (!finished) {
			// get the next task from the storage interface mailbox
			try {
				Task currentTask = Task.receive(MAILBOX);
				// handle task appropriately

				if (currentTask instanceof WriteRequestTask) { // write task
					// update the actual filesystem, etc.
					Msg.info("Writing file '"
 + ((WriteRequestTask) currentTask).getFileBlock());
					// currentTask.execute();
					// simulate the expense
				}

				if (currentTask instanceof ReadRequestTask) { // read task
					// update the actual filesystem, etc. (metadata for read)
					Msg.info("Reading file '"
 + ((ReadRequestTask) currentTask).getName());
					elapseTime(constantCost);
					String loc = ((ReadRequestTask) currentTask)
							.getOriginMailbox();
					ArrayList<FileBlock> fakeRead = new ArrayList<FileBlock>();
					fakeRead.add(new FileBlock(null, 50, new KeyValuePairs(keyValueNum, keyValueSize)));
					(new FileTransferTask(fakeRead)).send(loc);
				}
				if (currentTask instanceof NotifyNoMoreTasks)
					this.finish();

			} catch (TimeoutException e) {
				this.finish();
			}
		}
	}
}