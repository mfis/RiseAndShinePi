package mfi.riseandshinepi.hardware;

import java.util.Timer;
import java.util.TimerTask;

public class CurrentDateTime extends TimerTask {

	private static CurrentDateTime instance;

	private static final Object monitor = new Object();

	private static Timer timer = new Timer();

	private static long millis = 0;

	static {
		instance = new CurrentDateTime();
	}

	private CurrentDateTime() {
		callMillis();
		timer.schedule(this, 600, 600);
	}

	private void callMillis() {
		millis = System.currentTimeMillis();
	}

	public static CurrentDateTime getInstance() {
		if (instance == null) {
			synchronized (monitor) {
				if (instance == null) {
					instance = new CurrentDateTime();
				}
			}
		}
		return instance;
	}

	public long getMillis() {
		return millis;
	}

	@Override
	public void run() {
		callMillis();
	}

}
