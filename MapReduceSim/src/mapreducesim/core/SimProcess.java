package mapreducesim.core;

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
	// this is meant to store how much time was spent working in any Process for easy retrieval at end.
	// However, Task.execute should be avoided if this is to be up to date
	protected double timeSlept;
	public final String MAILBOX;

	/**
	 * Default constructor for Processes needs to be used as part of framework. Also takes in a mailbox to set MAILBOX equal
	 * to.
	 * 
	 * @param host
	 * @param name
	 * @param args
	 * @param mailbox
	 */
	public SimProcess(Host host, String name, String[] args, String mailbox) {
		super(host, name, args);
		MAILBOX = mailbox;
	}

	/**
	 * Default constructor for Processes needs to be used as part of framework. In this case, the host name is used as the
	 * mailbox with host.getName().
	 * 
	 * @param host
	 * @param name
	 * @param args
	 */
	public SimProcess(Host host, String name, String[] args) {
		this(host, name, args, host.getName());
	}

	/**
	 * Sleeps for amountOfTime and add that to timeElapsed
	 * 
	 * @param amountOfTime
	 * @throws HostFailureException
	 */
	protected void elapseTime(double amountOfTime) throws HostFailureException {
		waitFor(amountOfTime);
		timeSlept += amountOfTime;
	}

	/**
	 * Checks to receive an Item to MAILBOX, blocks until a task is received.
	 * 
	 * @return task from Task.receive(MAILBOX), blocks until done
	 * @throws MsgException
	 */
	protected Task checkTask() throws MsgException {
		Task received = Task.receive(MAILBOX);
		return received;
	}

	/**
	 * Checks to receive an Item to MAILBOX, blocks until a task is received or timeout is expired.
	 * 
	 * @param timeout
	 *            limit for blocking time
	 * @return task from Task.receive(MAILBOX,timeout), blocks until done or timeout
	 * @throws MsgException
	 *             thrown if timeout occurs
	 */
	protected Task checkTask(double timeout) throws MsgException {
		Task received = Task.receive(MAILBOX, timeout);
		return received;
	}

	/**
	 * Sets finished equal to true.
	 */
	public void finish() {
		finished = true;
	}

	/**
	 * Getter for finished
	 * 
	 * @return finished
	 */
	public boolean isFinished() {
		return finished;
	}

	/**
	 * Getter for timeElapsed
	 * 
	 * @return timeElapsed
	 */
	public double getTimeElapsed() {
		return timeSlept;
	}
}
