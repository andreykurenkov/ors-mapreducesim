package mapreducesim.scheduling.test;

import mapreducesim.scheduling.JobSubmission;

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
		//second arg is the number of map tasks
		int numMap = Integer.parseInt(arg0[1]);
		//third arg is the number of reduce tasks
		int numReduce = Integer.parseInt(arg0[2]);
		JobSubmission js = new JobSubmission(jobName,numMap,numReduce);
		
		js.send("JobTracker");
	}

}
