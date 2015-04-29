package mfi.clockworkpi.hardware;

import java.awt.Color;

import mfi.clockworkpi.logic.Processor;

public class DisplayBacklight {

	private Processor processor;

	public DisplayBacklight(Processor processor) {
		this.processor = processor;
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
		// TODO
	}

}
