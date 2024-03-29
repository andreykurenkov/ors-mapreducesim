/********+R0y

This file is part of the Troy programming language.

All code is copyright 2012 Troy O'Neal.  All rights reserved.

 **********/
package mapreducesim.util.xml;

import java.io.File;

import mapreducesim.util.SmartFile;

public class XMLDocument extends XMLNode {

	String encoding = null;
	String version = null;
	XMLElement root;
	XMLElement doctype = null;

	public XMLDocument(String encoding, String version,
			XMLElement doctypeDeclaration) {
		this.encoding = encoding;
		this.version = version;
		this.doctype = doctype;
	}

	public XMLDocument(XMLElement root) {
		this.root = root;
	}

	public XMLDocument() {

	}

	public static XMLDocument parseDocument(File file) {
		SmartFile smart = new SmartFile(file);
		if (smart.exists())
			return parseDocument(smart.read());
		return null;
	}

	public static XMLDocument parseDocument(String str) {
		if (str.contains("encoding"))
			str = str.substring(str.indexOf("?>") + 2);
		XMLNode parsed = XMLParser.parse(str);
		if (parsed instanceof XMLElement)
			return new XMLDocument((XMLElement) parsed);
		if (parsed instanceof XMLDocument)
			return (XMLDocument) parsed;
		return null;
	}

	public XMLElement getRoot() {
		return root;
	}

	public void setRoot(XMLElement root) {
		this.root = root;
	}

	public String doToRawXML(boolean formatPretty, int depth) {
		StringBuilder res = new StringBuilder();
		String indent;
		if (formatPretty) {
			indent = StringUtil.repeat("\t", depth);
		} else {
			indent = "";
		}

		String ls = System.getProperty("line.separator");
		if (version != null && encoding != null) {
			res.append(indent + "<?xml version=\"" + version + "\" encoding=\""
					+ encoding + "\"?>" + ls);
		}
		if (root != null) {
			res.append(root.doToRawXML(formatPretty, depth + 1));
		}
		return res.toString();
	}

	public XMLDocument deepCopy() {
		XMLDocument xmlDocument = new XMLDocument(encoding, version, doctype);
		xmlDocument.setRoot(getRoot().deepCopy());
		return xmlDocument;
	}

	public boolean equals(Object n2) {
		if (!(n2 instanceof XMLDocument)) {
			return false;
		}
		return equals((XMLDocument) n2);
	}

	public boolean equals(XMLDocument t2) {
		if (encoding.equals(t2.encoding)) {
			if (version.equals(t2.version)) {
				if (getRoot().equals(t2.getRoot())) {
					return true;
				}
			}
		}
		return false;
	}

	public String toString() {
		return toRawXML();
	}

	public String toRawXML(int... options) {
		return XMLNode.Util.toRawXML(this, options);
	}

}
