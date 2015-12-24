package mfi.riseandshinepi.logic;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import mfi.riseandshinepi.hardware.CurrentDateTime;

public class DisplayOffController {

	// external setters

	private long lastActivity;

	private int displayOnFixHour = 21; // TODO: UserProperties

	private int displayOffFixHour = 12; // TODO: UserProperties

	// internal

	private List<Integer> onFixHours = new LinkedList<Integer>();

	private Date autoOffDueToInactivity;

	private Date autoOnDueToAlarmTime;

	private Date now;

	private Calendar cal = new GregorianCalendar();

	private Processor processor;

	private long calculatedForLastActivity = 0;

	private Long calculatedForNextAlarm = null;

	public DisplayOffController(Processor processor) {
		now = new Date();
		this.processor = processor;
		lastActivity = CurrentDateTime.getInstance().getMillis();
	}

	public void calculate(Calendar nextAlarm) {

		boolean changedLastActivity = calculatedForLastActivity != lastActivity;

		boolean changedNextAlarm = false;
		if (calculatedForNextAlarm == null && nextAlarm == null) {
			changedNextAlarm = false;
		} else if ((nextAlarm != null && calculatedForNextAlarm == null) || (nextAlarm == null && calculatedForNextAlarm != null)
				|| (nextAlarm.getTimeInMillis() != calculatedForNextAlarm)) {
			changedNextAlarm = true;
		}

		if (!changedLastActivity && !changedNextAlarm) {
			return;
		}

		calculatedForLastActivity = lastActivity;
		calculatedForNextAlarm = nextAlarm != null ? nextAlarm.getTimeInMillis() : null;

		long nowInLong = CurrentDateTime.getInstance().getMillis();

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
			System.out.println("autoOffDueToInactivity = " + autoOffDueToInactivity + " autoOnDueToAlarmTime = " + autoOnDueToAlarmTime + " onFixHours = "
					+ onFixHours.toString());
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

	public void newActivity() {
		this.lastActivity = CurrentDateTime.getInstance().getMillis();
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
