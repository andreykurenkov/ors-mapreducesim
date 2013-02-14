package mapreducesim.core;

import mapreducesim.core.HostData;
import mapreducesim.core.MapReduceSimMain;

import org.simgrid.msg.Host;
import org.simgrid.msg.HostFailureException;
import org.simgrid.msg.MsgException;
import org.simgrid.msg.Process;
import org.simgrid.msg.Task;

public abstract class SimProcess extends Process {

	protected boolean finished;
	// this is meant to store how much time was spent working in any Process
	protected double timeElapsed;
	public final String MAILBOX;

	public SimProcess() {
		MAILBOX = host.getName();
		this.getHost().setData(new HostData());
	}

	public SimProcess(Host host, String name) {
		super(host, name);
		MAILBOX = host.getName();
	}

	protected void elapseTime(double amountOfTIme) throws HostFailureException {
		sleep(MapReduceSimMain.SIM_STEP);
		timeElapsed += MapReduceSimMain.SIM_STEP;
	}

	protected Task checkTask() throws MsgException {
		Task received = Task.receive(MAILBOX);
		return received;
	}

	public void kill() {
		finished = true;
		super.kill();
	}

	public boolean isFinished() {
		return finished;
	}

	public int getTimeElapsed() {
		return timeElapsed;
	}
}
