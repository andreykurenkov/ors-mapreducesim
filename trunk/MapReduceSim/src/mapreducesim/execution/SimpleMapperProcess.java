package mapreducesim.execution;

import java.util.ArrayList;

import mapreducesim.execution.tasks.WorkTask;
import mapreducesim.storage.DataLocation;
import mapreducesim.storage.File;
import mapreducesim.storage.FileBlock;
import mapreducesim.storage.FileTransferTask;
import mapreducesim.storage.FileTransferTask.ReadRequestTask;
import mapreducesim.storage.FileTransferTask.WriteRequestTask;
import mapreducesim.storage.Node;
import mapreducesim.storage.StorageProcess;

import org.simgrid.msg.Host;
import org.simgrid.msg.Msg;
import org.simgrid.msg.MsgException;
import org.simgrid.msg.Task;
import org.simgrid.msg.TimeoutException;

/**
 * Simple implementation of mapping that simulates retrieving needed files, doing map work, and writing output
 * 
 * @author Andrey Kurenkov
 * @version 1.0 Mar 13, 2013
 */
public class SimpleMapperProcess extends WorkerProcess {

	/**
	 * Constructor for SimpleMapperProcess that gives values to the instance variables corresponding to the parameters using
	 * chaining to the superclass.
	 * 
	 * @param host
	 * @param name
	 * @param mailbox
	 * @param parent
	 * @param workTask
	 */
	public SimpleMapperProcess(Host host, String name, String mailbox, TaskRunnerProcess parent, WorkTask workTask) {
		super(host, name, mailbox, parent, workTask);
	}

	/**
	 * Main method of Process - once started performs simulation for finishing entire map task.
	 */
	public void main(String[] args) throws MsgException {
		Msg.info(this.getHost().getName() + " starting " + task);
		ArrayList<FileBlock> output = new ArrayList<FileBlock>();
		Node myNode = StorageProcess.get(host.getName());
		File outFile = new File(task.getID() + " Out");
		// if this job has reduce tasks, store locally
		if (task.JOB.getOriginalReduceTasks().size() > 0)
			outFile.setParent(myNode);
		int index = 0;
		for (DataLocation dataLocation : task.NEEDED_DATA.getLocations()) {
			ReadRequestTask read = new ReadRequestTask(dataLocation, this.MAILBOX);
			read.send(StorageProcess.DEFAULT_STORAGE_MAILBOX);
			Task transferTask = null;
			try {
				transferTask = Task.receive(this.MAILBOX, 10000);
			} catch (TimeoutException e) {
				Msg.info("Transfer task receiving timed out.");
				return;
			}

			while (!(transferTask instanceof FileTransferTask)) {
				try {
					transferTask = Task.receive(this.MAILBOX, 10000);
				} catch (TimeoutException e) {
					Msg.info("Transfer task receiving timed out.");
					return;
				}
			}
			for (FileBlock block : ((FileTransferTask) transferTask).getTransferFileBlocks()) {
				this.elapseTime(TaskRunnerProcess.getTimer().estimateComputeDuration(this.getHost(), task, block.getPairs()));
				FileBlock out = new FileBlock(outFile, index++, block.getSize() / 2);
				output.add(out);
				if (task.JOB.getOriginalReduceTasks().size() == 0) {
					Msg.info("Sending write request task to storage");
					WriteRequestTask task = new WriteRequestTask(out);
					task.sendToStorage();
				} else {
					outFile.addFileBlock(out);
				}
			}
		}
		Msg.info(this.getHost().getName() + " finishing " + task);
		parent.notifyMapFinish(task, output);// TODO
	}
}
