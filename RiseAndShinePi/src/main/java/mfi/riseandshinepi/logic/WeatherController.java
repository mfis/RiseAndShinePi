package mfi.riseandshinepi.logic;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import mfi.riseandshinepi.hardware.CurrentDateTime;
import mfi.riseandshinepi.webservices.YahooWeatherWebService;
import mfi.riseandshinepi.webservices.YahooWeatherWebService.Language;
import mfi.riseandshinepi.webservices.YahooWeatherWebService.TemperatureUnit;

public class WeatherController {

	private Processor processor;

	private Object monitor = new Object();

	private long lastDataAvailable = 0;
	private long lastRequest = 0;

	private YahooWeatherWebService weatherWebService;

	private Calendar calendar = new GregorianCalendar();

	private LocalTime localTime;

	public WeatherController(Processor processor) {

		this.processor = processor;

		String location = ApplicationProperties.WEATHER_SERVICE_LOCATION.toString();
		Language language = YahooWeatherWebService.Language.valueOf(ApplicationProperties.LANGUAGE.toString());
		TemperatureUnit temperatureUnit = YahooWeatherWebService.TemperatureUnit.valueOf(ApplicationProperties.WEATHER_SERVICE_UNIT.toString());

		weatherWebService = new YahooWeatherWebService(location, language, temperatureUnit);
	}

	public synchronized void refreshWeather(boolean forceRefresh) {

		if ((CurrentDateTime.getInstance().getMillis() - lastRequest) < 1000 * 31) {
			return;
		}

		long refreshRate = (processor.getGui().isActualPaneShowingWeatherInformation() || forceRefresh) ? (1000 * 60 * 10) : (1000 * 60 * 120);

		if ((CurrentDateTime.getInstance().getMillis() - lastDataAvailable) < refreshRate) {
			return;
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				synchronized (monitor) {
					if (processor.isDevelopmentMode()) {
						System.out.println("CALLING WEATHER");
					}
					lastRequest = CurrentDateTime.getInstance().getMillis();
					weatherWebService.weatherCall();
					if (weatherWebService.isDataAvailable()) {
						lastDataAvailable = CurrentDateTime.getInstance().getMillis();
					}
					processor.getGui().refreshGuiValues();
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
		calendar.setTimeInMillis(CurrentDateTime.getInstance().getMillis());
		localTime = LocalTime.of(calendar.get(java.util.Calendar.HOUR_OF_DAY), calendar.get(java.util.Calendar.MINUTE));
		return localTime.isAfter(weatherWebService.getSunrise()) && localTime.isBefore(weatherWebService.getSunset());
	}

}
