/********+R0y

This file is part of the Troy programming language.

All code is copyright 2012 Troy O'Neal.  All rights reserved.

**********/
package mapreducesim.util.xml;

import java.awt.geom.Area;

public class StringUtil {
	public static String capitalize(String in){
		if (in.length()==0){
			return in;
		}
		return in.substring(0,1).toUpperCase()+in.substring(1);
	}
	
	public static boolean matchesAny(String[] regexes,String input){
		for (int i =0;i<regexes.length;i++){
			String regex = regexes[i];
			if (input.matches(regex)){
				return true;
			}
		}
		return false;
	}
	
	public static boolean isValidInt(String str){
		try{
			Integer.parseInt(str);
		}catch(NumberFormatException nfe){
			return false;
		}
		return true;
	}
	
	public static boolean isValidDouble(String str){
		try{
			Double.parseDouble(str);
		}catch(NumberFormatException nfe){
			return false;
		}
		
		return true;
	}
	
	public static boolean isValidBoolean(String str){
		
		return str.equalsIgnoreCase("true")||str.equalsIgnoreCase("false");
	}

	public static String repeat(String string, int n) {
		StringBuilder sb= new StringBuilder();
		for (int i = 0;i<n;i++){
			sb.append(string);
		}
		return sb.toString();
	}
	
}
