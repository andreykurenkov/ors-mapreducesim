package mapreducesim.execution;

import mapreducesim.execution.tasks.WorkTask;
import mapreducesim.storage.DataLocation;
import mapreducesim.storage.File;
import mapreducesim.storage.FileBlockLocation;
import mapreducesim.storage.FileTransferTask;
import mapreducesim.storage.FileTransferTask.ReadRequestTask;
import mapreducesim.storage.FileTransferTask.WriteRequestTask;
import mapreducesim.storage.StorageProcess;

import org.simgrid.msg.Host;
import org.simgrid.msg.Msg;
import org.simgrid.msg.MsgException;
import org.simgrid.msg.Task;

//TODO: handle excepptions
public class SimpleMapperProcess extends WorkerProcess {
	public final double failureRate = 0.001;

	public SimpleMapperProcess(Host host, String name, String mailbox, TaskRunnerProcess parent, WorkTask workTask) {
		super(host, name, mailbox, parent, workTask);
	}

	@Override
	public void main(String[] args) throws MsgException {
		// read needed files
		for (DataLocation dataLocation : task.NEEDED_DATA.getLocations()) {
			ReadRequestTask read = new ReadRequestTask(dataLocation, MAILBOX);
			read.send(StorageProcess.STORAGE_MAILBOX);
			Task transferTask = Task.receive(this.MAILBOX);
			while (!(transferTask instanceof FileTransferTask)) {
				transferTask = Task.receive(this.MAILBOX);
			}
		}
		// Do map task
		Msg.info(this.getHost().getName() + " starting " + task + " for " + task.getComputeDuration() + " expected time.");
		task.execute();
		Msg.info(this.getHost().getName() + " finishing " + task);
		// Write output
		File outputFile = new File(null, "null");
		WriteRequestTask write = new WriteRequestTask(outputFile, this.getHost());
		// write.send(StorageProcess.STORAGE_MAILBOX);
		parent.notifyMapFinish();

	}

}
