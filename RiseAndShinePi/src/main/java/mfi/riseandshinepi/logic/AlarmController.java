package mfi.riseandshinepi.logic;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import mfi.riseandshinepi.hardware.CurrentDateTime;

public class AlarmController implements Constants {

	private Processor processor;
	private Calendar actualCalendar;

	private List<Alarm> alarms;

	public AlarmController(Processor processor) {

		this.processor = processor;
		actualCalendar = new GregorianCalendar();
		alarms = new LinkedList<Alarm>();

		alarms.add(new Alarm(5, 12, true, false, false, false)); // FIXME:
																	// porperties
		alarms.add(new Alarm(9, 0, false, false, false, false)); // FIXME:
																	// porperties
		alarms.add(new Alarm(11, 30, false, true, true, false)); // FIXME:
																	// porperties
	}

	public synchronized Alarm calculateNextAlarm() {

		Alarm next = null;
		for (Alarm a : alarms) {
			Calendar aNextAlarm = a.getCachedNextAlarm();
			if (aNextAlarm != null) {
				if (next == null) {
					next = a;
				} else {
					if (aNextAlarm.before(next.getCachedNextAlarm())) {
						next = a;
					}
				}
			}
		}

		processor.getDisplayOffController().calculate(next != null ? next.getCachedNextAlarm() : null);
		processor.getGui().setAlarmTimeString(next != null ? next.getCachedNextAlarmString() : null);

		return next;
	}

	public void processAlarmTimer() {

		actualCalendar.setTimeInMillis(CurrentDateTime.getInstance().getMillis());
		Alarm next = calculateNextAlarm();

		if (next == null) {
			if (processor.isAlarmNowOn()) {
				processor.alarmOff();
			}
			return;
		}

		// If alarm is on more than an hour, turn off
		if (processor.isAlarmNowOn() && (next.getCachedNextAlarm().getTimeInMillis() + (oneHourInMilliSeconds * 2)) < actualCalendar.getTimeInMillis()) {
			processor.alarmOff();
			return;
		}

		if (processor.isAlarmNowOn()) {
			return;
		}

		if (!next.getCachedNextAlarm().after(actualCalendar)) {
			processor.alarmOn(next);
			return;
		}
	}

	public void activateSnoozeAlarm(int minutes) {

		for (Alarm alarm : processor.getAlarmController().getAlarms()) {
			if (alarm.isSnooze()) {
				Calendar calendar = new GregorianCalendar();
				calendar.setTimeInMillis(CurrentDateTime.getInstance().getMillis());
				calendar.add(Calendar.MINUTE, minutes);
				alarm.setHour(calendar.get(Calendar.HOUR_OF_DAY));
				alarm.setMinute(calendar.get(Calendar.MINUTE));
				alarm.setActive(true);
				break;
			}
		}
	}

	public void markForetimeAlarmsAsStopped() {

		// mark alarms incl overtaken ones alarms as triggered and stopped
		actualCalendar.setTimeInMillis(CurrentDateTime.getInstance().getMillis());
		for (Alarm a : alarms) {
			Calendar alarmCal = a.getCachedNextAlarm();
			if (alarmCal != null) {
				if (!alarmCal.after(actualCalendar)) {
					a.hasBeenTriggered();
					a.hasBeenStopped();
				}
			}
		}
	}

	public List<Alarm> getAlarms() {
		return alarms;
	}

}
