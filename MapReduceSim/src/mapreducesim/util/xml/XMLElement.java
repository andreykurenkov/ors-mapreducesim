/********+R0y

This file is part of the Troy programming language.

All code is copyright 2012 Troy O'Neal.  All rights reserved.

 **********/
package mapreducesim.util.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class XMLElement extends XMLNode {

	private List<XMLNode> children;
	private String qname;
	private HashMap<String, String> attributes = new HashMap<String, String>();
	private boolean hasClosing = true;

	public XMLElement(String qname) {
		this.qname = qname;
		children = new ArrayList<XMLNode>();
	}

	public XMLElement(String qname, boolean hasClosingTag) {
		this(qname);
		this.hasClosing = hasClosingTag;
	}

	public List<XMLNode> getChildren() {
		return children;
	}

	/**
	 * Helper method to extract only the XMLElement children of this element
	 * 
	 * @return
	 */
	public List<XMLElement> getElements() {
		ArrayList<XMLElement> elements = new ArrayList<XMLElement>();
		for (XMLNode node : children)
			if (node instanceof XMLElement)
				elements.add((XMLElement) node);
		return elements;
	}

	public void addChild(XMLNode child) {
		children.add(child);
	}

	public String getQName() {
		return qname;
	}

	private String getChildrenRawXML(boolean formatPretty, int depth) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < getChildren().size(); i++) {
			sb.append(getChildren().get(i).doToRawXML(formatPretty, depth));
		}
		return sb.toString();
	}

	public String doToRawXML(boolean formatPretty, int depth) {

		String indent;
		if (formatPretty) {
			indent = StringUtil.repeat("\t", depth);
		} else {
			indent = "";
		}

		String ls = System.getProperty("line.separator");
		StringBuilder sb = new StringBuilder();
		sb.append(indent + "<" + qname);
		if (attributes.size() > 0) {
			for (String attrName : attributes.keySet()) {
				sb.append(" ");
				sb.append(attrName);
				sb.append("=");
				sb.append("\"");
				sb.append(attributes.get(attrName));
				sb.append("\"");
			}
		}
		if (!hasClosing) {
			sb.append(" /");
		}
		sb.append(">");

		boolean textOnLine = (children.size() == 1 && (children.get(0) instanceof XMLTextNode));

		if (textOnLine) {
			sb.append(((XMLTextNode) children.get(0)).getText());
		} else {
			if (formatPretty) {
				sb.append(ls);
			}
			sb.append(getChildrenRawXML(formatPretty, depth + 1));
			// sb.append(indent);
		}

		if (hasClosing) {
			if (!textOnLine) {
				sb.append(indent);
			}
			sb.append("</" + qname + ">");
			if (formatPretty) {
				sb.append(ls);
			}
		}
		return sb.toString();
	}

	public XMLElement deepCopy() {
		XMLElement newNode = new XMLElement(qname);
		List<XMLNode> newChildren = new ArrayList<XMLNode>();

		newNode.hasClosing = hasClosing;

		// copy attributes
		for (String attrName : attributes.keySet()) {
			newNode.setAttribute(attrName, getAttributeValue(attrName));
		}

		// copy children
		for (int i = 0; i < getChildren().size(); i++) {
			newChildren.add(getChildren().get(i).deepCopy());
		}
		newNode.children = newChildren;
		return newNode;
	}

	public String getContentText() {
		if (getChildren().size() == 1) {
			XMLNode child = getChildren().get(0);
			if (child instanceof XMLTextNode) {
				return ((XMLTextNode) child).getText();
			}
		}
		return null;
	}

	public boolean hasContentText() {
		if (getChildren().size() == 1) {
			XMLNode child = getChildren().get(0);
			if (child instanceof XMLTextNode) {
				return true;
			}
		}
		return false;
	}

	public XMLElement getChildByName(String qname) {
		for (int i = 0; i < getChildren().size(); i++) {
			XMLNode child = getChildren().get(i);
			if (child instanceof XMLElement) {
				XMLElement tn = (XMLElement) child;
				if (tn.getQName().equals(qname)) {
					return tn;
				}
			}
		}
		return null;
	}

	public String getText(String childName) {
		return getChildText(childName);
	}

	public List<XMLElement> getChildrenByName(String qname) {
		List<XMLElement> rv = new ArrayList<XMLElement>();
		for (int i = 0; i < getChildren().size(); i++) {
			XMLNode child = getChildren().get(i);
			if (child instanceof XMLElement) {
				XMLElement tn = (XMLElement) child;
				if (tn.getQName().equals(qname)) {
					rv.add(tn);
				}
			}
		}
		return rv;
	}

	public String getChildText(String childName) {
		XMLNode child = getChildByName(childName);
		if (child instanceof XMLElement) {
			XMLElement tn = (XMLElement) child;
			if (tn.hasContentText()) {
				return tn.getContentText();
			}
		}
		return null;
	}

	public HashMap<String, String> getAttributes() {
		return attributes;
	}

	public String getAttributeValue(String attrName) {
		return attributes.get(attrName);
	}

	public void setAttribute(String attrName, String attrVal) {
		attributes.put(attrName, attrVal);
		// System.out.println("Attrs: "+attributes);
	}

	public boolean equals(Object n2) {
		if (!(n2 instanceof XMLElement)) {
			return false;
		}
		return equals((XMLElement) n2);
	}

	public boolean equals(XMLElement t2) {
		if (t2.getQName().equals(getQName())) {
			if (t2.getAttributes().equals(getAttributes())) {
				if (t2.children.equals(children)) {
					return true;
				}
			}
		}
		return false;
	}

	public HashMap<String, String> toMap() {
		HashMap<String, String> hm = new HashMap<String, String>();
		for (int i = 0; i < children.size(); i++) {
			XMLNode ch = children.get(i);
			if (ch instanceof XMLElement) {
				XMLElement tn = (XMLElement) ch;
				if (tn.hasContentText()) {
					hm.put(tn.getQName(), tn.getContentText());
				}
			}
		}
		return hm;
	}

	public void setContentText(String string) {
		children = new ArrayList<XMLNode>();
		children.add(new XMLTextNode(string));
	}

	public String toString() {
		return toRawXML();
	}

	public void addChildren(XMLNode[] nodes) {
		for (int i = 0; i < nodes.length; i++) {
			addChild(nodes[i]);
		}
	}

	public int getTagNodeChildCount() {
		int a = 0;
		for (int i = 0; i < getChildren().size(); i++) {
			XMLNode child = getChildren().get(i);
			if (child instanceof XMLElement) {
				a++;
			}
		}
		return a;
	}

	public List<XMLNode> getTagNodeChildren() {
		ArrayList<XMLNode> rv = new ArrayList<XMLNode>();
		for (int i = 0; i < getChildren().size(); i++) {
			XMLNode child = getChildren().get(i);
			if (child instanceof XMLElement) {
				rv.add(child);
			}
		}
		return rv;
	}

	public String toRawXML(int... options) {
		return XMLNode.Util.toRawXML(this, options);
	}

	public void removeChild(XMLNode child) {
		children.remove(child);
	}

}
