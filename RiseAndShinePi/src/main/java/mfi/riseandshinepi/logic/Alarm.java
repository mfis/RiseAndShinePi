package mfi.riseandshinepi.logic;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.apache.commons.lang3.StringUtils;
import mfi.riseandshinepi.hardware.CurrentDateTime;

public class Alarm {

	public Alarm(int hour, int minute, AlarmType alarmType, boolean active, ApplicationProperties property) {
		this.alarmHour = hour;
		this.alarmMinute = minute;
		this.alarmType = alarmType;
		this.active = active;
		this.property = property;
		alarmCalendar = new GregorianCalendar();
		actualCalendar = new GregorianCalendar();
		refreshCache();
	}

	private ApplicationProperties property;
	private int alarmHour;
	private int alarmMinute;
	private AlarmType alarmType;
	private Calendar actualCalendar;
	private Calendar alarmCalendar;
	private static SimpleDateFormat sdfHHmm = Utils.getSimpleDateFormat("HH:mm");
	private static SimpleDateFormat sdfEEHHmm = Utils.getSimpleDateFormat("EE, HH:mm");
	private int dayInYear = -1;
	private boolean active;
	private boolean hasBeenTriggered = false;
	private boolean hasBeenStopped = false;

	private Calendar cachedNextAlarm;
	private String cachedNextAlarmString;

	public String toDisplayableString() {
		StringBuilder sb = new StringBuilder(30);
		switch (alarmType) {
		case DAILY:
			sb.append("täglich");
			break;
		case WEEKDAYS:
			sb.append("Mo - Fr");
			break;
		case ONCE:
			sb.append("einmalig");
			break;
		case SNOOZE:
			sb.append("schlummern");
			break;
		}
		sb.append("  ");
		sb.append(alarmHour < 10 ? "0" : "");
		sb.append(alarmHour);
		sb.append(":");
		sb.append(alarmMinute < 10 ? "0" : "");
		sb.append(alarmMinute);
		sb.append(" Uhr");
		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(30);
		sb.append(property.name());
		sb.append(";");
		sb.append(alarmType.name());
		sb.append(";");
		sb.append(alarmHour);
		sb.append("_");
		sb.append(alarmMinute);
		sb.append(";");
		sb.append(active);
		return sb.toString();
	}

	public static Alarm fromString(String string) {

		String[] tokens = StringUtils.splitPreserveAllTokens(string, ';');
		String[] hhmm = StringUtils.splitPreserveAllTokens(tokens[2], '_');
		return new Alarm(Integer.valueOf(hhmm[0]), Integer.valueOf(hhmm[1]), AlarmType.valueOf(tokens[1]), Boolean.valueOf(tokens[3]),
				ApplicationProperties.valueOf(tokens[0]));
	}

	public void hasBeenTriggered() {

		actualCalendar.setTimeInMillis(CurrentDateTime.getInstance().getMillis());
		if (cachedNextAlarm != null && !cachedNextAlarm.after(actualCalendar)) {
			hasBeenTriggered = true;
			hasBeenStopped = false;
		}
	}

	public void hasBeenStopped() {

		actualCalendar.setTimeInMillis(CurrentDateTime.getInstance().getMillis());
		if (cachedNextAlarm != null && !cachedNextAlarm.after(actualCalendar) && hasBeenTriggered) {
			hasBeenStopped = true;
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

		if (alarmType == AlarmType.WEEKDAYS) {
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

		ApplicationProperties.valueOf(property.name()).setValue(toString());

		if (!active) {
			cachedNextAlarm = null;
			cachedNextAlarmString = null;
			hasBeenTriggered = false;
			hasBeenStopped = false;
			return;
		}

		actualCalendar.setTimeInMillis(CurrentDateTime.getInstance().getMillis());
		cachedNextAlarm = nextAlarmTime(CurrentDateTime.getInstance().getMillis());
		hasBeenTriggered = false;
		hasBeenStopped = false;
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

		if (cachedNextAlarm != null && !cachedNextAlarm.after(actualCalendar) && hasBeenTriggered && hasBeenStopped) {
			if (alarmType == AlarmType.ONCE || alarmType == AlarmType.SNOOZE) {
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
		refreshCache();
	}

	public AlarmType getAlarmType() {
		return alarmType;
	}

	public void setAlarmType(AlarmType alarmType) {
		this.alarmType = alarmType;
		refreshCache();
	}

}
