package mapreducesim.execution;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mapreducesim.core.SimMain;
import mapreducesim.execution.tasks.WorkTask;
import mapreducesim.storage.KeyValuePair;

import org.simgrid.msg.Host;
import org.simgrid.msg.Msg;
import org.simgrid.msg.MsgException;

public class SimpleReduceProcess extends WorkerProcess {

	public final double failureRate = 0.001;
	private ShuffleSorter sorter;

	public SimpleReduceProcess(Host host, String name, String mailbox, TaskRunnerProcess parent, WorkTask task,
			ShuffleSorter sorter) {
		super(host, name, mailbox, parent, task);
		this.sorter = sorter;
	}

	@Override
	public void main(String[] arg0) throws MsgException {
		Msg.info(this.getHost().getName() + " starting WorkTask" + task + " at " + this.getTimeElapsed());

		Map<String, List<KeyValuePair>> pairs = sorter.doShuffleSort(task.NEEDED_DATA, this);
		for (Entry<String, List<KeyValuePair>> entry : pairs.entrySet()) {
			for (KeyValuePair pair : entry.getValue()) {
				this.waitFor(TaskRunnerProcess.getTimer().estimateComputeDuration(this.getHost(), task, pair));
				task.OUT.collectOutput(pair);
			}
		}
		task.OUT.writeOutput();
		Msg.info(this.getHost().getName() + " finishing WorkTask" + task + " at " + this.getTimeElapsed());
		parent.notifyReduceFinish();
		this.suspend();
	}

}
