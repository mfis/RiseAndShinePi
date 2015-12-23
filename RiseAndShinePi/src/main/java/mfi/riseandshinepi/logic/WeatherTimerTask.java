package mfi.riseandshinepi.logic;

import java.util.TimerTask;

public class WeatherTimerTask extends TimerTask {

	private Processor processor;

	public WeatherTimerTask(Processor processor) {
		this.processor = processor;
	}

	@Override
	public void run() {
		processor.getWeatherController().refreshWeather(false);
		processor.checkBacklightOffset();
	}

}
