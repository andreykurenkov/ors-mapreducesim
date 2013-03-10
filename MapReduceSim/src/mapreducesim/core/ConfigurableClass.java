package mapreducesim.core;

import org.simgrid.msg.Msg;

import mapreducesim.execution.SimpleWorkTaskTimer;
import mapreducesim.execution.WorkTaskTimer;
import mapreducesim.scheduling.SimpleFileSplitter;
import mapreducesim.scheduling.SimpleJobMaker;
import mapreducesim.util.ExceptionUtil;
import mapreducesim.util.ReflectionUtil;
import mapreducesim.util.xml.XMLDocument;
import mapreducesim.util.xml.XMLElement;

/**
 * The top-level class for the set of classes whose instances should be loaded
 * from the configuration xml file. This class enables reflexive instantiation
 * (instantiation given only the String name of the class) of different classes
 * within the simulation. This vastly increases the flexibility of simulation
 * since new implementation of ConfigurableClasses can be written and easily
 * swapped into the runtime of the simulation by specifying thier name in the
 * configuration XML file.
 * 
 * For any subclass of this class(which should typically be abstract), a
 * configuration file should have a child of the root XMLElement with the name
 * of the subclass and an CLASS_ATTRIBUTE_KEY attribute whose value is the
 * non-abstract subclass of the class of that XMLElement. A configuration file
 * may omit specifying the implementation of any configurable class, in which
 * case a default implementation will be used.
 * 
 * Instantiation of Configurable classes should be done using the
 * instantiateFromSimConfig, and all subclasses of this class must at minimum
 * have a constructor that takes an XMLElement. This XMLElement is meant to
 * store all needed arguments/values to be loaded by the configurable file after
 * reflextive instantiation.
 * 
 * @author Andrey Kurenkov
 * @version 1.0
 */
public abstract class ConfigurableClass {
	protected XMLElement input;
	public static final String CLASS_ATTRIBUTE_KEY = "classname";

	public static final String JOB_MAKER = "JobMaker";
	public static final String FILE_SPLITTER = "FileSplitter";

	/**
	 * Does nothing more than store the XMLElement. Subclasses should then use
	 * the element to instantiate or give values to any instance variables.
	 * 
	 * @param input
	 *            the XMLElement within the config XML file specified for this
	 *            ConfigurableClass
	 */
	public ConfigurableClass(XMLElement input) {
		this.input = input;
	}

	/**
	 * Method that handles reflexive instantiation from MapReduceSimMain's
	 * configuration file given the name of the node within the XML file for the
	 * desired subclass of ConfigurableFile.
	 * 
	 * @param nameOfNode
	 *            the name of the needed XMLElement
	 * @param classType
	 *            the type of the class to be instantiated (same as [class
	 *            name].class)
	 * @param defaultInstance
	 *            The default instance to be used in case no specification is
	 *            given in the Configuration file.
	 * @return An instance of the wanted class, either reflexively instantiated
	 *         or the default.
	 * @todo throw exception on invalid input/handle reflection errors better?
	 */
	public static <A extends ConfigurableClass> A instantiateFromSimConfig(
			String nameOfNode, Class<A> classType, A defaultInstance) {
		XMLDocument config = SimMain.getConfig();
		A toReturn = null;
		XMLElement element = null;
		try {
			if (config != null)
				element = config.getRoot().getChildByName(nameOfNode);
			if (element != null) {
				String nameOfA = element.getAttributeValue(CLASS_ATTRIBUTE_KEY);
				toReturn = ReflectionUtil.attemptConstructorCallAndCast(
						classType, Class.forName(nameOfA), nameOfA);
			} else {
				Msg.info("No config for " + classType.getSimpleName()
						+ " found. Using default of "
						+ defaultInstance.getClass().getSimpleName());
				toReturn = defaultInstance;
			}

		} catch (Exception e) {
			Msg.info(classType.getSimpleName()
					+ " loading failed. Error below:\n"
					+ ExceptionUtil.getStackTrace(e));
		}
		return toReturn;
	}

	/**
	 * The prefered form of instantiateFromSimConfig. Same as
	 * instantiateFromSimConfig except for using classType.getSimpleName as the
	 * name of the node to look in.
	 * 
	 * @param classType
	 *            the type of the class to be instantiated (same as [class
	 *            name].class)
	 * @param defaultInstance
	 *            The default instance to be used in case no specification is
	 *            given in the Configuration file.
	 * @return An instance of the wanted class, either reflexively instantiated
	 *         or the default.
	 */
	public static <A extends ConfigurableClass> A instantiateFromSimConfig(
			Class<A> classType, A defaultInstance) {
		return instantiateFromSimConfig(classType.getSimpleName(), classType,
				defaultInstance);
	}

	/**
	 * Allows instantiation of a configureable class given just the name of the
	 * object wanted, e.g, calling it with "FileSplitter" will return a custom
	 * FileSplitter object if defined in the xml, or just the default if it
	 * wasn't specified in the config xml.
	 * 
	 * @param <A>
	 * @param nameOfXMLNode
	 *            The name of the superclass of the object desired
	 * @return An instance of that object
	 */
	public static <A extends ConfigurableClass> A instantiateFromSimConfig(
			String nameOfXMLNode) {
		// see if the config has this node
		if (SimConfig.CONFIG.getRoot().getChildrenByName(nameOfXMLNode).size() == 0) {
			// the config does not have this node, use default
			return getDefaultInstance(nameOfXMLNode);
		} else {
			throw new RuntimeException(
					"Non-default instances not supported yet");
		}
	}

	/**
	 * Returns a default instance for a particular configurable class
	 * 
	 * @param <A>
	 * @param xmlNodeName
	 * @return
	 */
	public static <A extends ConfigurableClass> A getDefaultInstance(
			String xmlNodeName) {

		XMLElement xmlEle = SimConfig.CONFIG.getRoot().getChildByName(
				xmlNodeName);

		if (xmlNodeName.equalsIgnoreCase(FILE_SPLITTER)) {
			return (A) new SimpleFileSplitter();
		}
		if (xmlNodeName.equalsIgnoreCase(JOB_MAKER)) {
			return (A) new SimpleJobMaker(xmlEle);
		}

		throw new RuntimeException(
				"No default instance available for xmlNodeName '" + xmlNodeName
						+ "'");

	}
}
