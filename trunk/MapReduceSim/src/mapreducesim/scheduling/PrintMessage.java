package mapreducesim.scheduling;

import org.simgrid.msg.Host;
import org.simgrid.msg.Msg;
import org.simgrid.msg.MsgException;
import org.simgrid.msg.Process;

public class PrintMessage extends Process{

	public PrintMessage(Host host, String name, String[] args) {
		super(host, name, args);
	}
	
	@Override
	public void main(String[] arg0) throws MsgException {
		// TODO Auto-generated method stub
		Msg.info(arg0[0]);
	}

}
