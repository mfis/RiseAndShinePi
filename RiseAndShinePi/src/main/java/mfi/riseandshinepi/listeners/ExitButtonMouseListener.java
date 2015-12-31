package mfi.riseandshinepi.listeners;

import mfi.riseandshinepi.logic.Processor;

public class ExitButtonMouseListener extends AbstractLongClickMouseListener {

	public ExitButtonMouseListener(Processor processor) {
		super(processor, 2000);
	}

	@Override
	public void shortClick() {
		// noop
	}

	@Override
	public void longClick() {
		getProcessor().shutdown();
	}

}
