package mfi.riseandshinepi.hardware;

import java.awt.Color;

import mfi.riseandshinepi.logic.ApplicationProperties;
import mfi.riseandshinepi.logic.Processor;

public class DisplayBacklight {

	private Processor processor;

	private GPIOController backlightModulator;

	private int actualPercent;
	private int defaultPercent = ApplicationProperties.DISPLAY_BACKLIGHT_DEFAULT_PERCENT.valueAsInt();

	public DisplayBacklight(Processor processor) {
		this.processor = processor;
		if (!this.processor.isDevelopmentMode()) {
			backlightModulator = new GPIOController(ApplicationProperties.DISPLAY_BACKLIGHT_DIMMING_GPIO_PIN_NUMBER.valueAsInt(), true);
		}
	}

	public void dimToPercent(int percent) {
		actualPercent = percent;
		if (processor.isDevelopmentMode()) {
			dimToPercentInSimulation(percent);
		} else {
			dimToPercentInHardware(percent);
		}
	}

	private void dimToPercentInSimulation(int percent) {
		Color color;
		if (percent == 0) {
			color = new Color(0, 0, 0);
		} else if (percent == 100) {
			color = new Color(255, 255, 255);
		} else {
			float rgb = 255f / 100f * (percent);
			int rgbInt = (int) rgb;
			color = new Color(rgbInt, rgbInt, rgbInt);
		}
		processor.getGui().getDevelopmentPanel().getBacklightPane().setBackground(color);
	}

	private void dimToPercentInHardware(int percent) {
		backlightModulator.setPWM(percent * 10, 0); // hardware max = 1023
	}

	public int getActualPercent() {
		return actualPercent;
	}

	public int getDefaultPercent() {
		return defaultPercent;
	}

	public void setDefaultPercent(int defaultPercent) {
		this.defaultPercent = defaultPercent;
	}

}
