package mapreducesim.core;

import mapreducesim.util.xml.XMLDocument;
import mapreducesim.util.xml.XMLElement;

import org.simgrid.msg.Msg;

/**
 * A static class to hold the simulation configuration loaded at the beginning of simulation as well as helper methods for
 * accessing and parsing it.
 * 
 * @author Andrey Kurenkov
 * @version 1.0 Mar 1, 2013
 */
public class SimConfig {
	// SimMain.getConfig is protected, config retrieved now and publicly accesible through SimConfig.getConfig
	public final static XMLDocument CONFIG = SimMain.getConfig();

	/**
	 * Simple getter for configuration
	 * 
	 * @return simulation configuration
	 */
	public static XMLDocument getConfig() {
		return CONFIG;
	}

	/**
	 * Gets the contents of a desired XML within the static config, or returns the default if a config was not loaded at
	 * startup. The method also logs if the configuration does not have the element and the default is used instead.
	 * 
	 * @param elementName
	 *            the name of the element to get the contents of. Must be a top-level child of the root of the configuration.
	 * @param defaultValue
	 *            the default value to return in case the config does not
	 * @return context text of xml element if it exists or default value otherwise
	 */
	public static String getElementText(String elementName, String defaultValue) {
		if (CONFIG.getRoot() != null) {
			XMLElement child = CONFIG.getRoot().getChildByName(elementName);
			if (child != null)
				if (child.hasContentText())
					return child.getContentText();
		}
		Msg.info("No value found for " + elementName + " in config file. Using default " + defaultValue);
		return defaultValue;
	}

	/**
	 * Returns a child of the configuration with the given element if it exists. Helper to not need getter and to handle
	 * safety regarding root.
	 * 
	 * @param elementName
	 *            the name of the top-level child of the root in config
	 * @return desired Element if it config, else null
	 */
	public static XMLElement getConfigurationElement(String elementName) {
		if (CONFIG.getRoot() != null)
			return CONFIG.getRoot().getChildByName(elementName);
		return null;
	}

	/**
	 * Simple helper method to parse a double attribute from a given XMLElement
	 * 
	 * @param attribute
	 *            the name of the attribute
	 * @param XMLElement
	 *            element the element to parse from
	 * @param defaultVal
	 *            default value if not provided
	 * @return parse attribute or default if not there
	 */
	public static double parseDoubleAttribute(XMLElement element, String attribute, double defaultVal) {
		double toReturn = defaultVal;
		String attrStr = element.getAttributeValue(attribute);
		try {
			toReturn = Double.parseDouble(attrStr);
		} catch (Exception e) {
			Msg.info("Invalid or not value for attribute " + attribute + " for element " + element.getQName()
					+ "; using default" + defaultVal);

		}
		return toReturn;
	}

	/**
	 * Simple helper method to parse an int attribute from a given XMLElement
	 * 
	 * @param attribute
	 *            the name of the attribute
	 * @param XMLElement
	 *            element the element to parse from
	 * @param defaultVal
	 *            default value if not provided
	 * @return parse attribute or default if not there
	 */
	public static double parseIntAttribute(XMLElement element, String attribute, int defaultVal) {
		int toReturn = defaultVal;
		String attrStr = element.getAttributeValue(attribute);
		try {
			toReturn = Integer.parseInt(attrStr);
		} catch (Exception e) {
			Msg.info("Invalid or not value for attribute " + attribute + " for element " + element.getQName()
					+ "; using default" + defaultVal);

		}
		return toReturn;
	}

}
