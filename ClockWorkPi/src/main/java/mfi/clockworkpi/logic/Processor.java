package mfi.clockworkpi.logic;

import mfi.clockworkpi.gui.components.MainFrame;
import mfi.clockworkpi.hardware.Bulb;
import mfi.clockworkpi.hardware.DisplayBacklight;

public class Processor {

	private boolean developmentMode = true;
	private MainFrame gui;
	private DisplayBacklight displayBacklight;
	private Bulb bulb;
	
	public Processor(boolean developmentMode) {
		this.developmentMode = developmentMode;
		gui = new MainFrame(this);
		displayBacklight = new DisplayBacklight(this);
		bulb = new Bulb(this);
		initializeHardware();
	}

	private void initializeHardware() {
		displayBacklight.dimToPercent(100);
		bulb.switchTo(false);
	}
	
	public boolean isDevelopmentMode() {
		return developmentMode;
	}

	public void setDevelopmentMode(boolean developmentMode) {
		this.developmentMode = developmentMode;
	}

	public MainFrame getGui() {
		return gui;
	}

	public void setGui(MainFrame gui) {
		this.gui = gui;
	}
	
}
