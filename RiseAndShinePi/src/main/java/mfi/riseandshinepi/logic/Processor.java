package mfi.riseandshinepi.logic;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import mfi.riseandshinepi.gui.cardpanes.AlarmPane;
import mfi.riseandshinepi.gui.cardpanes.BlankPane;
import mfi.riseandshinepi.gui.cardpanes.ClockPane;
import mfi.riseandshinepi.gui.cardpanes.VolumeAndBacklightSettingsPane;
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
	private DisplayOffController displayOffController;
	private WeatherController weatherController;

	private Timer weatherTimer;

	private List<Alarm> alarms;
	private boolean alarmNowOn = false;

	public Processor(boolean developmentMode) {
		super();
		ApplicationProperties.init();
		actualCalendar = new GregorianCalendar();
		this.developmentMode = developmentMode;
		displayBacklight = new DisplayBacklight(this);
		bulb = new Bulb(this);
		alarms = new LinkedList<Alarm>();
		alarms.add(new Alarm(5, 12, true, false, false, false)); // FIXME:
																	// porperties
		alarms.add(new Alarm(9, 0, false, false, false, false)); // FIXME:
																	// porperties
		alarms.add(new Alarm(11, 30, false, true, true, false)); // FIXME:
																	// porperties
		// (only
		// time)
		alarmTimer = new Timer();
		weatherTimer = new Timer();
	}

	public void initialize() {
		displayOffController = new DisplayOffController(this);
		displayOffController.newActivity();
		weatherController = new WeatherController(this);
		gui = new Gui(this);
		gui.paintGui();
		initializeHardware();
		alarmTimer.schedule(new AlarmTimerTask(this), 1003, 5003); // every 5
																	// sec
		weatherTimer.schedule(new WeatherTimerTask(this), 1000 * 2, 1000 * 60); // every
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

		if (name.equals(ClockPane.class.getName())) {
			calculateNextAlarm();
		}

		gui.switchGuiTo(name);

		weatherController.refreshWeather(true);
		checkBacklightOffset();
	}

	public synchronized Alarm calculateNextAlarm() {

		actualCalendar.setTimeInMillis(System.currentTimeMillis());
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

		displayOffController.calculate(next != null ? next.getCachedNextAlarm() : null);
		gui.setAlarmTimeString(next != null ? next.getCachedNextAlarmString() : null);

		return next;
	}

	public void processAlarmTimer() {

		actualCalendar.setTimeInMillis(System.currentTimeMillis());
		Alarm next = calculateNextAlarm();

		if (next == null) {
			if (alarmNowOn) {
				alarmOff();
			}
			return;
		}

		// If alarm is on more than an hour, turn off
		if (alarmNowOn && (next.getCachedNextAlarm().getTimeInMillis() + (oneHourInMilliSeconds * 2)) < actualCalendar.getTimeInMillis()) {
			alarmOff();
			return;
		}

		if (alarmNowOn) {
			return;
		}

		if (!next.getCachedNextAlarm().after(actualCalendar)) {
			alarmOn(next);
			return;
		}
	}

	public void processDisplayAutoOff() {

		if (gui.getActualPaneName().equals(BlankPane.class.getName())) {
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

	private void alarmOn(Alarm alarm) {

		if (alarmNowOn) {
			return;
		}

		alarmNowOn = true;
		turnOnBulb();
		audioPlayer.start();
		alarm.hasBeenTriggered();

		switchGuiTo(AlarmPane.class.getName());
	}

	public void alarmOff() {

		alarmNowOn = false;
		turnOffBulb();
		if (audioPlayer.isPlaying()) {
			audioPlayer.stop();
		}

		// mark overtaken alarms as triggered
		actualCalendar.setTimeInMillis(System.currentTimeMillis());
		for (Alarm a : alarms) {
			Calendar alarmCal = a.getCachedNextAlarm();
			if (alarmCal != null) {
				if (!alarmCal.after(actualCalendar)) {
					a.hasBeenTriggered();
				}
			}
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

	public void checkBacklightOffset() {
		boolean offset = (bulb.isState() || weatherController.isDaylightTime())
				&& !gui.getActualPaneName().equals(VolumeAndBacklightSettingsPane.class.getName());
		displayBacklight.useOffset(offset);
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
