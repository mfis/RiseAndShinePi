package mfi.riseandshinepi.util;

import java.time.LocalTime;
import java.time.ZoneId;

public class LocalTimeTest {

	public static void main(String[] args) {

		System.out.println(ZoneId.systemDefault());

		testDaylight(9, 8, 17, true);
		testDaylight(11, 8, 17, true);
		testDaylight(16, 8, 17, true);

		testDaylight(18, 8, 17, false);
		testDaylight(23, 8, 17, false);
		testDaylight(1, 8, 17, false);
		testDaylight(5, 8, 17, false);
		testDaylight(7, 8, 17, false);
	}

	private static void testDaylight(int actualHour, int sunriseHour, int sunsetHour, boolean asserted) {

		LocalTime now = LocalTime.of(actualHour, 0);
		LocalTime sunrise = LocalTime.of(sunriseHour, 0);
		LocalTime sunset = LocalTime.of(sunsetHour, 0);

		if (!asserted == now.isAfter(sunrise) && now.isBefore(sunset)) {
			throw new IllegalStateException(actualHour + " * " + sunriseHour + "-" + sunsetHour);
		}

	}

}
