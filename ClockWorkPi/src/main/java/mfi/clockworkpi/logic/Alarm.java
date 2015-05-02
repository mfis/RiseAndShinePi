package mfi.clockworkpi.logic;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Alarm {

	public Alarm(int hour, int minute, boolean onWeekdaysOnly, boolean once) {
		this.alarmHour = hour;
		this.alarmMinute = minute;
		this.onWeekdaysOnly = onWeekdaysOnly;
		this.once = once;
	}

	private int alarmHour;
	private int alarmMinute;
	private boolean onWeekdaysOnly;
	private boolean once;

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

		Calendar cal = new GregorianCalendar();
		int actHour = cal.get(Calendar.HOUR_OF_DAY);
		int actMinute = cal.get(Calendar.MINUTE);
		int alarmMinOfDay = (alarmHour * 60) + alarmMinute;
		int actMinOfDay = (actHour * 60) + actMinute;

		if (actMinOfDay > alarmMinOfDay) {
			moveToNextDayAtAlarmTime(cal);
		} else {
			moveToAlarmTime(cal);
		}

		if (!once && onWeekdaysOnly) {
			while (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				moveToNextDayAtAlarmTime(cal);
			}
		}

		return cal;
	}

	public String nextAlarmTimeString() {

		Calendar actCalendar = new GregorianCalendar();
		Calendar alarmCalendar = nextAlarmTime();
		if (actCalendar.get(Calendar.YEAR) == alarmCalendar.get(Calendar.YEAR) && actCalendar.get(Calendar.DAY_OF_YEAR) == alarmCalendar.get(Calendar.DAY_OF_YEAR)) {
			// today - no weekday
			return Utils.getSimpleDateFormat("HH:mm").format(nextAlarmTime()) + " Uhr";
		} else {
			return Utils.getSimpleDateFormat("EE, HH:mm").format(nextAlarmTime()) + " Uhr";
		}

	}

	private void moveToAlarmTime(Calendar cal) {
		cal.set(Calendar.HOUR_OF_DAY, alarmHour);
		cal.set(Calendar.MINUTE, alarmMinute);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
	}

	private void moveToNextDayAtAlarmTime(Calendar cal) {
		cal.add(Calendar.DAY_OF_MONTH, 1);
		moveToAlarmTime(cal);
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
