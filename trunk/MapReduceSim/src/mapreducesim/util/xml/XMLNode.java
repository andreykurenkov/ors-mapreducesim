/********+R0y

This file is part of the Troy programming language.

All code is copyright 2012 Troy O'Neal.  All rights reserved.

**********/
package mapreducesim.util.xml;


public abstract class XMLNode {
	public static final int PRETTYFORMAT = 1;
	public abstract XMLNode deepCopy();
	
	/**
	 * 
	 * @param options Can be XMLNode.PRETTYFORMAT for formatted XML with tabs and spaces
	 * @return A raw xml string
	 */
	public abstract String toRawXML(int...options);
	
	/**
	 * You should not use this function.  Use toRawXML() instead
	 * @param formatPretty Whether to format the XML with tabs and line breaks.
	 * 		If this parameter is true, this node must be pre-trimmed for the output to make sense.
	 * @param depth The indent depth
	 * @return
	 */
	abstract String doToRawXML(boolean formatPretty, int depth);
	
	/**
	 * 
	 * @return A deep copy of this node with all empty text nodes removed
	 */
	public XMLNode trim(){
		return Trimmer.trimEmptyNodeWithCopy(this);
	}
	/**
	 * Removes all empty text nodes recursively from this node
	 */
	public void trimInPlace(){
		Trimmer.trimEmptyNodes(this);
	}
	
	static class Trimmer{
		
		/**
		 * Does not modify the input
		 * @param node
		 * @return
		 */
		static XMLNode trimEmptyNodeWithCopy(XMLNode node){
			XMLNode copy = node.deepCopy();
			if (copy instanceof XMLTextNode){
				XMLTextNode tn = (XMLTextNode)copy;
				if (isEmpty(tn)){
					return null;
				}
				return tn;
			}
			copy.trimInPlace(); 
			return copy;
		}
		/**
		 * Modifies the input
		 * @param node
		 */
		static void trimEmptyNodes(XMLNode node){
			if (node instanceof XMLElement){
				doTrimEmptyNodes((XMLElement)node);
			}
			else if (node instanceof XMLDocument){
				doTrimEmptyNodes(((XMLDocument)node).getRoot());
			}
			else{
				throw new RuntimeException("Unhandled node type: "+node.getClass());
			}
		}
		
		/**
		 * Removes all the empty text nodes (recursive) in a tag node.  WARNING: modifies the input
		 * @param tn
		 * @return
		 */
		private static void doTrimEmptyNodes(XMLElement tn){

			
			for (int i = 0;i<tn.getChildren().size();i++){
				XMLNode child = tn.getChildren().get(i);
				if (child instanceof XMLTextNode){
					if (isEmpty((XMLTextNode)child)){
						tn.removeChild(child);
						i--;
						continue;
					}
				}else if (child instanceof XMLElement){
					XMLElement chtn = (XMLElement)child;
					doTrimEmptyNodes(chtn);
				}else{
					throw new RuntimeException("Unsupported node: "+child.getClass());
				}
			}
			
		}
		
		static boolean isEmpty(XMLTextNode tn){
			
			return tn.getText().trim().length()==0;
		}
	}
	
	static class Util{
		public static String toRawXML(XMLNode node,int...options){
			
			
			boolean prettyFlag = false;
			
			//look at the options specified
			for (int i = 0;i<options.length;i++){
				int opt = options[i];
				if (opt==PRETTYFORMAT){
					prettyFlag = true;
				}
			}
			
			if (prettyFlag){
				return node.trim().doToRawXML(prettyFlag,0);
			}
			
			return node.doToRawXML(false,0);
			
		}
	}
}
