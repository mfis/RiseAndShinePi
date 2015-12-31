package mfi.riseandshinepi.util;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import mfi.riseandshinepi.hardware.CurrentDateTime;

public class TimestampTest {

	public static void main(String[] args) {

		test(100000, false);

		System.out.println("l0 against l1");
		System.out.println("l2 against l3");
		System.out.println("l4 against l5");
		System.out.println("");

		test(1000000, true);

		System.out.println("end");

	}

	private static void test(int iterations, boolean sysout) {

		// FIXME: l0 with Singleton
		// Proof, that Singleton with timer is faster than
		// System.currentTimeMillis()

		long l0 = System.currentTimeMillis();
		for (int i = 0; i < iterations; i++) {
			long x = CurrentDateTime.getInstance().getMillis();
			if (x == -1) {
				System.out.println("l0");
			}
		}
		if (sysout) {
			System.out.println("l0 = " + (System.currentTimeMillis() - l0));
		}

		long l1 = System.currentTimeMillis();
		for (int i = 0; i < iterations; i++) {
			long x = System.currentTimeMillis();
			if (x == -1) {
				System.out.println("l1");
			}
		}
		if (sysout) {
			System.out.println("l1 = " + (System.currentTimeMillis() - l1));
		}

		// ---------------------------------------------------------------------

		long l2 = System.currentTimeMillis();
		for (int i = 0; i < iterations; i++) {
			long x = Calendar.getInstance().getTimeInMillis(); // USED IN
																// AnalogClock
			if (x == -1) {
				System.out.println("l2");
			}
		}
		if (sysout) {
			System.out.println("l2 = " + (System.currentTimeMillis() - l2));
		}

		Calendar c3 = new GregorianCalendar();
		long l3 = System.currentTimeMillis();
		for (int i = 0; i < iterations; i++) {
			c3.setTimeInMillis(CurrentDateTime.getInstance().getMillis());
			long x = c3.getTimeInMillis();
			if (x == -1) {
				System.out.println("l3");
			}
		}
		if (sysout) {
			System.out.println("l3 = " + (System.currentTimeMillis() - l3));
		}

		// ---------------------------------------------------------------------

		long l4 = System.currentTimeMillis();
		for (int i = 0; i < iterations; i++) {
			LocalTime lt4 = LocalTime.now(); // USED IN WeatherController
			if (lt4 == null) {
				System.out.println("l4");
			}
		}
		if (sysout) {
			System.out.println("l4 = " + (System.currentTimeMillis() - l4));
		}

		LocalTime lt5;
		Calendar c5 = new GregorianCalendar();
		long l5 = System.currentTimeMillis();
		for (int i = 0; i < iterations; i++) {
			c5.setTimeInMillis(CurrentDateTime.getInstance().getMillis());
			lt5 = LocalTime.of(c5.get(java.util.Calendar.HOUR_OF_DAY), c5.get(java.util.Calendar.MINUTE));
			if (lt5 == null) {
				System.out.println("l5");
			}
		}
		if (sysout) {
			System.out.println("l5 = " + (System.currentTimeMillis() - l5));
		}

		// ---------------------------------------------------------------------

	}
}
