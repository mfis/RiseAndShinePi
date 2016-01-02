package mfi.riseandshinepi.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

public enum ApplicationProperties {

	LANGUAGE, //
	BULB_POWER_ON_DELAY_MILLIES, //
	BULB_POWER_GPIO_PIN_NUMBER, //
	SPEAKER_POWER_ON_DELAY_MILLIES, //
	SPEAKER_POWER_GPIO_PIN_NUMBER, //
	DISPLAY_BACKLIGHT_DIMMING_GPIO_PIN_NUMBER, //
	DISPLAY_BACKLIGHT_DEFAULT_VALUE, //
	MUSIC_DIR_RELATIVE_TO_USER_HOME, //
	DISPLAY_ON_X_HOURS_BEFORE_ALARM, //
	DISPLAY_OFF_X_MINUTES_IN_INACTIVITY, //
	WEATHER_SERVICE_LOCATION, //
	WEATHER_SERVICE_UNIT, //
	SPEAKER_VOLUME_PERCENT, //
	ALARM_1, //
	ALARM_2, //
	ALARM_3, //
	; //

	private String value = null;

	public static void init() {

		Properties applicationProperties = new Properties();
		Properties userProperties = new Properties();

		try {

			URL url = ApplicationProperties.class.getClassLoader().getResource("application.properties");
			URLConnection resConn = url.openConnection();
			resConn.setUseCaches(false);
			InputStream in = resConn.getInputStream();
			applicationProperties.load(in);

			loadUserProperties(userProperties);

		} catch (Exception e) {
			throw new IllegalStateException("application.properties/user.properties could not be loaded ", e);
		}

		for (ApplicationProperties e : values()) {
			String value = applicationProperties.getProperty(e.name());
			if (userProperties.containsKey(e.name())) {
				// overwriting application properties with user properties
				value = userProperties.getProperty(e.name());
			}
			e.value = value;
		}
	}

	private static void loadUserProperties(Properties userProperties) throws IOException, FileNotFoundException {

		File userPropertiesFile = new File("raspUserProperties.properties");
		if (!userPropertiesFile.exists()) {
			userPropertiesFile.createNewFile();
		}

		FileInputStream fis = new FileInputStream(userPropertiesFile);
		userProperties.load(fis);

	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {

		if (value == null) {
			throw new IllegalStateException("ApplicationProperties - no value for: " + this.name());
		}
		return value.trim();
	}

	public int valueAsInt() {

		String string = toString();
		return Integer.parseInt(string);
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static void store() {

		try {
			Properties userProperties = new Properties();
			loadUserProperties(userProperties);

			File userPropertiesFile = new File("raspUserProperties.properties");
			if (!userPropertiesFile.exists()) {
				userPropertiesFile.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(userPropertiesFile);

			for (ApplicationProperties e : values()) {
				userProperties.setProperty(e.name(), e.toString());
			}
			userProperties.store(fos, "");
			fos.flush();
			fos.close();

		} catch (Exception e) {
			throw new IllegalStateException("application.properties/user.properties could not be written ", e);
		}
	}

}
