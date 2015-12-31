package mfi.riseandshinepi.logic;

import java.util.Timer;
import mfi.riseandshinepi.gui.cardpanes.AlarmPane;
import mfi.riseandshinepi.gui.cardpanes.BlankPane;
import mfi.riseandshinepi.gui.cardpanes.ClockPane;
import mfi.riseandshinepi.gui.cardpanes.VolumeAndBacklightSettingsPane;
import mfi.riseandshinepi.gui.components.Gui;
import mfi.riseandshinepi.hardware.AudioPlayer;
import mfi.riseandshinepi.hardware.Bulb;
import mfi.riseandshinepi.hardware.CurrentDateTime;
import mfi.riseandshinepi.hardware.DisplayBacklight;

public class Processor implements Constants {

	private boolean developmentMode = true;
	private Gui gui;
	private DisplayBacklight displayBacklight;
	private Bulb bulb;
	private AudioPlayer audioPlayer;

	private DisplayOffController displayOffController;
	private WeatherController weatherController;
	private AlarmController alarmController;

	private Timer alarmTimer;
	private Timer weatherTimer;

	private boolean alarmNowOn = false;

	public Processor(boolean developmentMode) {

		super();
		ApplicationProperties.init();

		this.developmentMode = developmentMode;
		displayBacklight = new DisplayBacklight(this);
		bulb = new Bulb(this);
		alarmTimer = new Timer();
		weatherTimer = new Timer();
	}

	public void initialize() {
		displayOffController = new DisplayOffController(this);
		displayOffController.newActivity();
		weatherController = new WeatherController(this);
		alarmController = new AlarmController(this);
		gui = new Gui(this);
		gui.paintGui();
		initializeHardware();

		// every 5 sec
		alarmTimer.schedule(new AlarmTimerTask(this), 1003, 5003);
		// every min
		weatherTimer.schedule(new WeatherTimerTask(this), 1000 * 2, 1000 * 60);
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
			alarmController.calculateNextAlarm();
		}

		gui.switchGuiTo(name);

		weatherController.refreshWeather(true);
		checkBacklightOffset();
	}

	public void processDisplayAutoOff() {

		if (gui.getActualPaneName().equals(BlankPane.class.getName())) {
			if (displayOffController.autoOnNow(CurrentDateTime.getInstance().getMillis())) {
				switchGuiTo(ClockPane.class.getName());
			}
		} else {
			if (!bulb.isState()) {
				if (displayOffController.autoOffNow(CurrentDateTime.getInstance().getMillis())) {
					switchGuiTo(BlankPane.class.getName());
				}
			}
		}

	}

	public void alarmOn(Alarm alarm) {

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

		alarmController.markForetimeAlarmsAsStopped();

		alarmController.calculateNextAlarm();
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

	public void shutdown() {

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

		gui.shutdown();
		if (!isDevelopmentMode()) {
			try {
				gui.getDevice().setFullScreenWindow(null);
			} catch (Exception e) {
				// noop
			}
		}

		ApplicationProperties.store();

		System.exit(0);
	}

	public boolean isDevelopmentMode() {
		return developmentMode;
	}

	public Gui getGui() {
		return gui;
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

	public AlarmController getAlarmController() {
		return alarmController;
	}

}
