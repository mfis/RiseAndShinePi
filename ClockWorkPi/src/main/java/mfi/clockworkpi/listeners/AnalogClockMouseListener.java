package mfi.clockworkpi.listeners;

import mfi.clockworkpi.gui.cardpanes.BlankPane;
import mfi.clockworkpi.logic.Processor;

public class AnalogClockMouseListener extends AbstractLongClickMouseListener {

	public AnalogClockMouseListener(Processor processor) {
		super(processor, 2000);
	}

	@Override
	public void shortClick() {
		getProcessor().toggleBulb();

	}

	@Override
	public void longClick() {
		getProcessor().switchGuiTo(BlankPane.class.getName());
	}

}
