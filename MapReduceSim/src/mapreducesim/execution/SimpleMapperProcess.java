package mapreducesim.execution;

import mapreducesim.core.MapReduceSimMain;
import mapreducesim.core.SimProcess;
import mapreducesim.interfaces.StorageInterface;
import mapreducesim.storage.File;
import mapreducesim.storage.File.FileLocation;
import mapreducesim.tasks.FileTransferTask;
import mapreducesim.tasks.WorkTask;
import mapreducesim.tasks.FileTransferTask.WriteFileRequestTask;
import mapreducesim.tasks.FileTransferTask.ReadFileRequestTask;
import mapreducesim.util.ExceptionUtil;

import org.simgrid.msg.Comm;
import org.simgrid.msg.Host;
import org.simgrid.msg.Msg;
import org.simgrid.msg.MsgException;
import org.simgrid.msg.Process;
import org.simgrid.msg.Task;

//TODO: handle excepptions
public class SimpleMapperProcess extends WorkerProcess {
	private TaskTrackerProcess parent;
	public final double failureRate = 0.001;

	public SimpleMapperProcess(Host host, String name, String mailbox, TaskTrackerProcess parent, WorkTask workTask) {
		super(host, name, mailbox, parent, workTask);
	}

	@Override
	public void main(String[] args) throws MsgException {
		// read needed files
		int totalSize = 0;
		for (FileLocation neededFile : task.NEEDED_FILES) {
			ReadFileRequestTask read = new ReadFileRequestTask(neededFile, MAILBOX);
			read.send(StorageInterface.MAILBOX);
			Task transferTask = Task.receive(this.MAILBOX);
			while (!(transferTask instanceof FileTransferTask)) {
				transferTask = Task.receive(this.MAILBOX);
			}
			// totalSize += ((FileTransferTask) transferTask).getTransferFile().getSize();
		}
		// Do map task
		Msg.info(this.getHost().getName() + " starting " + task + " at " + this.getTimeElapsed());
		long timeToWork = task.WORK_AMOUNT / (int) this.getHost().getSpeed();
		while (timeToWork > 0) {
			timeToWork -= MapReduceSimMain.SIM_STEP;
		}
		Msg.info(this.getHost().getName() + " finishing " + task + " at " + this.getTimeElapsed());
		// Write output
		File outputFile = new File(null, "null");
		WriteFileRequestTask write = new WriteFileRequestTask(outputFile, this.getHost());
		write.send(StorageInterface.MAILBOX);

		this.kill();
	}

}
