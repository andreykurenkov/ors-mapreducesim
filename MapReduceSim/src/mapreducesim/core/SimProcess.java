package mapreducesim.core;

import mapreducesim.core.HostData;
import mapreducesim.core.SimMain;

import org.simgrid.msg.Host;
import org.simgrid.msg.HostFailureException;
import org.simgrid.msg.MsgException;
import org.simgrid.msg.Process;
import org.simgrid.msg.Task;

/**
 * Extension of Simgrid's process for the simulator's use. As of now no more than a storer of a constant mailbox and
 * indication of finished state. For consistency and convenience should be the class the simulator's processes extends,
 * instead of SimGrid Process.
 * 
 * @author Andrey Kurenkov
 * 
 */
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
	 * Default constructor for Processes needs to be used as part of framework. In this case, the host name is used as the
	 * mailbox.
	 * 
	 * @param host
	 * @param name
	 * @param args
	 */
	public SimProcess(Host host, String name, String[] args) {
		this(host, name, args, host.getName());
	}

	protected void elapseTime(double amountOfTIme) throws HostFailureException {
		sleep(SimMain.SIM_STEP);
		timeElapsed += SimMain.SIM_STEP;
	}

	protected Task checkTask() throws MsgException {
		Task received = Task.receive(MAILBOX);
		return received;
	}

	protected Task checkTask(double timeout) throws MsgException {
		Task received = Task.receive(MAILBOX, timeout);
		return received;
	}

	public void finish() {
		finished = true;
	}

	public boolean isFinished() {
		return finished;
	}

	public double getTimeElapsed() {
		return timeElapsed;
	}
}
