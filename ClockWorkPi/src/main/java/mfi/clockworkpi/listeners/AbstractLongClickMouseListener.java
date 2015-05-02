package mfi.clockworkpi.listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.TimerTask;

import mfi.clockworkpi.logic.Processor;

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
		if (t == null) {
			t = new java.util.Timer();
		}
		t.schedule(new TimerTask() {
			@Override
			public void run() {
				eventFired = true;
				longClick();
			}
		}, timespan);
	}

	@Override
	public final void mouseReleased(MouseEvent e) {
		if (t != null) {
			t.cancel();
			t.purge();
			t = null;
			if (!eventFired) {
				eventFired = true;
				shortClick();
			}
		}
	}

	public abstract void shortClick();

	public abstract void longClick();

	Processor getProcessor() {
		return processor;
	}

}
