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

	/**
	 * Default constructor for Processes needs to be used as part of framework.
	 * 
	 * @param host
	 * @param name
	 * @param args
	 */
	public SimProcess(Host host, String name, String[] args, String mailbox) {
		super(host, name, args);
		MAILBOX = mailbox;
	}

	/**
	 * Default constructor for Processes needs to be used as part of framework.
	 * 
	 * @param host
	 * @param name
	 * @param args
	 */
	public SimProcess(Host host, String name, String[] args) {
		this(host, name, args, host.getName());
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

	public double getTimeElapsed() {
		return timeElapsed;
	}
}
