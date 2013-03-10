package mapreducesim.scheduling;

import java.util.ArrayList;
import java.util.List;

import mapreducesim.core.ConfigurableClass;

import org.simgrid.msg.Task;

public class JobSubmission extends Task {
	public MapReduceJobSpecification jobToRun;

	public JobSubmission(MapReduceJobSpecification jobToRun) {
		this.jobToRun = jobToRun;
	}

	public static JobSubmission constructFromXML(String jobName) {
		// get the JobMaker, and use it to grab a MapReduceJobSpecification
		// object
		JobMaker jm = ConfigurableClass.instantiateFromSimConfig(JobMaker.class);
		MapReduceJobSpecification spec = jm.getJob(jobName);
		return new JobSubmission(spec);

	}

}
