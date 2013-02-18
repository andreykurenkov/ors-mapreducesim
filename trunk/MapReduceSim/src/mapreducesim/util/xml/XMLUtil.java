/********+R0y

This file is part of the Troy programming language.

All code is copyright 2012 Troy O'Neal.  All rights reserved.

**********/
package mapreducesim.util.xml;

import java.util.Map;

public class XMLUtil {
	public static XMLNode parse(String xml){
		return XMLParser.parse(xml);
	}
	
	public static <K,V> XMLElement createFromMap(String parentName,Map<K,V> map){
		XMLElement parent = new XMLElement(parentName);
		for (K k: map.keySet()){
			XMLElement child = new XMLElement(k.toString());
			child.setContentText(map.get(k).toString());
			parent.addChild(child);
		}
		return parent;
	}
	
	public static Map<String,String> createMapFromNode(XMLElement tn){
		return tn.toMap();
	}
}
