package mfi.riseandshinepi.hardware;

import mfi.riseandshinepi.logic.ApplicationProperties;
import mfi.riseandshinepi.logic.Processor;

public class Bulb {

	private Processor processor;
	private boolean state = false;

	private GPIOController bulbPowerSwitch;

	public Bulb(Processor processor) {
		this.processor = processor;
		if (!this.processor.isDevelopmentMode()) {
			bulbPowerSwitch = new GPIOController(ApplicationProperties.BULB_POWER_GPIO_PIN_NUMBER.valueAsInt(), false);
		}
	}

	public void switchTo(boolean newState) {
		if (processor.isDevelopmentMode()) {
			switchToInSimulation(newState);
		} else {
			switchToInHardware(newState);
		}
		state = newState;
	}

	private void switchToInSimulation(boolean state) {
		if (state) {
			processor.getGui().getDevelopmentPanel().getBulb().setVisible(true);
			processor.getGui().getDevelopmentPanel().getBulbBackground().setVisible(false);
		} else {
			processor.getGui().getDevelopmentPanel().getBulb().setVisible(false);
			processor.getGui().getDevelopmentPanel().getBulbBackground().setVisible(true);
		}
	}

	private void switchToInHardware(boolean state) {
		bulbPowerSwitch.setIO(state, ApplicationProperties.BULB_POWER_ON_DELAY_MILLIES.valueAsInt());
	}

	public boolean isState() {
		return state;
	}

}
