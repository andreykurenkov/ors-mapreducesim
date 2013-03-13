package mapreducesim.core;

import mapreducesim.util.SmartFile;
import mapreducesim.util.xml.XMLDocument;
import mapreducesim.util.xml.XMLElement;
import mapreducesim.util.xml.XMLNode;
import mapreducesim.util.xml.XMLParser;

import org.simgrid.msg.Msg;
import org.simgrid.msg.NativeException;

/**
 * The main class of the simulation. This is largely the same as SimGrid's sample main class, but in addition to initialising
 * SimGrid it also loads the Simulation's own configuration XML file as the third argument.
 * 
 * The loaded configuration is static and is accessible everywhere within the simulation through SimConfig. It is encouraged
 * to access the configuration from SimConfig through its helper methods.
 * 
 * Users are allowed to not provide a configuration XML file. In this case, the configuration file is an XMLDocument with a
 * null root. SimConfig accounts for that possibility, and so should be used.
 * 
 * @author Andrey Kurenkov
 * @version 1.0 Mar 1, 2013
 */
public class SimMain {
	// the configuration of the current Simulation, to be used in static intialization blocks for init and during the
	// simulation
	private static XMLDocument config;

	/**
	 * Simple getter for config
	 * 
	 * @return config
	 */
	protected static XMLDocument getConfig() {
		return config;
	}

	/**
	 * Entry point into simulation
	 * 
	 * @param args
	 *            important parameters indicating the names/paths to XML files for the platform, deployment, and optionally
	 *            configuration XML files for this simulation (in that order). If args is null, default values are attempted.
	 *            If args is less than 2 or greater than 4, an input format message is printed and the program exits.
	 * @throws NativeException
	 *             thrown by SimGrid in case of native error
	 */
	public static void main(String[] args) throws NativeException {
		/* check usage error and initialize with defaults */
		if (args.length == 0) {
			args = new String[3];
			System.out.print("** WARNING **\nusing default values:\n"
					+ "MapReduceSim_platform.xml MapReduceSim_deployment.xml MapReduceSim_config.xml\n\n");
			args[0] = "MapReduceSim_platform.xml";
			args[1] = "MapReduceSim_deployment.xml";
			args[2] = "MapReduceSim_config.xml";
		} else if (args.length < 2 || args.length > 4) {
			System.out.print("** ERROR **\n" + "Usage:\nplatform_file deployment_file config_file\n");
			System.out
					.print("Example:\nMapReduceSim_platform.xml MapReduceSim_deployment.xml  (optional) MapReduceSim_config.xml\n");
			System.exit(1);
		}
		/* initialize the MSG simulation. Must be done before anything else (even logging). */
		Msg.init(args);
		Msg.info("Simulation start...");
		/* Creates the configuration to be used by the rest of the simulation */
		config = new XMLDocument();
		// read in configuration file
		if (args.length > 2) {
			SmartFile file = new SmartFile(args[2]);
			if (file.exists()) {
				XMLDocument parse = XMLDocument.parseDocument(file);
				if (parse != null) {
					config = parse;
				} else {
					System.out.print("** ERROR **\n" + "XML config file " + args[2] + " not found or badly formatted:\n"
							+ file.read());
					System.exit(0);
				}
			} else {
				System.err.println("Configuration file at " + file.getAbsolutePath() + " does not exist.");
			}

		}

		/* construct the platform and deploy the application */
		Msg.createEnvironment(args[0]);
		Msg.deployApplication(args[1]);

		/* execute the simulation. */
		Msg.run();
		double clock = Msg.getClock();
		Msg.info("Simulation time:" + clock);
	}
}
