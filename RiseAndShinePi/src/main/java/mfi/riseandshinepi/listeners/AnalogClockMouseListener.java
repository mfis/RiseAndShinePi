package mfi.riseandshinepi.listeners;

import mfi.riseandshinepi.logic.Processor;

public class AnalogClockMouseListener extends AbstractLongClickMouseListener {

	long lastShortClick = 0;

	public AnalogClockMouseListener(Processor processor) {
		super(processor, 0);
	}

	@Override
	public void shortClick() {
		if (System.currentTimeMillis() - lastShortClick > 500) {
			lastShortClick = System.currentTimeMillis();
			getProcessor().toggleBulb();
		}
	}

	@Override
	public void longClick() {
		// noop
	}

}
