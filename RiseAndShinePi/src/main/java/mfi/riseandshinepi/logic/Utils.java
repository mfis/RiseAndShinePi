package mfi.riseandshinepi.logic;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Utils {

	public static boolean isStringValueNumeric(String string) {
		if (string == null) {
			return false;
		}
		for (int i = 0; i < string.length(); i++) {
			if (!Character.isDigit(string.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static SimpleDateFormat getSimpleDateFormat(String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.GERMAN);
		// sdf.setTimeZone(TimeZone.getTimeZone("CET"));
		return sdf;
	}
}
