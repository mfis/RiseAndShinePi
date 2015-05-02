package mfi.clockworkpi.logic;

public class Alarm {

	public Alarm(int hour, int minute, boolean onWeekdaysOnly) {
		this.hour = hour;
		this.minute = minute;
		this.onWeekdaysOnly = onWeekdaysOnly;
	}

	private int hour;

	private int minute;

	private boolean onWeekdaysOnly;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(30);
		sb.append(hour);
		sb.append(":");
		sb.append(minute < 10 ? "0" : "");
		sb.append(minute);
		sb.append(" Uhr");
		return sb.toString();
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public boolean isOnWeekdaysOnly() {
		return onWeekdaysOnly;
	}

	public void setOnWeekdaysOnly(boolean onWeekdaysOnly) {
		this.onWeekdaysOnly = onWeekdaysOnly;
	}

}
