package mapreducesim.scheduling;

public class JobStatus {

	public TaskPool tasks;
	private final String jobName;
	public JobStatus(String jobName,TaskPool tasks){
		this.tasks = tasks;
		this.jobName = jobName;
	}
	public String getJobName(){
		return jobName;
	}
}
