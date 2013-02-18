package mapreducesim.scheduling;

import org.simgrid.msg.Host;
import org.simgrid.msg.MsgException;

public class JobSubmitter extends org.simgrid.msg.Process{

	public JobSubmitter(Host host, String name, String[] args) {
		super(host, name, args);
	}
	
	@Override
	public void main(String[] arg0) throws MsgException {
	
		//first arg is the name of job to submit
		String jobName = arg0[0];
		JobSubmission js = new JobSubmission(jobName);
		
		js.send("JobTracker");
	}

}
