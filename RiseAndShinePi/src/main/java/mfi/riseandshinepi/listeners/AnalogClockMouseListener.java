package mfi.riseandshinepi.listeners;

import mfi.riseandshinepi.hardware.CurrentDateTime;
import mfi.riseandshinepi.logic.Processor;

public class AnalogClockMouseListener extends AbstractLongClickMouseListener {

	long lastShortClick = 0;

	public AnalogClockMouseListener(Processor processor) {
		super(processor, 0);
	}

	@Override
	public void shortClick() {
		if (CurrentDateTime.getInstance().getMillis() - lastShortClick > 500) {
			lastShortClick = CurrentDateTime.getInstance().getMillis();
			getProcessor().toggleBulb();
		}
	}

	@Override
	public void longClick() {
		// noop
	}

}
