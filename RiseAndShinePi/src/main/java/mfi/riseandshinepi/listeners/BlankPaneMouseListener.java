package mfi.riseandshinepi.listeners;

import mfi.riseandshinepi.gui.cardpanes.ClockPane;
import mfi.riseandshinepi.logic.Processor;

public class BlankPaneMouseListener extends AbstractLongClickMouseListener {

	public BlankPaneMouseListener(Processor processor) {
		super(processor, 5000);
	}

	@Override
	public void shortClick() {
		getProcessor().setAlarmStateToDirty();
		getProcessor().getDisplayOffController().setLastActivity(System.currentTimeMillis());
		getProcessor().switchGuiTo(ClockPane.class.getName());

	}

	@Override
	public void longClick() {
		getProcessor().setAlarmStateToDirty();
		getProcessor().getDisplayOffController().setLastActivity(System.currentTimeMillis());
		getProcessor().exit();
	}

}
