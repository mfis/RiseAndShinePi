package mfi.clockworkpi.logic;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Alarm {

	public Alarm(int hour, int minute, boolean onWeekdaysOnly, boolean once) {
		this.alarmHour = hour;
		this.alarmMinute = minute;
		this.onWeekdaysOnly = onWeekdaysOnly;
		this.once = once;
		alarmCalendar = new GregorianCalendar();
	}

	private int alarmHour;
	private int alarmMinute;
	private boolean onWeekdaysOnly;
	private boolean once;
	private Calendar alarmCalendar;
	private static SimpleDateFormat sdfHHmm = Utils.getSimpleDateFormat("HH:mm");;
	private static SimpleDateFormat sdfEEHHmm = Utils.getSimpleDateFormat("EE, HH:mm");

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(30);
		if (once) {
			sb.append("einmalig");
		} else {
			sb.append(onWeekdaysOnly ? "Mo - Fr" : "täglich");
		}
		sb.append("   ");
		sb.append(alarmHour < 10 ? "0" : "");
		sb.append(alarmHour);
		sb.append(":");
		sb.append(alarmMinute < 10 ? "0" : "");
		sb.append(alarmMinute);
		sb.append(" Uhr");
		return sb.toString();
	}

	public Calendar nextAlarmTime() {

		alarmCalendar.setTimeInMillis(System.currentTimeMillis());
		long actHour = alarmCalendar.get(Calendar.HOUR_OF_DAY);
		long actMinute = alarmCalendar.get(Calendar.MINUTE);
		long actSecond = alarmCalendar.get(Calendar.SECOND);
		long alarmSecOfDay = ((alarmHour * 60) + alarmMinute) * 60;
		long actSecOfDay = (((actHour * 60) + actMinute) * 60) + actSecond;

		if (actSecOfDay > alarmSecOfDay) {
			moveToNextDayAtAlarmTime(alarmCalendar);
		} else {
			moveToAlarmTime(alarmCalendar);
		}

		if (!once && onWeekdaysOnly) {
			while (alarmCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || alarmCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				moveToNextDayAtAlarmTime(alarmCalendar);
			}
		}

		return alarmCalendar;
	}

	public static String nextAlarmTimeStringFor(Calendar alarmCalendar, Calendar actCalendar) {

		if (actCalendar.get(Calendar.YEAR) == alarmCalendar.get(Calendar.YEAR) && actCalendar.get(Calendar.DAY_OF_YEAR) == alarmCalendar.get(Calendar.DAY_OF_YEAR)) {
			// today - no weekday
			return sdfHHmm.format(alarmCalendar.getTime()) + " Uhr";
		} else {
			return sdfEEHHmm.format(alarmCalendar.getTime()) + " Uhr";
		}

	}

	private void moveToNextDayAtAlarmTime(Calendar cal) {
		cal.add(Calendar.DAY_OF_MONTH, 1);
		moveToAlarmTime(cal);
	}

	private void moveToAlarmTime(Calendar cal) {
		cal.set(Calendar.HOUR_OF_DAY, alarmHour);
		cal.set(Calendar.MINUTE, alarmMinute);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
	}

	public int getHour() {
		return alarmHour;
	}

	public void setHour(int hour) {
		this.alarmHour = hour;
	}

	public int getMinute() {
		return alarmMinute;
	}

	public void setMinute(int minute) {
		this.alarmMinute = minute;
	}

	public boolean isOnWeekdaysOnly() {
		return onWeekdaysOnly;
	}

	public void setOnWeekdaysOnly(boolean onWeekdaysOnly) {
		this.onWeekdaysOnly = onWeekdaysOnly;
	}

	public boolean isOnce() {
		return once;
	}

	public void setOnce(boolean once) {
		this.once = once;
	}

}
