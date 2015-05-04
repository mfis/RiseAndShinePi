package mfi.riseandshinepi.listeners;

import mfi.riseandshinepi.gui.cardpanes.BlankPane;
import mfi.riseandshinepi.logic.Processor;

public class AnalogClockMouseListener extends AbstractLongClickMouseListener {

	public AnalogClockMouseListener(Processor processor) {
		super(processor, 2000);
	}

	@Override
	public void shortClick() {
		if (getProcessor().isAlarmNowOn()) {
			getProcessor().alarmOff();
		} else {
			getProcessor().toggleBulb();
		}

	}

	@Override
	public void longClick() {
		getProcessor().switchGuiTo(BlankPane.class.getName());
	}

}
