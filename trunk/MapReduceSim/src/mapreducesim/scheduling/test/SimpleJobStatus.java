package mapreducesim.scheduling.test;

import mapreducesim.scheduling.TaskPool;

public class SimpleJobStatus {

	public TaskPool tasks;
	private final String jobName;

	public SimpleJobStatus(String jobName, TaskPool tasks) {
		this.tasks = tasks;
		this.jobName = jobName;
	}

	public String getJobName() {
		return jobName;
	}
}
