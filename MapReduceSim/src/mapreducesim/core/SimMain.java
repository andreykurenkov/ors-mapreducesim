package mapreducesim.core;

import mapreducesim.util.SmartFile;
import mapreducesim.util.xml.XMLDocument;
import mapreducesim.util.xml.XMLElement;
import mapreducesim.util.xml.XMLNode;
import mapreducesim.util.xml.XMLParser;

import org.simgrid.msg.Msg;
import org.simgrid.msg.NativeException;

public class SimMain {
	public static final int SIM_STEP = 10;// generic time passing quantity. May be removed in future.

	private static XMLDocument config;

	public static XMLDocument getConfig() {
		return config;
	}

	public static String getConfigurationElementText(String elementName, String defaultValue) {
		if (config.getRoot() != null) {
			XMLElement child = config.getRoot().getChildByName(elementName);
			if (child != null)
				if (child.hasContentText())
					return child.getContentText();
		}
		Msg.info("No value found for " + elementName + " in config file. Using default " + defaultValue);
		return defaultValue;
	}

	public static XMLElement getConfigurationElement(String elementName) {
		if (config.getRoot() != null)
			return config.getRoot().getChildByName(elementName);
		return null;
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
		/* Creates the configuration to be used by the rest of the simulation */
		config = new XMLDocument();
		if (args.length > 2) {
			SmartFile file = new SmartFile(args[2]);
			if (file.exists()) {
				XMLNode node = XMLParser.parse(file.read());
				if (node instanceof XMLElement) {
					config.setRoot((XMLElement) node);
				} else {
					System.out.print("** ERROR **\n" + "XML config file not \n");
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
		Msg.info("Simulation time:" + Msg.getClock());
	}
}
