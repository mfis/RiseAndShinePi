package mfi.riseandshinepi.hardware;

import java.awt.Color;

import mfi.riseandshinepi.logic.ApplicationProperties;
import mfi.riseandshinepi.logic.Processor;

public class DisplayBacklight {

	private Processor processor;

	private GPIOController backlightModulator;

	public DisplayBacklight(Processor processor) {
		this.processor = processor;
		if (!this.processor.isDevelopmentMode()) {
			backlightModulator = new GPIOController(
					ApplicationProperties.DISPLAY_BACKLIGHT_DIMMING_GPIO_PIN_NUMBER
							.valueAsInt(), true);
		}
	}

	public void dimToPercent(int percent) {
		if (processor.isDevelopmentMode()) {
			dimToPercentInSimulation(percent);
		} else {
			dimToPercentInHardware(percent);
		}
	}

	private void dimToPercentInSimulation(int percent) {
		if (percent == 0) {
			processor.getGui().getDevelopmentPanel().getBacklightPane()
					.setBackground(Color.BLACK);
		} else if (percent < 30) {
			processor.getGui().getDevelopmentPanel().getBacklightPane()
					.setBackground(Color.DARK_GRAY);
		} else if (percent < 50) {
			processor.getGui().getDevelopmentPanel().getBacklightPane()
					.setBackground(Color.GRAY);
		} else if (percent < 100) {
			processor.getGui().getDevelopmentPanel().getBacklightPane()
					.setBackground(Color.LIGHT_GRAY);
		} else {
			processor.getGui().getDevelopmentPanel().getBacklightPane()
					.setBackground(Color.WHITE);
		}
	}

	private void dimToPercentInHardware(int percent) {
		backlightModulator.setPWM(percent * 10, 0); // hardware max = 1023
	}

}
