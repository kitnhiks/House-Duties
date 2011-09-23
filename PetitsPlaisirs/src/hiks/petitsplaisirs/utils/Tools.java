package hiks.petitsplaisirs.utils;

public class Tools {

	
	public static String escapeDataChars(String s){
		String returnValue = s.replace("\"", "");
		return returnValue;
	}
}
