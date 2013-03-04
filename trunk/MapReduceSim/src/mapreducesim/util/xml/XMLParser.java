/********+R0y

This file is part of the Troy programming language.

All code is copyright 2012 Troy O'Neal.  All rights reserved.

 **********/
package mapreducesim.util.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import mapreducesim.util.xml.XMLParser.RootScope.ElementScope;
import mapreducesim.util.xml.XMLParser.RootScope.ElementScope.TagScope;

//horrible perf, don't know why yet (UPDATE: FIXED!!)
public class XMLParser {

	public static final boolean debug = false;
	public static final boolean displayProgress = false;

	public static abstract class Scope {
		public void onBeingFolded() {

		}

		public String toString() {
			return getClass().getSimpleName();
		}
	}

	public static abstract class ParentScope extends Scope {
		public ArrayList<Scope> children = new ArrayList<Scope>();

		public void fold(Scope subScope) {
			children.add(subScope);
		}

		public String toString() {
			return super.toString() + children.toString();
		}
	}

	public static class QuotedTextScope extends Scope {
		static StringBuilder directBuffer = new StringBuilder(255);

		public QuotedTextScope() {
			directBuffer.delete(0, directBuffer.length());
		}

		public String toString() {
			return super.toString() + "(\"" + directBuffer.toString() + "\")";
		}

	}

	public static class TextScope extends Scope {
		static StringBuilder buffer = new StringBuilder(255);
		String text;

		public TextScope() {
			buffer.delete(0, buffer.length());
		}

		public void onBeingFolded() {
			text = buffer.toString();
		}

		public String toString() {
			return super.toString() + "\"" + buffer + "\"";
		}
	}

	public static class RootScope extends ParentScope {

		public static class ElementScope extends ParentScope {
			String qname;
			boolean isHeader = false;

			public void fold(Scope subScope) {
				super.fold(subScope);
				if (subScope instanceof TagScope) {
					TagScope ts = (TagScope) subScope;
					if (ts.isHeader) {
						isHeader = true;
					}
					if (ts.qname != null) {
						if (ts.isStartTag) {
							qname = ts.qname;
						}
					}
				}
			}

			public static class TagScope extends ParentScope {
				boolean isStartTag;

				// if the tag is one of these <br /> with no closing tag
				boolean isSingleTag = false;
				String qname;
				String innerText;
				String commentText;
				boolean isHeader = false;
				boolean isDoctype = false;
				boolean isComment = false;
				boolean commentStartFound = false;
				boolean commentEndFound = false;
				static StringBuilder nameBuffer = new StringBuilder();
				static StringBuilder commentBuilder = new StringBuilder();
				static StringBuilder attributeNameBuffer = new StringBuilder();
				boolean endFound = false;
				boolean nameFound = false;
				String currAttributeName;
				boolean expectingAttribute = false;
				HashMap<String, String> attributes = new HashMap<String, String>();

				public TagScope() {
					nameBuffer.delete(0, nameBuffer.length());
					attributeNameBuffer.delete(0, attributeNameBuffer.length());
				}

				public void processCharacter(String rawXML, int i) {
					char c = rawXML.charAt(i);
					// check for end tag
					if (c == '>') {
						endFound = true;
						commentText = commentBuilder.toString();
						commentBuilder.delete(0, commentBuilder.length());
						innerText = nameBuffer.toString();
						if (innerText.startsWith("?")) {
							isHeader = true;
							qname = "?xml?";
						} else {
							if (attributes.size() == 0) {
								qname = innerText;
							}
						}
						if (innerText.startsWith("/")) {
							isStartTag = false;
						} else {
							isStartTag = true;
						}
					}
					if (!expectingAttribute) {
						// build up the name/comment of this tag

						// check for !doctype declarations
						if (nameBuffer.length() == 0 && c == '!'
								&& rawXML.charAt(i + 1) == 'D') {
							// we found a doctype declaration
							isDoctype = true;
							isSingleTag = true;
							nameFound = true;

						}

						if (isComment) {
							if (!commentEndFound) {
								if (commentStartFound) {
									if (rawXML.charAt(i + 1) == '-'
											&& rawXML.charAt(i + 2) == '-') {
										commentEndFound = true;

									}

									commentBuilder.append(c);
								} else {
									// look for the -- which begins the comment
									if (c == '-' && rawXML.charAt(i - 1) == '-') {
										commentStartFound = true;
									}
								}
							}
						} else if (isDoctype) {
							// no - op
						} else {
							if (c == ' ') {
								qname = nameBuffer.toString();
								nameFound = true;
								expectingAttribute = true;
							} else {
								if (!nameFound) {
									if (nameBuffer.length() == 0) {
										if (c == '!') {
											// consider this an xml comment
											isComment = true;
											isSingleTag = true;
										}
									}
									nameBuffer.append(c);
								}
							}
						}
					} else {
						// if we were expecting attribute text
						if (c == ' ' && attributeNameBuffer.length() == 0) {
							// just keep waiting till the attribute name
						} else {
							// build up the attribute name
							attributeNameBuffer.append(c);
						}

						// if we were expecting attributes and found a "/"
						if (c == '/') {
							// then consider this a single tag (e.g, <br />)
							isSingleTag = true;
						}
					}
				}

