package mfi.clockworkpi.hardware;

import mfi.clockworkpi.logic.Processor;

public class Bulb {

	private Processor processor;
	private boolean state = false;

	public Bulb(Processor processor) {
		this.processor = processor;
		if (!processor.isDevelopmentMode()) {
			switchTo(false);
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
		// TODO
	}

	public boolean isState() {
		return state;
	}

}
