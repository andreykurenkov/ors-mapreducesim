/********+R0y

This file is part of the Troy programming language.

All code is copyright 2012 Troy O'Neal.  All rights reserved.

**********/
package mapreducesim.util.xml;


public class XMLCommentNode extends XMLNode{
	private String text;
	
	public XMLCommentNode(String text){
		this.text = text;
	}
	
	public String getText(){
		return text;
	}

	public XMLNode deepCopy() {
		return new XMLCommentNode(getText());
	}
	
	public String doToRawXML(boolean formatPretty, int depth){
		
		String indent;
		String ls = System.getProperty("line.separator");
		if (formatPretty){
			indent = StringUtil.repeat("\t",depth);
		}else{
			indent = "";
		}
		String xmlCmt = "<!--"+this.text+"-->";
		if (formatPretty){
			return indent+xmlCmt+ls;
		}
		return indent+xmlCmt;
	}
	
	public boolean equals(Object n2) {
		if (!(n2 instanceof XMLCommentNode)){
			return false;
		}
		return equals((XMLCommentNode)n2);
	}
	
	public boolean equals(XMLCommentNode t2){
		return getText().equals(t2.getText());
	}
	
	public String toString(){
		return "\""+getText()+"\"";
	}

	public String toRawXML(int... options) {
		return XMLNode.Util.toRawXML(this,options);
	}
	


}
