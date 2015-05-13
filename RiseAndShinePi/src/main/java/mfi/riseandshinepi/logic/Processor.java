package mfi.riseandshinepi.logic;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;

import mfi.riseandshinepi.gui.cardpanes.BlankPane;
import mfi.riseandshinepi.gui.cardpanes.ClockPane;
import mfi.riseandshinepi.gui.components.Gui;
import mfi.riseandshinepi.hardware.AudioPlayer;
import mfi.riseandshinepi.hardware.Bulb;
import mfi.riseandshinepi.hardware.DisplayBacklight;

public class Processor implements Constants {

	private boolean developmentMode = true;
	private Gui gui;
	private DisplayBacklight displayBacklight;
	private Bulb bulb;
	private AudioPlayer audioPlayer;

	private Calendar actualCalendar;
	private Timer alarmTimer;

	private List<Alarm> alarms;
	private Integer activeAlarm = null;
	private Calendar nextAlarm = null;
	private String nextAlarmString = null;
	private boolean alarmNowOn = false;
	private boolean alarmStateDirty;

	private int backlightLevel = 90; // FIXME: user properties

	public Processor(boolean developmentMode) {
		super();
		ApplicationProperties.init();
		actualCalendar = new GregorianCalendar();
		alarmStateDirty = true;
		this.developmentMode = developmentMode;
		displayBacklight = new DisplayBacklight(this);
		bulb = new Bulb(this);
		alarms = new LinkedList<Alarm>();
		alarms.add(new Alarm(5, 12, true, false));
		alarms.add(new Alarm(9, 0, false, false));
		alarms.add(new Alarm(11, 30, false, true));
		alarmTimer = new Timer();
	}

	public void initialize() {
		gui = new Gui(this);
		gui.paintGui();
		initializeHardware();
		alarmTimer.schedule(new AlarmTimerTask(this), 1003, 1003);
	}

	private void initializeHardware() {
		switchGuiTo(ClockPane.class.getName());
		displayBacklight.dimToPercent(backlightLevel);
		bulb.switchTo(false);
		audioPlayer = new AudioPlayer(this);
		turnOffBulb();
	}

	public void switchGuiTo(String name) {

		if (alarmNowOn) {
			alarmOff();
		}

		alarmStateDirty = true;

		if (name.equals(BlankPane.class.getName())) {
			displayBacklight.dimToPercent(0);
			turnOffBulb();
		} else {
			displayBacklight.dimToPercent(backlightLevel);
		}

		gui.switchGuiTo(name);
	}

	public synchronized void calculateNextAlarm() {

		if (activeAlarm == null) {
			nextAlarm = null;
			nextAlarmString = null;
			alarmStateDirty = false;
			return;
		}

		Alarm a = alarms.get(activeAlarm);
		nextAlarm = a.nextAlarmTime();
		actualCalendar.setTimeInMillis(System.currentTimeMillis());
		nextAlarmString = Alarm.nextAlarmTimeStringFor(nextAlarm,
				actualCalendar);
		alarmStateDirty = false;
	}

	public String nextAlarmTimeString() {

		if (alarmStateDirty) {
			calculateNextAlarm();
		}
		return nextAlarmString;
	}

	public void processAlarmTimer() {

		if (alarmStateDirty) {
			calculateNextAlarm();
		}

		if (nextAlarm == null) {
			if (alarmNowOn) {
				alarmOff();
			}
			return;
		}

		actualCalendar.setTimeInMillis(System.currentTimeMillis());

		// If alarm is on more than an hour, turn off
		if (alarmNowOn
				&& (nextAlarm.getTimeInMillis() + (oneHourInMilliSeconds * 2)) < actualCalendar
						.getTimeInMillis()) {
			alarmOff();
			return;
		}

		if (alarmNowOn) {
			return;
		}

		if (nextAlarm.before(actualCalendar)) {
			alarmOn();
			return;
		}
	}

	private void alarmOn() {
		nextAlarmString = "jetzt";
		alarmNowOn = true;
		turnOnBulb();
		audioPlayer.start();
	}

	public void alarmOff() {
		alarmNowOn = false;
		turnOffBulb();
		audioPlayer.stop();
		if (alarms.get(activeAlarm).isOnce()) {
			activeAlarm = null;
		}
		calculateNextAlarm();
	}

	public void turnOffBulb() {
		bulb.switchTo(false);
	}

	public void turnOnBulb() {
		bulb.switchTo(true);
	}

	public void toggleBulb() {
		bulb.switchTo(!bulb.isState());
	}

	public void exit() {

		alarmTimer.cancel();
		alarmTimer.purge();

		audioPlayer.stop();
		turnOffBulb();
		displayBacklight.dimToPercent(90);

		// FIXME: device.setFullScreenWindow(null);
		System.exit(0);
	}

	public void setAlarmStateToDirty() {
		alarmStateDirty = true;
	}

	public boolean isDevelopmentMode() {
		return developmentMode;
	}

	public void setDevelopmentMode(boolean developmentMode) {
		this.developmentMode = developmentMode;
	}

	public Gui getGui() {
		return gui;
	}

	public void setGui(Gui gui) {
		this.gui = gui;
	}

	public List<Alarm> getAlarms() {
		return alarms;
	}

	public Integer getActiveAlarm() {
		return activeAlarm;
	}

	public void setActiveAlarm(Integer activeAlarm) {
		this.activeAlarm = activeAlarm;
	}

	public boolean isAlarmNowOn() {
		return alarmNowOn;
	}

}
