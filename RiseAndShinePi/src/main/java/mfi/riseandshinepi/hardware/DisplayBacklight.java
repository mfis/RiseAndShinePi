package mfi.riseandshinepi.hardware;

import java.awt.Color;

import mfi.riseandshinepi.logic.ApplicationProperties;
import mfi.riseandshinepi.logic.Processor;

public class DisplayBacklight {

	private Processor processor;
	private GPIOController backlightModulator;

	public final static int MIN_VALUE = 1;
	public final static int MAX_VALUE = 20;

	private int actualValue;
	private int defaultValue = ApplicationProperties.DISPLAY_BACKLIGHT_DEFAULT_VALUE.valueAsInt();

	public DisplayBacklight(Processor processor) {
		this.processor = processor;
		if (!this.processor.isDevelopmentMode()) {
			backlightModulator = new GPIOController(
					ApplicationProperties.DISPLAY_BACKLIGHT_DIMMING_GPIO_PIN_NUMBER.valueAsInt(), true);
		}
	}

	public void dimToValue(int value) {
		actualValue = value;
		if (processor.isDevelopmentMode()) {
			dimToValueInSimulation(value);
		} else {
			dimToValueInHardware(value);
		}
	}

	private void dimToValueInSimulation(int value) {
		Color color;
		if (value == 0) {
			color = new Color(0, 0, 0);
		} else if (value == 100) {
			color = new Color(255, 255, 255);
		} else {
			float rgb = 255f / MAX_VALUE * (value);
			int rgbInt = (int) rgb;
			color = new Color(rgbInt, rgbInt, rgbInt);
		}
		processor.getGui().getDevelopmentPanel().getBacklightPane().setBackground(color);
	}

	private void dimToValueInHardware(int value) {
		int y = (int) Math.round((Math.pow(value, 3) / 12f) + (value * 1));
		backlightModulator.setPWM(y, 0); // hardware max = 1023
	}

	public int getActualValue() {
		return actualValue;
	}

	public int getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(int defaultValue) {
		this.defaultValue = defaultValue;
		ApplicationProperties.DISPLAY_BACKLIGHT_DEFAULT_VALUE.setValue(defaultValue + "");
	}

}
