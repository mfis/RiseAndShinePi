package mfi.riseandshinepi.logic;

import mfi.riseandshinepi.webservices.YahooWeatherWebService;
import mfi.riseandshinepi.webservices.YahooWeatherWebService.Language;
import mfi.riseandshinepi.webservices.YahooWeatherWebService.TemperatureUnit;

public class WeatherController {

	private Processor processor;
	
	public WeatherController(Processor processor) {

		this.processor = processor;
		
		String location = ApplicationProperties.WEATHER_SERVICE_LOCATION.toString();
		Language language = YahooWeatherWebService.Language.valueOf(ApplicationProperties.LANGUAGE.toString());
		TemperatureUnit temperatureUnit = YahooWeatherWebService.TemperatureUnit
				.valueOf(ApplicationProperties.WEATHER_SERVICE_UNIT.toString());

		weatherWebService = new YahooWeatherWebService(location, language, temperatureUnit);
	}

	private long lastCall = 0;

	private YahooWeatherWebService weatherWebService;

	public void refreshWeather() {

		if (System.currentTimeMillis() - lastCall > 1000 * 60) {

			new Thread(new Runnable() {
				public void run() {
					weatherWebService.weatherCall();
					lastCall = System.currentTimeMillis();
					processor.getGui().refreshGuiValues();
				}
			}).start();
		}
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

	public String getProvider(){
		return weatherWebService.getProvider();
	}
	
	public boolean isDataAvailable() {
		return weatherWebService.isDataAvailable();
	}

}
