package mapreducesim.util;

import org.simgrid.msg.Msg;

public class SafeParsing {

	public static int safeIntParse(String parseFom, int defaultValue, String errorMessage) {
		try {
			int result = Integer.parseInt(parseFom);
			return result;
		} catch (Exception e) {
			Msg.debug(errorMessage);
			return defaultValue;
		}
	}
}