				public void fold(Scope subScope) {
					super.fold(subScope);
					if (subScope instanceof QuotedTextScope) {
						if (nameFound == false) {
							throw new RuntimeException(
									"Name not found, should be found before folding up the tag");
						}
						QuotedTextScope qts = (QuotedTextScope) subScope;
						if (isDoctype) {
							// the only quoted scope is the dtd place
							currAttributeName = "dtdurl";
						} else {
							currAttributeName = attributeNameBuffer.toString();
							currAttributeName = currAttributeName.substring(0,
									currAttributeName.length() - 1);
						}
						attributeNameBuffer = new StringBuilder();
						println("Attr: '" + currAttributeName + "' -> '"
								+ qts.directBuffer.toString() + "'");
						attributes.put(currAttributeName, qts.directBuffer
								.toString());
					}
				}

				public String toString() {
					return "TagScope<" + qname
							+ (attributes.size() > 0 ? (" " + attributes) : "")
							+ ">";
				}

			}

		}

	}

	Stack<Scope> scopeStack;

	public static XMLNode parse(String rawXml) {

		return new XMLParser().doParse(rawXml);
	}

	char currChar;

	public static void println(Object o) {
		if (debug) {
			System.out.println(o);
		}
	}

	public XMLNode doParse(String rawXML) {

		rawXML = rawXML.trim();

		println("Parsing: " + rawXML);

		if (rawXML.length() == 0) {
			throw new RuntimeException("Input XML was blank, cannot parse");
		}

		scopeStack = new Stack<Scope>();
		scopeStack.push(new RootScope());

		int maxStackSize = 0;
		int prevStackSize = 0;
		for (int i = 0; i < rawXML.length(); i++) {
			currChar = rawXML.charAt(i);
			Scope scopeStackPeek = scopeStack.peek();

			if (displayProgress) {
				if (scopeStack.size() != prevStackSize || true) {
					System.out.println("Stack size: " + scopeStack.size());
					if (scopeStack.size() > maxStackSize) {
						maxStackSize = scopeStack.size();
					}
					prevStackSize = scopeStack.size();
				}
				System.out.println((i + 1) * 100.0f / rawXML.length() + "%");
				System.out.println("Processed characters: " + i + "/"
						+ rawXML.length());

			}
			if (debug && displayProgress) {
				println("Char('" + currChar + "'),stacktop=" + scopeStackPeek);
			}

			if (scopeStackPeek instanceof RootScope) {
				if (currChar == '<') {
					println("pushing element and tag scope on rootscope");
					scopeStack.push(new ElementScope());
					scopeStack.push(new TagScope());
					continue;
				}
			} else if (scopeStackPeek instanceof QuotedTextScope) {
				QuotedTextScope qts = (QuotedTextScope) scopeStackPeek;
				if (isQuote(currChar)) {
					fold();
					continue;
				}
				qts.directBuffer.append(currChar);
				continue;
			} else if (scopeStackPeek instanceof TagScope) {
				TagScope ts = (TagScope) scopeStackPeek;
				if (isQuote(currChar)) {
					scopeStack.push(new QuotedTextScope());
					continue;
				}
				ts.processCharacter(rawXML, i);

				if (ts.endFound) {

					// if we had the opening tag, and it wasn't a single tag
					// like <br />
					if (ts.isStartTag && (!ts.isSingleTag)) {
						// fold tag
						println("Folding tag");
						fold();
					} else {
						// fold tag and element
						println("Folding tag and element");
						fold();
						fold();
					}

					// stay on same char
					i--;
					continue;
				}
			} else if (scopeStackPeek instanceof ElementScope) {
				ElementScope es = (ElementScope) scopeStackPeek;
				if (es.isHeader) {
					fold();
					continue;
				}

				if (currChar == '<') {
					if (rawXML.charAt(i + 1) == '/') {
						// we found an end tag
						println("Found end tag, not pushing new element scope");
					} else {
						// we found a start tag
						println("Found start tag,  pushing new element scope");

						scopeStack.push(new ElementScope());
					}
					scopeStack.push(new TagScope());
					continue;
				} else {
					// we have plain char data
					scopeStack.push(new TextScope());

					continue;
				}
			} else if (scopeStackPeek instanceof TextScope) {
				if (currChar == '<') {
					fold();
					i--;
					continue;
				}
				TextScope ts = (TextScope) scopeStackPeek;
				ts.buffer.append(currChar);
			} else {
				throw new RuntimeException("Invalid scope: " + scopeStackPeek);
			}
		}

		if (debug) {
			System.out.println("Max stack size: " + maxStackSize);
			System.out.println("Final stack: " + scopeStack);
		}

		return makeDOM(scopeStack.get(0));
	}

