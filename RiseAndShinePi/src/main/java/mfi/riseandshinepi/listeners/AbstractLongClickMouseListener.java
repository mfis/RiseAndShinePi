package mfi.riseandshinepi.listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.TimerTask;

import mfi.riseandshinepi.logic.Processor;

public abstract class AbstractLongClickMouseListener extends MouseAdapter {

	private java.util.Timer t;
	private boolean eventFired;
	private Processor processor;
	private int timespan;

	public AbstractLongClickMouseListener(Processor processor, int timespanInMillies) {
		this.processor = processor;
		timespan = timespanInMillies;
	}

	@Override
	public final void mousePressed(MouseEvent e) {

		eventFired = false;
		if (timespan > 0) {
			if (t == null) {
				t = new java.util.Timer();
			}
			t.schedule(new TimerTask() {
				@Override
				public void run() {
					eventFired = true;
					clickEvents();
					longClick();
				}
			}, timespan);
		}
	}

	@Override
	public final void mouseReleased(MouseEvent e) {
		if (t != null) {
			t.cancel();
			t.purge();
			t = null;
			if (!eventFired) {
				eventFired = true;
				clickEvents();
				shortClick();
			}
		} else {
			eventFired = true;
			clickEvents();
			shortClick();
		}
	}

	private void clickEvents() {
		getProcessor().setAlarmStateToDirty();
		getProcessor().getDisplayOffController().setLastActivity(System.currentTimeMillis());
	}

	public abstract void shortClick();

	public abstract void longClick();

	Processor getProcessor() {
		return processor;
	}

}
