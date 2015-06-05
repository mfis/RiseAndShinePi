package mfi.riseandshinepi.logic;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

public enum ApplicationProperties {

	BULB_POWER_ON_DELAY_MILLIES, //
	BULB_POWER_GPIO_PIN_NUMBER, //
	SPEAKER_POWER_ON_DELAY_MILLIES, //
	SPEAKER_POWER_GPIO_PIN_NUMBER, //
	DISPLAY_BACKLIGHT_DIMMING_GPIO_PIN_NUMBER, //
	DISPLAY_BACKLIGHT_DEFAULT_VALUE, //
	MUSIC_DIR_1_RELATIVE_TO_USER_HOME, //
	MUSIC_DIR_2_RELATIVE_TO_USER_HOME, //
	DISPLAY_ON_X_HOURS_BEFORE_ALARM, //
	DISPLAY_OFF_X_MINUTES_IN_INACTIVITY, //
	; //

	private String value = null;

	public static void init() {

		Properties properties = new Properties();

		try {

			URL url = ApplicationProperties.class.getClassLoader().getResource("application.properties");
			URLConnection resConn = url.openConnection();
			resConn.setUseCaches(false);
			InputStream in = resConn.getInputStream();

			properties.load(in);

		} catch (Exception e) {
			throw new IllegalStateException("application.properties could not be loaded ", e);
		}

		for (ApplicationProperties e : values()) {
			e.value = properties.getProperty(e.name());
		}
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

}
