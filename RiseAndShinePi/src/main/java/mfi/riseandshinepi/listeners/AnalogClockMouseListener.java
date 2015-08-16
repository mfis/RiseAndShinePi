package mfi.riseandshinepi.listeners;

import mfi.riseandshinepi.logic.Processor;

public class AnalogClockMouseListener extends AbstractLongClickMouseListener {

	public AnalogClockMouseListener(Processor processor) {
		super(processor, 0);
	}

	@Override
	public void shortClick() {
		getProcessor().toggleBulb();
	}

	@Override
	public void longClick() {
		// noop
	}

}
