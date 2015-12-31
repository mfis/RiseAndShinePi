package mfi.riseandshinepi.listeners;

import mfi.riseandshinepi.gui.cardpanes.ClockPane;
import mfi.riseandshinepi.gui.cardpanes.SnoozePane;
import mfi.riseandshinepi.logic.Processor;

public class SnoozeListener extends AbstractLongClickMouseListener {

	public SnoozeListener(Processor processor) {
		super(processor, 2000);
	}

	@Override
	public void shortClick() {
		if (!getProcessor().isAlarmNowOn()) {
			getProcessor().switchGuiTo(ClockPane.class.getName());
		}
	}

	@Override
	public void longClick() {
		if (getProcessor().isAlarmNowOn()) {
			if (getProcessor().getAudioPlayer().isPlaying()) {
				getProcessor().getAudioPlayer().stop();
			}
			getProcessor().switchGuiTo(SnoozePane.class.getName());
		}
	}

}
