package mapreducesim.core;

import java.util.HashMap;

import mapreducesim.scheduling.FileSplitter;
import mapreducesim.scheduling.JobMaker;
import mapreducesim.scheduling.SimpleFileSplitter;
import mapreducesim.scheduling.SimpleJobMaker;
import mapreducesim.util.ExceptionUtil;
import mapreducesim.util.ReflectionUtil;
import mapreducesim.util.xml.XMLDocument;
import mapreducesim.util.xml.XMLElement;

import org.simgrid.msg.Msg;

/**
 * The top-level class for the set of classes whose instances should be loaded from the configuration xml file. This class
 * enables reflexive instantiation (instantiation given only the String name of the class) of different classes within the
 * simulation. This vastly increases the flexibility of simulation since new implementation of ConfigurableClasses can be
 * written and easily swapped into the runtime of the simulation by specifying thier name in the configuration XML file.
 * 
 * For any subclass of this class(which should typically be abstract), a configuration file should have a child of the root
 * XMLElement with the name of the subclass and an CLASS_ATTRIBUTE_KEY attribute whose value is the non-abstract subclass of
 * the class of that XMLElement. A configuration file may omit specifying the implementation of any configurable class, in
 * which case a default implementation will be used. It is up to each subclass of Configurable class to statically call
 * addDefaultInstance
 * 
 * Instantiation of Configurable classes should be done using the instantiateFromSimConfig, and all subclasses of this class
 * must at minimum have a constructor that takes an XMLElement. This XMLElement is meant to store all needed arguments/values
 * to be loaded by the configurable file after reflextive instantiation.
 * 
 * @author Andrey Kurenkov
 * @version 1.0
 */
public abstract class ConfigurableClass {
	protected XMLElement input;
	public static final String CLASS_ATTRIBUTE_KEY = "classname";

	public static final HashMap<Class<? extends ConfigurableClass>, ConfigurableClass> defaults;

	// creates storage for defaults
	static {
		defaults = new HashMap<Class<? extends ConfigurableClass>, ConfigurableClass>();
		defaults.put(JobMaker.class, new SimpleJobMaker(null));
		defaults.put(FileSplitter.class, new SimpleFileSplitter(null));
	}

	/**
	 * Adds the given class to the map containing default instances of ConfigurabeClass subclasses.
	 * 
	 * 
	 * @param forClass
	 * @param defaultInstance
	 */
	public static <A extends ConfigurableClass> void addDefaultInstance(Class<A> forClass, A defaultInstance) {
		defaults.put(forClass, defaultInstance);
	}

	/**
	 * Returns a default instance for a particular configurable class
	 * 
	 * @param xmlNodeName
	 * @return
	 */
	public static <A extends ConfigurableClass> A getDefaultInstance(Class<A> forClass, XMLElement element) {
		try {
			return (A) defaults.get(forClass);
		} catch (Exception e) {
			throw new RuntimeException("Failed to load a default instance for class: " + forClass.getSimpleName(), e);
		}
		// return forClass.cast(defaults.get(forClass));
	}

	/**
	 * Does nothing more than store the XMLElement. Subclasses should then use the element to instantiate or give values to
	 * any instance variables.
	 * 
	 * @param input
	 *            the XMLElement within the config XML file specified for this ConfigurableClass
	 */
	public ConfigurableClass(XMLElement input) {
		this.input = input;
	}

