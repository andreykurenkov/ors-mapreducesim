package mapreducesim.execution;

import mapreducesim.core.SimProcess;
import mapreducesim.tasks.WorkTask;

import org.simgrid.msg.Host;
import org.simgrid.msg.MsgException;
import org.simgrid.msg.Process;

public class WorkerProcess extends SimProcess {
	private boolean working;

	public WorkerProcess(Host host, String name) {
		super(host, name);
	}

	public void handleWorkTask(WorkTask task) throws MsgException {
		if (!working) {
			long timeToWork = task.calculateWorkLength(this);
			working = true;
			long worked = 0;
			while (worked < timeToWork) {
				step();
				worked += 10;
			}
			working = false;
		}
	}

	public boolean isWorking() {
		return working;
	}
}
