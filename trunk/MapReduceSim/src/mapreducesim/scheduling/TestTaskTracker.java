package mapreducesim.scheduling;

import org.simgrid.msg.Host;
import org.simgrid.msg.Msg;
import org.simgrid.msg.MsgException;
import org.simgrid.msg.Process;

public class TestTaskTracker extends Process {

	static int numHeartbeatsToSend = 3;

	public TestTaskTracker(Host host, String name, String[] args) {
		super(host, name, args);
	}

	@Override
	public void main(String[] arg0) throws MsgException {
		// TODO Auto-generated method stub
		Msg.info("Task tracker booting up on host: " + getHost().getName());

	}

}
