package mfi.riseandshinepi.logic;

import java.time.LocalTime;
import mfi.riseandshinepi.webservices.YahooWeatherWebService;
import mfi.riseandshinepi.webservices.YahooWeatherWebService.Language;
import mfi.riseandshinepi.webservices.YahooWeatherWebService.TemperatureUnit;

public class WeatherController {

	private Processor processor;

	private Object monitor = new Object();

	private long lastCall = 0;

	private YahooWeatherWebService weatherWebService;

	public WeatherController(Processor processor) {

		this.processor = processor;

		String location = ApplicationProperties.WEATHER_SERVICE_LOCATION.toString();
		Language language = YahooWeatherWebService.Language.valueOf(ApplicationProperties.LANGUAGE.toString());
		TemperatureUnit temperatureUnit = YahooWeatherWebService.TemperatureUnit.valueOf(ApplicationProperties.WEATHER_SERVICE_UNIT.toString());

		weatherWebService = new YahooWeatherWebService(location, language, temperatureUnit);
	}

	public synchronized void refreshWeather() {

		if ((System.currentTimeMillis() - lastCall) < 1000 * 60 * 15) {
			return;
		}

		if (!processor.getGui().isActualPaneShowingWeatherInformation()) {
			if ((System.currentTimeMillis() - lastCall) < 1000 * 60 * 360) {
				return;
			}
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				synchronized (monitor) {
					if (processor.isDevelopmentMode()) {
						System.out.println("CALLING WEATHER");
					}
					weatherWebService.weatherCall();
					if (weatherWebService.isDataAvailable()) {
						lastCall = System.currentTimeMillis();
					}
					processor.getGui().refreshGuiValues();
					processor.checkBacklightOffset();
				}
			}
		}).start();
	}

	public String getActualTemperature() {
		return weatherWebService.getActualTemperature();
	}

	public String getActualCondition() {
		return weatherWebService.getActualCondition();
	}

	public String getTodayMinTemperature() {
		return weatherWebService.getTodayMinTemperature();
	}

	public String getTodayMaxTemperature() {
		return weatherWebService.getTodayMaxTemperature();
	}

	public String getTodayCondition() {
		return weatherWebService.getTodayCondition();
	}

	public String getWeatherLocation() {
		return weatherWebService.getWeatherLocation();
	}

	public String getProvider() {
		return weatherWebService.getProvider();
	}

	public boolean isDataAvailable() {
		return weatherWebService.isDataAvailable();
	}

	public boolean isDaylightTime() {
		if (weatherWebService.getSunrise() == null || weatherWebService.getSunset() == null) {
			return false;
		}
		LocalTime lc = LocalTime.now();
		return lc.isAfter(weatherWebService.getSunrise()) && lc.isBefore(weatherWebService.getSunset());
	}

}
