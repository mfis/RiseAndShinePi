package mfi.riseandshinepi.logic;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;

import mfi.riseandshinepi.gui.cardpanes.AlarmPane;
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
	private int dayInYear = -1;
	private Timer alarmTimer;
	private DisplayOffController displayOffController;
	private WeatherController weatherController;

	private Timer weatherTimer;

	private List<Alarm> alarms;
	private Integer activeAlarm = null;
	private Calendar nextAlarm = null;
	private String nextAlarmString = null;
	private boolean alarmNowOn = false;
	private boolean alarmStateDirty;

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
		weatherTimer = new Timer();
	}

	public void initialize() {
		displayOffController = new DisplayOffController(this);
		displayOffController.setLastActivity(System.currentTimeMillis());
		weatherController = new WeatherController(this);
		gui = new Gui(this);
		gui.paintGui();
		initializeHardware();
		alarmTimer.schedule(new AlarmTimerTask(this), 1003, 1003); // every sec
		weatherTimer.schedule(new WeatherTimerTask(this), 1000 * 2, 1000 * 60 * 20); // every
																						// 20
																						// min
	}

	private void initializeHardware() {
		switchGuiTo(ClockPane.class.getName());
		displayBacklight.dimToValue(displayBacklight.getDefaultValue());
		bulb.switchTo(false);
		audioPlayer = new AudioPlayer(this);
		turnOffBulb();
	}

	public void switchGuiTo(String name) {

		if (name.equals(BlankPane.class.getName())) {
			displayBacklight.dimToValue(0);
			turnOffBulb();
		} else {
			displayBacklight.dimToValue(displayBacklight.getDefaultValue());
		}

		gui.switchGuiTo(name);

		weatherController.refreshWeather();
	}

	public synchronized void calculateNextAlarm() {

		if (activeAlarm == null) {
			nextAlarm = null;
			nextAlarmString = null;
			alarmStateDirty = false;
			displayOffController.calculate(nextAlarm, System.currentTimeMillis());
			return;
		}

		Alarm a = alarms.get(activeAlarm);
		nextAlarm = a.nextAlarmTime(System.currentTimeMillis());
		actualCalendar.setTimeInMillis(System.currentTimeMillis());
		nextAlarmString = Alarm.nextAlarmTimeStringFor(nextAlarm, actualCalendar);
		displayOffController.calculate(nextAlarm, System.currentTimeMillis());
		alarmStateDirty = false;
	}

	public String nextAlarmTimeString() {

		if (alarmStateDirty) {
			calculateNextAlarm();
		}
		return nextAlarmString;
	}

	public void processAlarmTimer() {

		actualCalendar.setTimeInMillis(System.currentTimeMillis());
		int newDayInYear = actualCalendar.get(Calendar.DAY_OF_YEAR);
		if (newDayInYear != dayInYear) {
			alarmStateDirty = true;
			dayInYear = newDayInYear;
		}

		if (alarmStateDirty) {
			calculateNextAlarm();
		}

		if (nextAlarm == null) {
			if (alarmNowOn) {
				alarmOff();
			}
			return;
		}

		// If alarm is on more than an hour, turn off
		if (alarmNowOn
				&& (nextAlarm.getTimeInMillis() + (oneHourInMilliSeconds * 2)) < actualCalendar.getTimeInMillis()) {
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

	public void processDisplayAutoOff() {

		if (gui.getActualPane().equals(BlankPane.class.getName())) {
			if (displayOffController.autoOnNow(System.currentTimeMillis())) {
				switchGuiTo(ClockPane.class.getName());
			}
		} else {
			if (!bulb.isState()) {
				if (displayOffController.autoOffNow(System.currentTimeMillis())) {
					switchGuiTo(BlankPane.class.getName());
				}
			}
		}

	}

	private void alarmOn() {

		alarmNowOn = true;
		turnOnBulb();
		audioPlayer.start();

		switchGuiTo(AlarmPane.class.getName());
	}

	public void alarmOff() {
		alarmNowOn = false;
		turnOffBulb();
		if (audioPlayer.isPlaying()) {
			audioPlayer.stop();
		}
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

		if (alarmNowOn) {
			alarmOff();
		}

		alarmTimer.cancel();
		alarmTimer.purge();

		weatherTimer.cancel();
		weatherTimer.purge();

		audioPlayer.stop();
		turnOffBulb();
		displayBacklight.dimToValue(DisplayBacklight.MAX_VALUE);

		if (!isDevelopmentMode()) {
			try {
				gui.getDevice().setFullScreenWindow(null);
			} catch (Exception e) {
			}
		}

		ApplicationProperties.store();
		
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

	public DisplayBacklight getDisplayBacklight() {
		return displayBacklight;
	}

	public DisplayOffController getDisplayOffController() {
		return displayOffController;
	}

	public AudioPlayer getAudioPlayer() {
		return audioPlayer;
	}

	public WeatherController getWeatherController() {
		return weatherController;
	}

	public Calendar getActualCalendar() {
		return actualCalendar;
	}

}
