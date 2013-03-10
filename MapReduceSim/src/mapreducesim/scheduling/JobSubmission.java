package mapreducesim.scheduling;

import java.util.ArrayList;
import java.util.List;

import org.simgrid.msg.Task;

public class JobSubmission extends Task {
	public MapReduceJobSpecification jobToRun;

	public JobSubmission(MapReduceJobSpecification jobToRun) {
		this.jobToRun = jobToRun;
	}

	public static JobSubmission constructFromXML(String jobName) {
		return new JobSubmission(MapReduceJobSpecification
				.constructFromXML(jobName));
	}

}
