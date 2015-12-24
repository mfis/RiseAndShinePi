package mfi.riseandshinepi.logic;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import mfi.riseandshinepi.hardware.CurrentDateTime;

public class Alarm {

	public Alarm(int hour, int minute, boolean onWeekdaysOnly, boolean once, boolean snooze, boolean active) {
		this.alarmHour = hour;
		this.alarmMinute = minute;
		this.onWeekdaysOnly = onWeekdaysOnly;
		this.once = once;
		this.snooze = snooze;
		this.active = active;
		alarmCalendar = new GregorianCalendar();
		actualCalendar = new GregorianCalendar();
		refreshCache();
	}

	private int alarmHour;
	private int alarmMinute;
	private boolean onWeekdaysOnly;
	private boolean once;
	private boolean snooze;
	private Calendar actualCalendar;
	private Calendar alarmCalendar;
	private static SimpleDateFormat sdfHHmm = Utils.getSimpleDateFormat("HH:mm");;
	private static SimpleDateFormat sdfEEHHmm = Utils.getSimpleDateFormat("EE, HH:mm");
	private int dayInYear = -1;
	private boolean active;
	private boolean hasBeenTriggered = false;

	private Calendar cachedNextAlarm;
	private String cachedNextAlarmString;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(30);
		if (once) {
			if (snooze) {
				sb.append("schlummern");
			} else {
				sb.append("einmalig");
			}
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

	public void hasBeenTriggered() {

		actualCalendar.setTimeInMillis(CurrentDateTime.getInstance().getMillis());
		if (cachedNextAlarm != null && !cachedNextAlarm.after(actualCalendar)) {
			hasBeenTriggered = true;
		}
	}

	private Calendar nextAlarmTime(long now) {

		if (!active) {
			return null;
		}

		alarmCalendar.setTimeInMillis(now);
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

	private static String nextAlarmTimeStringFor(Calendar alarmCalendar, Calendar actCalendar) {

		if (alarmCalendar == null) {
			return null;
		}

		if (actCalendar.get(Calendar.YEAR) == alarmCalendar.get(Calendar.YEAR)
				&& actCalendar.get(Calendar.DAY_OF_YEAR) == alarmCalendar.get(Calendar.DAY_OF_YEAR)) {
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

	private synchronized void refreshCache() {

		if (!active) {
			cachedNextAlarm = null;
			cachedNextAlarmString = null;
			hasBeenTriggered = false;
			return;
		}

		actualCalendar.setTimeInMillis(CurrentDateTime.getInstance().getMillis());
		cachedNextAlarm = nextAlarmTime(CurrentDateTime.getInstance().getMillis());
		hasBeenTriggered = false;
		cachedNextAlarmString = nextAlarmTimeStringFor(cachedNextAlarm, actualCalendar);

		int newDayInYear = actualCalendar.get(Calendar.DAY_OF_YEAR);
		dayInYear = newDayInYear;
	}

	private void checkCache() {

		if (cachedNextAlarm == null) {
			refreshCache();
			return;
		}

		actualCalendar.setTimeInMillis(CurrentDateTime.getInstance().getMillis());
		int newDayInYear = actualCalendar.get(Calendar.DAY_OF_YEAR);
		if (newDayInYear != dayInYear) {
			dayInYear = newDayInYear;
			refreshCache();
			return;
		}

		if (cachedNextAlarm != null && !cachedNextAlarm.after(actualCalendar) && hasBeenTriggered) {
			if (isOnce()) {
				setActive(false);
			}
			refreshCache();
			return;
		}

		if ((active && cachedNextAlarm == null) || !active && cachedNextAlarm != null) {
			refreshCache();
			return;
		}
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
		refreshCache();
	}

	public int getMinute() {
		return alarmMinute;
	}

	public void setMinute(int minute) {
		this.alarmMinute = minute;
		refreshCache();
	}

	public boolean isOnWeekdaysOnly() {
		return onWeekdaysOnly;
	}

	public void setOnWeekdaysOnly(boolean onWeekdaysOnly) {
		this.onWeekdaysOnly = onWeekdaysOnly;
		refreshCache();
	}

	public boolean isOnce() {
		return once;
	}

	public void setOnce(boolean once) {
		this.once = once;
		refreshCache();
	}

	public boolean isSnooze() {
		return snooze;
	}

	public Calendar getCachedNextAlarm() {
		checkCache();
		return cachedNextAlarm;
	}

	public String getCachedNextAlarmString() {
		checkCache();
		return cachedNextAlarmString;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
