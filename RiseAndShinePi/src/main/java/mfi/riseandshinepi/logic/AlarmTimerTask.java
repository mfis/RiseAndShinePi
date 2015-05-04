package mfi.riseandshinepi.logic;

import java.util.TimerTask;

public class AlarmTimerTask extends TimerTask {

	private Processor processor;

	public AlarmTimerTask(Processor processor) {
		this.processor = processor;
	}

	@Override
	public void run() {
		processor.processAlarmTimer();
	}

}
