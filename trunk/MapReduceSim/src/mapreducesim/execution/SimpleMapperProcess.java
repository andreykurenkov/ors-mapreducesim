package mapreducesim.execution;

import mapreducesim.core.MapReduceSimMain;
import mapreducesim.core.SimFile.SimFileLocation;
import mapreducesim.core.SimProcess;
import mapreducesim.tasks.ReadTask;
import mapreducesim.tasks.SimpleMapTask;
import mapreducesim.tasks.WorkTask;
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

	public SimpleMapperProcess(Host host, String name, TaskTrackerProcess parent, SimpleMapTask task) {
		super(host, name, parent, task);
	}

	@Override
	public void main(String[] args) throws MsgException {
		for (SimFileLocation neededFile : task.NEEDED_FILES) {
			ReadTask read = new ReadTask(neededFile);
			read.send(StorageInterface.MAILBOX);
			while (!read.isReadDone()) {
				this.step();
			}
		}
		Msg.info(this.getHost().getName() + " starting Map" + task + " at " + this.getTimeElapsed());
		long timeToWork = task.WORK_AMOUNT / (int) this.getHost().getSpeed();
		while (timeToWork > 0) {
			timeToWork -= MapReduceSimMain.SIM_STEP;
		}
		Msg.info(this.getHost().getName() + " finishing Map" + task + " at " + this.getTimeElapsed());
		this.kill();
	}

}