	private boolean isQuote(char c) {
		return (c == '"' || c == '\'');
	}

	public void fold() {
		scopeStack.peek().onBeingFolded();
		((ParentScope) scopeStack.get(scopeStack.size() - 2)).fold(scopeStack
				.peek());
		scopeStack.pop();
	}

	public XMLNode makeDOM(Scope s) {
		if (s instanceof RootScope) {
			return makeDOM((RootScope) s);
		}
		if (s instanceof ElementScope) {
			return makeDOM((ElementScope) s);
		}
		if (s instanceof TextScope) {
			return makeDOM((TextScope) s);
		}

		throw new RuntimeException("Unhandled scope: " + s);
	}

	public XMLNode makeDOM(TextScope ts) {
		return new XMLTextNode(ts.text);
	}

	public boolean isHeader(Scope s) {
		if (s instanceof ElementScope) {
			ElementScope es = (ElementScope) s;
			return es.isHeader;
		}
		return false;
	}

	public boolean isDoctypeDeclaration(Scope s) {
		if (s instanceof ElementScope) {
			ElementScope es = (ElementScope) s;
			if (es.children.size() == 1) {
				Scope ss = es.children.get(0);
				if (ss instanceof TagScope) {
					TagScope ts = (TagScope) ss;
					if (ts.isDoctype) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public XMLNode makeDOM(RootScope rs) {

		// check for header

		if (rs.children.size() == 1) {
			return makeDOM(rs.children.get(0));
		} else {

			// // handle awkward xml document declaration stuff

			String version = null, encoding = null;
			XMLElement dtdDecl = null;
			for (int i = 0; i < 2; i++) {
				if (rs.children.size() >= i) {
					Scope c = rs.children.get(i);
					if (isHeader(c)) {
						ElementScope es = (ElementScope) c;
						// extract header data
						// TODO: actually get encoding and version stuff here
					} else if (isDoctypeDeclaration(c)) {
						// extract doctype data
						dtdDecl = (XMLElement) makeDOM(c);
					}
				}
			}
			// the last child must be the actual root
			Scope lastC = rs.children.get(rs.children.size() - 1);
			if (isHeader(lastC) || isDoctypeDeclaration(lastC)) {
				throw new RuntimeException(
						"Empty xml body (only headers declared, no content)");
			}
			XMLElement body = (XMLElement) makeDOM(lastC);
			XMLDocument xmld = new XMLDocument(encoding, version, dtdDecl);
			xmld.setRoot(body);
			return xmld;
		}

		/*
		 * else if (rs.children.size() == 2) { // TODO XMLDocument xmlDocument =
		 * new XMLDocument(); XMLElement tn =
		 * (XMLElement)makeDOM(rs.children.get(1)); xmlDocument.setRoot(tn);
		 * return xmlDocument; } else { throw new
		 * RuntimeException("Invalid root scope.  Num of children: "
		 * +rs.children.size()); }
		 */
	}

	public XMLNode makeDOM(ElementScope es) {
		TagScope startTagScope = (TagScope) es.children.get(0);
		XMLElement node;
		if (startTagScope.isComment) {
			return new XMLCommentNode(startTagScope.commentText);
		}
		if (startTagScope.isSingleTag) {
			node = new XMLElement(es.qname, false);
		} else {
			node = new XMLElement(es.qname, true);
		}
		for (String attrName : startTagScope.attributes.keySet()) {
			node.setAttribute(attrName, startTagScope.attributes.get(attrName));
		}
		for (int i = 0; i < es.children.size(); i++) {
			Scope ss = es.children.get(i);
			if (!(ss instanceof TagScope)) {

				// do not add empty text scopes
				if (ss instanceof TextScope) {
					TextScope ts = (TextScope) ss;
					if (ts.text.length() == 0) {
						continue;
					}
				}
				node.addChild(makeDOM(ss));
			}
		}
		return node;
	}

}
