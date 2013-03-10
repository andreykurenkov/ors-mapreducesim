package mapreducesim.scheduling.test;

import mapreducesim.core.SimProcess;
import mapreducesim.scheduling.JobSubmission;

import org.simgrid.msg.Host;
import org.simgrid.msg.Msg;
import org.simgrid.msg.MsgException;

public class JobSubmitterProcess extends SimProcess {

	public JobSubmitterProcess(Host host, String name, String[] args) {
		super(host, name, args, "JobSubmitter");
	}

	@Override
	public void main(String[] arg0) throws MsgException {

		// first arg is the name of job to submit
		String jobName = arg0[0];

		JobSubmission js = JobSubmission.constructFromXML(jobName);

		Msg.info("Job submitter is submitting job: " + js.jobToRun);

		js.send("JobTracker");
	}

}
