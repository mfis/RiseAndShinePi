package mfi.riseandshinepi.listeners;

import mfi.riseandshinepi.gui.cardpanes.AlarmSettingsPane;
import mfi.riseandshinepi.gui.cardpanes.DisplayAutoOffSettingsPane;
import mfi.riseandshinepi.logic.Processor;

public class SettingsMouseListener extends AbstractLongClickMouseListener {

	public SettingsMouseListener(Processor processor) {
		super(processor, 2000);
	}

	@Override
	public void shortClick() {
		getProcessor().setAlarmStateToDirty();
		getProcessor().getDisplayOffController().setLastActivity(System.currentTimeMillis());
		getProcessor().switchGuiTo(AlarmSettingsPane.class.getName());
	}

	@Override
	public void longClick() {
		getProcessor().setAlarmStateToDirty();
		getProcessor().getDisplayOffController().setLastActivity(System.currentTimeMillis());
		getProcessor().switchGuiTo(DisplayAutoOffSettingsPane.class.getName());
	}

}
