package mapreducesim.scheduling;

import org.simgrid.msg.Task;

public class JobSubmission extends Task{
	String jobName;
	public JobSubmission(String jobName){
		this.jobName = jobName;
	}
}
