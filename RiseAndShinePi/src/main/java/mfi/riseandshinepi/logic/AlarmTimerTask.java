package mfi.riseandshinepi.logic;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.TimerTask;
import mfi.riseandshinepi.gui.cardpanes.BlankPane;

public class AlarmTimerTask extends TimerTask {

	private Processor processor;
	private Robot robot;

	public AlarmTimerTask(Processor processor) {
		this.processor = processor;
		try {
			robot = new Robot();
		} catch (AWTException e) {
		}
	}

	@Override
	public void run() {

		if (!processor.isDevelopmentMode()) {
			if (processor.getDisplayBacklight().getActualValue() > 0 && !processor.getGui().getActualPaneName().equals(BlankPane.class.getName())) {
				robot.setAutoDelay(10);
				robot.keyPress(KeyEvent.VK_R);
				robot.keyRelease(KeyEvent.VK_R);
			}
		}

		processor.getAlarmController().processAlarmTimer();
		processor.processDisplayAutoOff();
	}

}
