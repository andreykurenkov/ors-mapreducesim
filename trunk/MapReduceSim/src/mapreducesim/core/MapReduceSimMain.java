/*
 *  Generated Main Class. Most user won't have to modify it.
 */
package mapreducesim.core;

import org.simgrid.msg.Msg;
import org.simgrid.msg.NativeException;

public class MapReduceSimMain {

	public static final long simStep = 10;

	public static void main(String[] args) throws NativeException {
		/* check usage error and initialize with defaults */
		if (args.length == 0) {
			args = new String[2];
			System.out.print("** WARNING **\nusing default values:\n"
					+ "MapReduceSim_platform.xml MapReduceSim_deployment.xml\n\n");
			args[0] = "MapReduceSim_platform.xml";
			args[1] = "MapReduceSim_deployment.xml";
		} else if (args.length != 2) {
			System.out.print("** ERROR **\n" + "Usage:\nplatform_file deployment_file\n");
			System.out.print("Example:\nMapReduceSim_platform.xml MapReduceSim_deployment.xml\n");
			System.exit(1);
		}
		/* initialize the MSG simulation. Must be done before anything else (even logging). */
		Msg.init(args);
		Msg.info("Simulation start...");
		/* construct the platform and deploy the application */
		Msg.createEnvironment(args[0]);
		Msg.deployApplication(args[1]);

		/* execute the simulation. */
		Msg.run();
		Msg.info("Simulation time:" + Msg.getClock());
	}
}
