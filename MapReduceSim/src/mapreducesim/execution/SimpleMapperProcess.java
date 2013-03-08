package mapreducesim.execution;

import mapreducesim.execution.tasks.WorkTask;
import mapreducesim.storage.DataLocation;
import mapreducesim.storage.File;
import mapreducesim.storage.FileBlock;
import mapreducesim.storage.FileBlockLocation;
import mapreducesim.storage.FileTransferTask;
import mapreducesim.storage.FileTransferTask.ReadRequestTask;
import mapreducesim.storage.FileTransferTask.WriteRequestTask;
import mapreducesim.storage.KeyValuePair;
import mapreducesim.storage.StorageProcess;

import org.simgrid.msg.Host;
import org.simgrid.msg.Msg;
import org.simgrid.msg.MsgException;
import org.simgrid.msg.Task;

//TODO: handle excepptions
public class SimpleMapperProcess extends WorkerProcess {
	public final double failureRate = 0.001;
	private boolean simulatePerPair = true;

	public SimpleMapperProcess(Host host, String name, String mailbox, TaskRunnerProcess parent, WorkTask workTask) {
		super(host, name, mailbox, parent, workTask);
	}

	protected void setSimulatePerPair(boolean setTo) {
		simulatePerPair = setTo;
	}

	@Override
	public void main(String[] args) throws MsgException {
		Msg.info(this.getHost().getName() + " starting " + task + " for " + task.getComputeDuration() + " expected time.");

		for (DataLocation dataLocation : task.NEEDED_DATA.getLocations()) {
			ReadRequestTask read = new ReadRequestTask(dataLocation, MAILBOX);
			read.send(StorageProcess.STORAGE_MAILBOX);
			Task transferTask = Task.receive(this.MAILBOX);
			while (!(transferTask instanceof FileTransferTask)) {
				transferTask = Task.receive(this.MAILBOX);
			}
			if (simulatePerPair) {
				for (FileBlock block : ((FileTransferTask) transferTask).getTransferData()) {
					for (KeyValuePair pair : block.getPairs()) {
						task.OUT.collectOutput(pair);
						this.waitFor(TaskRunnerProcess.getTimer().estimateComputeDuration(this.getHost(), task, pair));
					}
				}
			}
		}
		if (!simulatePerPair) {
			task.execute();
		}
		Msg.info(this.getHost().getName() + " finishing " + task);
		parent.notifyMapFinish();
		this.suspend();
	}
}
