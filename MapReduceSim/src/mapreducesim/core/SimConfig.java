package mapreducesim.core;

import mapreducesim.util.xml.XMLDocument;
import mapreducesim.util.xml.XMLElement;

import org.simgrid.msg.Msg;

/**
 * @author Andrey Kurenkov
 * @version 1.0 Mar 1, 2013
 */
public class SimConfig {
	public final static XMLDocument CONFIG = SimMain.getConfig();

	/**
	 * Simple getter for config
	 * 
	 * @return config
	 */
	public static XMLDocument getConfig() {
		return CONFIG;
	}

	/**
	 * Gets the contents of a desired XML within the static config, or returns
	 * the default if a config was not loaded at startup. The method also logs
	 * if the configuration does not have the element and the default is used
	 * instead.
	 * 
	 * @param elementName
	 *            the name of the element to get the contents of. Must be a
	 *            top-level child of the root of the configuration.
	 * @param defaultValue
	 *            the default value to return in case the config does not
	 * @return
	 */
	public static String getConfigurationElementText(String elementName,
			String defaultValue) {
		if (CONFIG.getRoot() != null) {
			XMLElement child = CONFIG.getRoot().getChildByName(elementName);
			if (child != null)
				if (child.hasContentText())
					return child.getContentText();
		}
		Msg.info("No value found for " + elementName
				+ " in config file. Using default " + defaultValue);
		return defaultValue;
	}

	/**
	 * Returns a child of the configuration with the given element if it exists.
	 * Helper to not need getter and to handle safety regarding root.
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
	 * Retrieves a simple string value from the root of the xml config, or
	 * defaultValue if it wasn't found in the config.
	 * 
	 * @param elementName
	 * @param defaultValue
	 * @return
	 */
	public static String getSimpleValue(String elementName, String defaultValue) {
		if (CONFIG.getRoot() != null) {
			XMLElement ele = CONFIG.getRoot().getChildByName(elementName);
			if (ele != null) {
				return ele.getContentText();
			}
		}

		return defaultValue;
	}

}
