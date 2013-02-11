package mapreducesim.scheduling;

import org.simgrid.msg.Host;
import org.simgrid.msg.Msg;
import org.simgrid.msg.*;

public class FIFOScheduler extends org.simgrid.msg.Process{
	public FIFOScheduler(Host host, String name, String[] args) {
		super(host, name, args);
	}
	
	@Override
	public void main(String[] arg0) throws MsgException {
		Msg.info("Jobtracker waiting for tasks on mailbox '"+getHost().getName()+"'");
		//receive first task
		Task taskReceived = Task.receive(getHost().getName());

		while (true){
			if (taskReceived instanceof JobSubmission){
				JobSubmission js = (JobSubmission)taskReceived;
				Msg.info("Job tracker received job submission: "+js.jobName);
			}
			else if (taskReceived instanceof Heartbeat){
				Msg.info("Job tracker received heartbeat from "+taskReceived.getSource().getName());
			}else {
				Msg.info("Job tracker ignoring unknown task received: "+taskReceived);
			}
			//continue receiving tasks, or exit if no more tasks received within 0.5s
			try {
				taskReceived = Task.receive(getHost().getName(),0.5);
				
			}catch (TimeoutException te){
				Msg.info("No more messages received by the scheduler.  Exiting.");
				break;
			}
		}
	}
}
