package mfi.clockworkpi.logic;

import java.util.LinkedList;
import java.util.List;

import mfi.clockworkpi.gui.cardpanes.BlankPane;
import mfi.clockworkpi.gui.cardpanes.ClockPane;
import mfi.clockworkpi.gui.components.Gui;
import mfi.clockworkpi.hardware.Bulb;
import mfi.clockworkpi.hardware.DisplayBacklight;

public class Processor {

	private boolean developmentMode = true;
	private Gui gui;
	private DisplayBacklight displayBacklight;
	private Bulb bulb;

	private List<Alarm> alarms;
	private Integer activeAlarm = null;

	private int backlightLevel = 100;

	public Processor(boolean developmentMode) {
		this.developmentMode = developmentMode;
		displayBacklight = new DisplayBacklight(this);
		bulb = new Bulb(this);
		alarms = new LinkedList<Alarm>();
		alarms.add(new Alarm(5, 12, true));
		alarms.add(new Alarm(9, 0, false));
	}

	public void initialize() {
		gui = new Gui(this);
		gui.paintGui();
		initializeHardware();
	}

	private void initializeHardware() {
		switchGuiTo(ClockPane.class.getName());
		displayBacklight.dimToPercent(backlightLevel);
		turnOffBulb();
	}

	public void switchGuiTo(String name) {

		if (name.equals(BlankPane.class.getName())) {
			displayBacklight.dimToPercent(0);
			turnOffBulb();
		} else {
			displayBacklight.dimToPercent(backlightLevel);
		}

		gui.switchGuiTo(name);
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
		// FIXME: device.setFullScreenWindow(null);
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

	public Integer getActiveAlarm() {
		return activeAlarm;
	}

	public void setActiveAlarm(Integer activeAlarm) {
		this.activeAlarm = activeAlarm;
	}

}
