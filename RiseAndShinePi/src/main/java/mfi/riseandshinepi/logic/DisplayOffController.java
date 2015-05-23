package mfi.riseandshinepi.logic;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DisplayOffController {

	// external setters

	private Date lastActivity;

	private int displayOnFixHour = 21; // TODO: UserProperties

	private int displayOffFixHour = 12; // TODO: UserProperties

	// internal

	private Date autoOffDueToFixTime;

	private Date autoOffDueToInactivity;

	private Date autoOnDueToFixTime;

	private Date autoOnDueToAlarmTime;

	private Date now;

	private Alarm calculator;

	@SuppressWarnings("unused")
	private Processor processor;

	public DisplayOffController(Processor processor) {
		now = new Date();
		this.processor = processor;
		lastActivity = new Date();
		calculator = new Alarm(0, 0, false, true);
	}

	public void calculate(Calendar nextAlarm, long nowInLong) {

		now.setTime(nowInLong);

		calculator.setHour(displayOffFixHour);
		autoOffDueToFixTime = calculator.nextAlarmTime(nowInLong).getTime();

		calculator.setHour(displayOnFixHour);
		autoOnDueToFixTime = calculator.nextAlarmTime(nowInLong).getTime();

		Calendar cal = new GregorianCalendar();

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

		// if (processor.isDevelopmentMode()) {
		// System.out.println("---------------------------------------------");
		// System.out.println("autoOffDueToFixTime = " + autoOffDueToFixTime);
		// System.out.println("autoOffDueToInactivity = " +
		// autoOffDueToInactivity);
		// System.out.println("autoOnDueToFixTime = " + autoOnDueToFixTime);
		// System.out.println("autoOnDueToAlarmTime = " + autoOnDueToAlarmTime);
		// System.out.println("---------------------------------------------");
		// }
	}

	public boolean autoOffNow(long nowInLong) {
		now.setTime(nowInLong);
		if (autoOnNow(nowInLong)) {
			return false;
		}
		return now.after(autoOffDueToInactivity) || now.after(autoOffDueToFixTime);
	}

	public boolean autoOnNow(long nowInLong) {
		now.setTime(nowInLong);
		return now.after(autoOnDueToFixTime) || (autoOnDueToAlarmTime != null && now.after(autoOnDueToAlarmTime));
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
