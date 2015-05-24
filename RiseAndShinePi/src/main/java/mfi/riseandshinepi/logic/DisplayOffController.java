package mfi.riseandshinepi.logic;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

public class DisplayOffController {

	// external setters

	private Date lastActivity;

	private int displayOnFixHour = 21; // TODO: UserProperties

	private int displayOffFixHour = 12; // TODO: UserProperties

	// internal

	private List<Integer> onFixHours = new LinkedList<Integer>();

	private Date autoOffDueToInactivity;

	private Date autoOnDueToAlarmTime;

	private Date now;

	private Calendar cal = new GregorianCalendar();

	private Processor processor;

	public DisplayOffController(Processor processor) {
		now = new Date();
		this.processor = processor;
		lastActivity = new Date();
	}

	public void calculate(Calendar nextAlarm, long nowInLong) {

		now.setTime(nowInLong);

		onFixHours.clear();
		int x = displayOnFixHour;
		for (int h = 0; h < 23; h++) {
			if (x > 23) {
				x = 0;
			}
			if (x == displayOffFixHour) {
				break;
			} else {
				onFixHours.add(x);
			}
			x++;
		}

		if (nextAlarm != null) {
			cal.setTimeInMillis(nextAlarm.getTimeInMillis());
			cal.add(Calendar.HOUR_OF_DAY, -1 * ApplicationProperties.DISPLAY_ON_X_HOURS_BEFORE_ALARM.valueAsInt());
			autoOnDueToAlarmTime = new Date(cal.getTimeInMillis());
		} else {
			autoOnDueToAlarmTime = null;
		}

		cal.setTimeInMillis(nowInLong);
		cal.add(Calendar.MINUTE, ApplicationProperties.DISPLAY_OFF_X_MINUTES_IN_INACTIVITY.valueAsInt());
		autoOffDueToInactivity = new Date(cal.getTimeInMillis());

		if (processor.isDevelopmentMode()) {
			System.out.println("---------------------------------------------");
			System.out.println("autoOffDueToInactivity = " + autoOffDueToInactivity);
			System.out.println("autoOnDueToAlarmTime = " + autoOnDueToAlarmTime);
			System.out.println("onFixHours = " + onFixHours.toString());
			System.out.println("---------------------------------------------");
		}
	}

	public boolean autoOffNow(long nowInLong) {
		now.setTime(nowInLong);
		if (autoOnNow(nowInLong)) {
			return false;
		}
		return now.after(autoOffDueToInactivity);
	}

	public boolean autoOnNow(long nowInLong) {
		now.setTime(nowInLong);
		cal.setTimeInMillis(nowInLong);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		return onFixHours.contains(hour) || (autoOnDueToAlarmTime != null && now.after(autoOnDueToAlarmTime));
	}

	public void setLastActivity(long now) {
		this.lastActivity.setTime(now);
	}

	public int getDisplayOnFixHour() {
		return displayOnFixHour;
	}

	public void setDisplayOnFixHour(int displayOnFixHour) {
		this.displayOnFixHour = displayOnFixHour;
	}

	public int getDisplayOffFixHour() {
		return displayOffFixHour;
	}

	public void setDisplayOffFixHour(int displayOffFixHour) {
		this.displayOffFixHour = displayOffFixHour;
	}

}
