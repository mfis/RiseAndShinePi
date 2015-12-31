package mfi.riseandshinepi.listeners;

import mfi.riseandshinepi.gui.cardpanes.SnoozePane;
import mfi.riseandshinepi.hardware.CurrentDateTime;
import mfi.riseandshinepi.logic.Processor;

public class AnalogClockMouseListener extends AbstractLongClickMouseListener {

	long lastShortClick = 0;

	public AnalogClockMouseListener(Processor processor) {
		super(processor, 2000);
	}

	@Override
	public void shortClick() {
		if (CurrentDateTime.getInstance().getMillis() - lastShortClick > 650) {
			lastShortClick = CurrentDateTime.getInstance().getMillis();
			getProcessor().toggleBulb();
		}
	}

	@Override
	public void longClick() {
		getProcessor().turnOnBulb();
		getProcessor().switchGuiTo(SnoozePane.class.getName());
	}

}
