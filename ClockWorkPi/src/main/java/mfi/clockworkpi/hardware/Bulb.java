package mfi.clockworkpi.hardware;

import mfi.clockworkpi.logic.Processor;

public class Bulb {

	private Processor processor;

	public Bulb(Processor processor) {
		this.processor = processor;
	}

	public void switchTo(boolean state) {
		if (processor.isDevelopmentMode()) {
			switchToInSimulation(state);
		} else {
			switchToInHardware(state);
		}
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
		// TODO
	}

}
