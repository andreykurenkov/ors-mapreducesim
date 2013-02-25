/*
 *  Generated Main Class. Most user won't have to modify it.
 */
package mapreducesim.core;

import mapreducesim.util.SmartFile;
import mapreducesim.util.xml.XMLDocument;
import mapreducesim.util.xml.XMLElement;
import mapreducesim.util.xml.XMLNode;
import mapreducesim.util.xml.XMLParser;

import org.simgrid.msg.Msg;
import org.simgrid.msg.NativeException;

public class MapReduceSimMain {
	public static final int SIM_STEP = 10;// generic time passing quantity. May be removed in future.

	private static XMLDocument config;

	public static XMLDocument getConfig() {
		return config;
	}

	public static void main(String[] args) throws NativeException {
		/* check usage error and initialize with defaults */
		if (args.length == 0) {
			args = new String[3];
			System.out.print("** WARNING **\nusing default values:\n"
					+ "MapReduceSim_platform.xml MapReduceSim_deployment.xml MapReduceSim_config.xml\n\n");
			args[0] = "MapReduceSim_platform.xml";
			args[1] = "MapReduceSim_deployment.xml";
			args[2] = "MapReduceSim_config.xml";
		} else if (args.length < 2) {
			System.out.print("** ERROR **\n" + "Usage:\nplatform_file deployment_file config_file\n");
			System.out
					.print("Example:\nMapReduceSim_platform.xml MapReduceSim_deployment.xml  (optional) MapReduceSim_config.xml\n");
			System.exit(1);
		}
		/* initialize the MSG simulation. Must be done before anything else (even logging). */
		Msg.init(args);
		Msg.info("Simulation start...");
		if (args.length > 2) {
			SmartFile file = new SmartFile(args[2]);
			XMLNode node = XMLParser.parse(file.read());
			config = new XMLDocument();
			if (node instanceof XMLElement) {
				config.setRoot((XMLElement) node);
			} else {
				System.out.print("** ERROR **\n" + "XML config file not \n");
				System.exit(0);
			}
		}

		/* construct the platform and deploy the application */
		Msg.createEnvironment(args[0]);
		Msg.deployApplication(args[1]);

		/* execute the simulation. */
		Msg.run();
		Msg.info("Simulation time:" + Msg.getClock());
	}
}
