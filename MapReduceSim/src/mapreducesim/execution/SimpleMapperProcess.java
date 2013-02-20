package mapreducesim.execution;

import mapreducesim.execution.tasks.WorkTask;
import mapreducesim.interfaces.StorageInterface;
import mapreducesim.storage.File;
import mapreducesim.storage.File.FileLocation;
import mapreducesim.storage.FileTransferTask;
import mapreducesim.storage.FileTransferTask.ReadFileRequestTask;
import mapreducesim.storage.FileTransferTask.WriteFileRequestTask;

import org.simgrid.msg.Host;
import org.simgrid.msg.Msg;
import org.simgrid.msg.MsgException;
import org.simgrid.msg.Task;

//TODO: handle excepptions
public class SimpleMapperProcess extends WorkerProcess {
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
		long timeToWork = task.WORK_AMOUNT / (int) this.getHost().getSpeed();
		Msg.info(this.getHost().getName() + " starting " + task + " for " + timeToWork + " expected time.");
		super.task.setComputeDuration(timeToWork);
		super.task.execute();
		Msg.info(this.getHost().getName() + " finishing " + task);
		// Write output
		File outputFile = new File(null, "null");
		WriteFileRequestTask write = new WriteFileRequestTask(outputFile, this.getHost());
		write.send(StorageInterface.MAILBOX);
		parent.notifyMapFinish();

	}

}
