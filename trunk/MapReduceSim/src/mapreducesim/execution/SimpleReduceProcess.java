package mapreducesim.execution;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mapreducesim.core.SimMain;
import mapreducesim.execution.tasks.WorkTask;
import mapreducesim.storage.FileBlock;
import mapreducesim.storage.KeyValuePairs;
import mapreducesim.storage.FileTransferTask.WriteRequestTask;

import org.simgrid.msg.Host;
import org.simgrid.msg.Msg;
import org.simgrid.msg.MsgException;

/**
 * Simple simulation of reduce operations
 * 
 * @author Andrey Kurenkov
 * @version 1.0 Mar 13, 2013
 */
public class SimpleReduceProcess extends WorkerProcess {

	public final double failureRate = 0.001;
	private ShuffleSorter sorter;

	/**
	 * Constructor for SimpleReduceProcess that gives values to the instance variables corresponding to the parameters.
	 * 
	 * @param host
	 * @param name
	 * @param mailbox
	 * @param parent
	 * @param task
	 * @param sorter
	 */
	public SimpleReduceProcess(Host host, String name, String mailbox, TaskRunnerProcess parent, WorkTask task,
			ShuffleSorter sorter) {
		super(host, name, mailbox, parent, task);
		this.sorter = sorter;
	}

	/**
	 * Main method of Process - once started performs simulation for finishing entire reduce task.
	 */
	public void main(String[] arg0) throws MsgException {
		Msg.info(this.getHost().getName() + " starting WorkTask" + task + " at " + this.getTimeElapsed());
		ArrayList<FileBlock> output = new ArrayList<FileBlock>();
		List<KeyValuePairs> pairs = sorter.doShuffleSort(task.NEEDED_DATA, this);
		for (KeyValuePairs pair : pairs) {
			this.elapseTime(TaskRunnerProcess.getTimer().estimateComputeDuration(this.getHost(), task, pair));
			FileBlock out = new FileBlock(null, 0, pair.getTotalSize() / 3);// TODO: get compression rate?
			output.add(out);
			WriteRequestTask task = new WriteRequestTask(out);
			task.sendToStorage();
		}
		Msg.info(this.getHost().getName() + " finishing WorkTask" + task + " at " + this.getTimeElapsed());
		parent.notifyReduceFinish(task, output);
	}

}