	/**
	 * Method that handles reflexive instantiation from MapReduceSimMain's configuration file given the desired subclass of
	 * configurable file, a default value, and the XMLElement to supply to the new class. This method should be used in cases
	 * where the node containing the configurable class is not a direct child of the root node - otherwise,
	 * instantiateFromSimConfig should be used.
	 * 
	 * @param classType
	 *            the type of the class to be instantiated (same as [class name].class)
	 * @param defaultInstance
	 *            The default instance to be used in case no specification is given in the Configuration file.
	 * @return An instance of the wanted class, either reflexively instantiated or the default.
	 */
	public static <A extends ConfigurableClass> A instantiateFromXMLElement(Class<A> classType, A defaultInstance,
			XMLElement element) {
		A toReturn = null;
		try {
			if (element != null) {
				String nameOfA = element.getAttributeValue(CLASS_ATTRIBUTE_KEY);
				if (nameOfA != null) {
				// with XMLElement param
				toReturn = ReflectionUtil.attemptConstructorCallAndCast(classType, Class.forName(nameOfA), element);
				// if did not work, with no param
				if (toReturn == null)
					toReturn = ReflectionUtil.attemptConstructorCallAndCast(classType, Class.forName(nameOfA));
				if (toReturn == null) {
					toReturn = defaultInstance;
					Msg.info(classType.getSimpleName() + " loading failed. Using default");
				}

				} else {
					Msg.info("No " + CLASS_ATTRIBUTE_KEY + " for " + classType.getSimpleName() + " given. Using default of "
							+ defaultInstance.getClass().getSimpleName());
					toReturn = defaultInstance;
				}
			} else {
				Msg.info("Null XMLElement for " + classType.getSimpleName() + " given. Using default of "
						+ defaultInstance.getClass().getSimpleName());
				toReturn = defaultInstance;
			}
		} catch (Exception e) {
			Msg.info(classType.getSimpleName() + " loading failed. Error below:\n" + ExceptionUtil.getStackTrace(e));
		}
		return toReturn;
	}

	/**
	 * Method that handles reflexive instantiation from MapReduceSimMain's configuration file given the name of the node
	 * within the XML file for the desired subclass of ConfigurableFile. It is preffered to use instantiateFromSimConfig
	 * without a provided nameOfNode and write the XML config to have the name of the node be the name of the abstract class
	 * needed.
	 * 
	 * @param nameOfNode
	 *            the name of the needed XMLElement
	 * @param classType
	 *            the type of the class to be instantiated (same as [class name].class)
	 * @param defaultInstance
	 *            The default instance to be used in case no specification is given in the Configuration file.
	 * @return An instance of the wanted class, either reflexively instantiated or the default.
	 */
	public static <A extends ConfigurableClass> A instantiateFromSimConfig(String nameOfNode, Class<A> classType,
			A defaultInstance) {
		XMLDocument config = SimMain.getConfig();
		A toReturn = null;
		XMLElement element = null;
		try {
			if (config != null)
				element = config.getRoot().getChildByName(nameOfNode);
			if (element != null) {
				return instantiateFromXMLElement(classType, defaultInstance, element);
			} else {
				Msg.info("No config for " + classType.getSimpleName() + " found. Using default of "
						+ defaultInstance.getClass().getSimpleName());
				toReturn = defaultInstance;
			}
		} catch (Exception e) {
			Msg.info(classType.getSimpleName() + " loading failed. Error below:\n" + ExceptionUtil.getStackTrace(e));
		}
		return toReturn;
	}

	/**
	 * The prefered form of instantiateFromSimConfig. Same as instantiateFromSimConfig except for using
	 * classType.getSimpleName as the name of the node to look in.
	 * 
	 * @param classType
	 *            the type of the class to be instantiated (same as [class name].class)
	 * @param defaultInstance
	 *            The default instance to be used in case no specification is given in the Configuration file.
	 * @return An instance of the wanted class, either reflexively instantiated or the default.
	 */
	public static <A extends ConfigurableClass> A instantiateFromSimConfig(Class<A> classType, A defaultInstance) {
		return instantiateFromSimConfig(classType.getSimpleName(), classType, defaultInstance);
	}

	/**
	 * Allows instantiation of a configurable class given just the name of the object wanted, e.g, calling it with
	 * FileSplitter.class will return a custom FileSplitter object if defined in the xml, or just the default if it wasn't
	 * specified in the config xml.
	 * 
	 * @param nameOfXMLNode
	 *            The name of the superclass of the object desired
	 * @return An instance of that object
	 */
	public static <A extends ConfigurableClass> A instantiateFromSimConfig(Class<A> forClass) {
		String nameOfXMLNode = forClass.getSimpleName();
		// Msg.info("Instantiating from config: " + nameOfXMLNode);
		return instantiateFromSimConfig(nameOfXMLNode, forClass, getDefaultInstance(forClass, null));
	}
}
