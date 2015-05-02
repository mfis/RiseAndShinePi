package mfi.clockworkpi.listeners;

import mfi.clockworkpi.gui.cardpanes.ClockPane;
import mfi.clockworkpi.logic.Processor;

public class BlankPaneMouseListener extends AbstractLongClickMouseListener {

	public BlankPaneMouseListener(Processor processor) {
		super(processor, 5000);
	}

	@Override
	public void shortClick() {
		getProcessor().switchGuiTo(ClockPane.class.getName());

	}

	@Override
	public void longClick() {
		getProcessor().exit();
	}

}
