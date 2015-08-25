package mfi.riseandshinepi.webservices;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.google.gson.GsonBuilder;

public class YahooWeatherWebService {

	public final static String DEGREE = "\u00b0";

	private final static String VAR_QUERY = "$$QUERY$$";
	private final static String VAR_LOCATION = "$$LOCATION$$";
	private final static String VAR_TEMPERATURE_UNIT = "$$TEMPERATUREUNIT$$";

	private final static SimpleDateFormat sdfActualWeather = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm a z",
			Locale.ENGLISH);
	private final static SimpleDateFormat sdfActualWeatherWithoutTimezone = new SimpleDateFormat(
			"EEE, dd MMM yyyy hh:mm a", Locale.ENGLISH);

	private final static SimpleDateFormat sdfForecastWeather = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
	
	private final static DecimalFormat decimalFormat = new DecimalFormat(" ");

	private final static String YAHOO_WEATHER_FORECAST_URL = //
	"https://query.yahooapis.com/v1/public/yql?q=" + VAR_QUERY
			+ "&format=json&diagnostics=false&env=store://datatables.org/alltableswithkeys&callback=";

	private final static String YAHOO_WEATHER_YQL_QUERY = //
	"select * from weather.forecast where u='" + VAR_TEMPERATURE_UNIT
			+ "' and woeid in (select woeid from geo.places(1) where text=\"" + VAR_LOCATION + "\")";

	private final static Map<Integer, String> weatherConditionsEN = new LinkedHashMap<Integer, String>();
	private final static Map<Integer, String> weatherConditionsDE = new LinkedHashMap<Integer, String>();
	private final static Map<Language, Map<Integer, String>> weatherConditions = new LinkedHashMap<YahooWeatherWebService.Language, Map<Integer, String>>();

	private String location;
	private Language language;
	private TemperatureUnit temperatureUnit;
	private Weather weather = null;
	private int forecastIndex = 0;
	private boolean dataAvailable = false;

	public enum Language {
		EN, DE;
	}

	public enum TemperatureUnit {
		CELSIUS('c'), FAHRENHEIT('f');
		private char key;

		private TemperatureUnit(char key) {
			this.key = key;
		}

		public char getKey() {
			return key;
		}
	}

	public YahooWeatherWebService(String location, Language language, TemperatureUnit temperatureUnit) {
		this.location = location;
		this.language = language;
		this.temperatureUnit = temperatureUnit;
	}

	// public static void main(String[] args) throws Exception {
	//
	// YahooWeatherWebService instance = new YahooWeatherWebService("Hattingen,
	// Germany", Language.DE,
	// TemperatureUnit.CELSIUS);
	// instance.weatherCall();
	//
	// System.out.println(instance.getActualCondition() + ", " +
	// instance.getActualTemperature());
	// }

	public void weatherCall() {

		try {
			String url = buildQueryURL(location);
			String json = request(url);
			weather = readJson(json, Weather.class);

			lookupForecastIndex();

		} catch (Exception e) {
			System.out.println("weather call: " + e.toString());
			dataAvailable = false;
			return;
		}

		dataAvailable = true;
	}

	private void lookupForecastIndex() throws ParseException {

		forecastIndex = -1;

		Date actualWeatherDate;
		try {
			actualWeatherDate = sdfActualWeather.parse(weather.query.results.channel.item.condition.date);
		} catch (ParseException pe) {
			// for some locations (e.g. Antarctica), Yahoo returns strange
			// timezone strings
			actualWeatherDate = sdfActualWeatherWithoutTimezone
					.parse(StringUtils.substringBeforeLast(weather.query.results.channel.item.condition.date, " "));
		}

		for (int i = 0; i < weather.query.results.channel.item.forecast.length; i++) {
			Date forecastWeather = sdfForecastWeather.parse(weather.query.results.channel.item.forecast[i].date);
			if (DateUtils.isSameDay(actualWeatherDate, forecastWeather)) {
				forecastIndex = i;
				break;
			}
		}

		if (forecastIndex == -1) {
			throw new IllegalArgumentException("No forecast index found");
		}
	}

	private String buildQueryURL(String location) {

		String yqlQuery = YAHOO_WEATHER_YQL_QUERY.replace(VAR_LOCATION, location);
		yqlQuery = yqlQuery.replace(VAR_TEMPERATURE_UNIT, String.valueOf(temperatureUnit.getKey()));
		String yqlQueryEncoded;
		try {
			yqlQueryEncoded = URLEncoder.encode(yqlQuery, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
		String url = YAHOO_WEATHER_FORECAST_URL.replace(VAR_QUERY, yqlQueryEncoded);
		return url;
	}

	public static <T> T readJson(String json, Class<T> clazz) {
		return new GsonBuilder().create().fromJson(json, clazz);
	}

	private String request(String urlString) {

		StringBuilder sb = new StringBuilder();
		try {
			URL url = new URL(urlString);
			HttpURLConnection.setFollowRedirects(true);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("user-agent", "RASP Weather Info");
			con.setUseCaches(false);
			con.setDoOutput(true);
			if (con.getResponseCode() != 200) {
				throw new IllegalStateException("HTTP-RC = " + con.getResponseCode() + " - " + urlString);
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			String decodedString;
			while ((decodedString = in.readLine()) != null) {
				sb.append(decodedString);
			}
			in.close();
		} catch (Exception e) {
			throw new IllegalStateException("Error requesting URL " + urlString, e);
		}
		return sb.toString();
	}

	public String getActualTemperature() {
		if (!isDataAvailable()) {
			return "";
		}
		return decimalFormat.format(weather.query.results.channel.item.condition.temp).trim() + DEGREE
				+ weather.query.results.channel.units.temperature;
	}

	public String getActualCondition() {
		if (!isDataAvailable()) {
			return "";
		}
		return weatherConditions.get(language).get(weather.query.results.channel.item.condition.code);
	}

	public String getTodayMinTemperature() {
		if (!isDataAvailable()) {
			return "";
		}
		return decimalFormat.format(weather.query.results.channel.item.forecast[forecastIndex].low).trim() + DEGREE
				+ weather.query.results.channel.units.temperature;
	}

	public String getTodayMaxTemperature() {
		if (!isDataAvailable()) {
			return "";
		}
		return decimalFormat.format(weather.query.results.channel.item.forecast[forecastIndex].high).trim() + DEGREE
				+ weather.query.results.channel.units.temperature;
	}

	public String getTodayCondition() {
		if (!isDataAvailable()) {
			return "";
		}
		return weatherConditions.get(language).get(weather.query.results.channel.item.forecast[forecastIndex].code);
	}

	public String getWeatherLocation() {
		if (!isDataAvailable()) {
			return "";
		}
		return weather.query.results.channel.location.city + ", " + weather.query.results.channel.location.country;
	}
	
	public String getProvider(){
		if (!isDataAvailable()) {
			return "";
		}
		return "Yahoo! Weather";
	}
	
	public boolean isDataAvailable() {
		return dataAvailable;
	}

	static {
		// english
		weatherConditionsEN.put(0, "tornado");
		weatherConditionsEN.put(1, "tropical storm");
		weatherConditionsEN.put(2, "hurricane");
		weatherConditionsEN.put(3, "severe thunderstorms");
		weatherConditionsEN.put(4, "thunderstorms");
		weatherConditionsEN.put(5, "mixed rain and snow");
		weatherConditionsEN.put(6, "mixed rain and sleet");
		weatherConditionsEN.put(7, "mixed snow and sleet");
		weatherConditionsEN.put(8, "freezing drizzle");
		weatherConditionsEN.put(9, "drizzle");
		weatherConditionsEN.put(10, "freezing rain");
		weatherConditionsEN.put(11, "showers");
		weatherConditionsEN.put(12, "showers");
		weatherConditionsEN.put(13, "snow flurries");
		weatherConditionsEN.put(14, "light snow showers");
		weatherConditionsEN.put(15, "blowing snow");
		weatherConditionsEN.put(16, "snow");
		weatherConditionsEN.put(17, "hail");
		weatherConditionsEN.put(18, "sleet");
		weatherConditionsEN.put(19, "dust");
		weatherConditionsEN.put(20, "foggy");
		weatherConditionsEN.put(21, "haze");
		weatherConditionsEN.put(22, "smoky");
		weatherConditionsEN.put(23, "blustery");
		weatherConditionsEN.put(24, "windy");
		weatherConditionsEN.put(25, "cold");
		weatherConditionsEN.put(26, "cloudy");
		weatherConditionsEN.put(27, "mostly cloudy");
		weatherConditionsEN.put(28, "mostly cloudy");
		weatherConditionsEN.put(29, "partly cloudy");
		weatherConditionsEN.put(30, "partly cloudy");
		weatherConditionsEN.put(31, "clear");
		weatherConditionsEN.put(32, "sunny");
		weatherConditionsEN.put(33, "fair");
		weatherConditionsEN.put(34, "fair");
		weatherConditionsEN.put(35, "mixed rain and hail");
		weatherConditionsEN.put(36, "hot");
		weatherConditionsEN.put(37, "isolated thunderstorms");
		weatherConditionsEN.put(38, "scattered thunderstorms");
		weatherConditionsEN.put(39, "scattered thunderstorms");
		weatherConditionsEN.put(40, "scattered showers");
		weatherConditionsEN.put(41, "heavy snow");
		weatherConditionsEN.put(42, "scattered snow showers");
		weatherConditionsEN.put(43, "heavy snow");
		weatherConditionsEN.put(44, "partly cloudy");
		weatherConditionsEN.put(45, "thundershowers");
		weatherConditionsEN.put(46, "snow showers");
		weatherConditionsEN.put(47, "isolated thundershowers");
		weatherConditionsEN.put(3200, "");
		// german
		weatherConditionsDE.put(0, "Tornado");
		weatherConditionsDE.put(1, "Tropischer Sturm");
		weatherConditionsDE.put(2, "Orkan");
		weatherConditionsDE.put(3, "Starkes Unwetter");
		weatherConditionsDE.put(4, "Unwetter");
		weatherConditionsDE.put(5, "Schneeregen");
		weatherConditionsDE.put(6, "Graupel");
		weatherConditionsDE.put(7, "Schnee und Graupel");
		weatherConditionsDE.put(8, "Gefrierender Niesel");
		weatherConditionsDE.put(9, "Nieselregen");
		weatherConditionsDE.put(10, "Gefrierender Regen");
		weatherConditionsDE.put(11, "Schauer");
		weatherConditionsDE.put(12, "Schauer");
		weatherConditionsDE.put(13, "Schnee");
		weatherConditionsDE.put(14, "Leichter Schnee");
		weatherConditionsDE.put(15, "Schneeböen");
		weatherConditionsDE.put(16, "Schnee");
		weatherConditionsDE.put(17, "Hagel");
		weatherConditionsDE.put(18, "Schneeregen");
		weatherConditionsDE.put(19, "Staubböen");
		weatherConditionsDE.put(20, "Nebel");
		weatherConditionsDE.put(21, "Dunst");
		weatherConditionsDE.put(22, "Nebel");
		weatherConditionsDE.put(23, "Stürmisch");
		weatherConditionsDE.put(24, "Windig");
		weatherConditionsDE.put(25, "Frostig");
		weatherConditionsDE.put(26, "Wolkig");
		weatherConditionsDE.put(27, "Meist wolkig");
		weatherConditionsDE.put(28, "Meist wolkig");
		weatherConditionsDE.put(29, "Teils wolkig");
		weatherConditionsDE.put(30, "Teils wolkig");
		weatherConditionsDE.put(31, "Klar");
		weatherConditionsDE.put(32, "Sonne");
		weatherConditionsDE.put(33, "Unbewölkt");
		weatherConditionsDE.put(34, "Unbewölkt");
		weatherConditionsDE.put(35, "Regen und Hagel");
		weatherConditionsDE.put(36, "Heiß");
		weatherConditionsDE.put(37, "Tropischer Sturn");
		weatherConditionsDE.put(38, "Vereinzelt Sturm");
		weatherConditionsDE.put(39, "Vereinzelt Sturm");
		weatherConditionsDE.put(40, "Vereinzelt Schauer");
		weatherConditionsDE.put(41, "Starker Schnee");
		weatherConditionsDE.put(42, "Vereinzelt Schnee");
		weatherConditionsDE.put(43, "Starker Schnee");
		weatherConditionsDE.put(44, "Teils wolkig");
		weatherConditionsDE.put(45, "Starke Schauer");
		weatherConditionsDE.put(46, "Schneeschauer");
		weatherConditionsDE.put(47, "Vereinzelt Sturm");
		weatherConditionsDE.put(3200, "");

		weatherConditions.put(Language.EN, weatherConditionsEN);
		weatherConditions.put(Language.DE, weatherConditionsDE);
	}

	@SuppressWarnings("unused")
	private class Weather {

		private Query query;

		private class Query {

			private Results results;

			private class Results {

				private Channel channel;

				private class Channel {

					private String title; // 'Yahoo! Weather - LOCATION'

					private Location location;

					private class Location {

						private String city; // 'Hattingen'
						
						private String country; // 'Germany'
					}
					
					private Units units;

					private class Units {

						private String temperature; // 'C'elsius or 'F'ahrenheit
					}

					private Item item;

					private class Item {

						private Condition condition;

						private class Condition {

							private int code; // Weather condition code, see Map
							private int temp; // actual temperature
							private String date; // Sun, 16 Aug 2015 11:00 am
													// CEST
						}

						private Forecast forecast[];

						private class Forecast {

							private int code; // Weather condition code, see Map
							private String date; // 16 Aug 2015
							private int low; // min temperature
							private int high; // max temperature
						}
					}

				};
			};
		};
	}

}
