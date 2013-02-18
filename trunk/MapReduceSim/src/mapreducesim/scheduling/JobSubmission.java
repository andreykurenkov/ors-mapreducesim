package mapreducesim.scheduling;

import org.simgrid.msg.Task;

public class JobSubmission extends Task{
	public final String jobName;
	public final int numMapTasks;
	public final int numReduceTasks;
	public JobSubmission(String jobName,int numMapTasks, int numReduceTasks){
		this.jobName = jobName;
		this.numMapTasks = numMapTasks;
		this.numReduceTasks = numReduceTasks;
	}
	
	public String getJobName(){
		return this.jobName;
	}
}
