package mfi.clockworkpi.gui.components;

/**
 *
 * @author hansolo Gerrit Grunwald Twitter @hansolo_
 */
public class AnalogClock extends javax.swing.JComponent implements
		java.awt.event.ActionListener {
	private static final long serialVersionUID = 1L;

	private final double ANGLE_STEP = 6;
	private final javax.swing.Timer CLOCK_TIMER = new javax.swing.Timer(100,
			this);
	private double minutePointerAngle = java.util.Calendar.getInstance().get(
			java.util.Calendar.MINUTE)
			* ANGLE_STEP;
	private double hourPointerAngle = java.util.Calendar.getInstance().get(
			java.util.Calendar.HOUR)
			* ANGLE_STEP
			* 5
			+ 0.5
			* java.util.Calendar.getInstance().get(java.util.Calendar.MINUTE);
	private double secondPointerAngle = java.util.Calendar.getInstance().get(
			java.util.Calendar.SECOND)
			* ANGLE_STEP;
	private java.awt.geom.Rectangle2D hourPointer;
	private java.awt.geom.Rectangle2D minutePointer;
	private java.awt.geom.GeneralPath secondPointer;
	private java.awt.geom.Ellipse2D centerKnob;
	private java.awt.geom.Rectangle2D smallTick;
	private java.awt.geom.Rectangle2D bigTick;
	private final java.awt.Color SECOND_COLOR = new java.awt.Color(0xF00000);
	private final java.awt.Color SHADOW_COLOR = new java.awt.Color(0.0f, 0.0f,
			0.0f, 0.65f);

	public enum TYPE {
		LIGHT, DARK
	};

	private TYPE type = TYPE.DARK;
	private static final String TIMEOFDAY_PROPERTY = "timeOfDay";
	private int timeOfDay = 0;
	private java.awt.Color currentForegroundColor;
	private java.awt.Color[] currentBackgroundColor;
	private java.awt.geom.Point2D center;
	private int timeZoneOffsetHour = 0;
	private int timeZoneOffsetMinute = 0;
	private static final String DAYOFFSET_PROPERTY = "dayOffset";
	private int dayOffset = 0;
	private int hour;
	private int minute;
	boolean am = (java.util.Calendar.getInstance()
			.get(java.util.Calendar.AM_PM) == java.util.Calendar.AM);
	// Flags
	private boolean secondPointerVisible = true;
	private boolean autoType = true;

	public AnalogClock() {
		super();
		setSize(100, 100);

		init();

		CLOCK_TIMER.start();
	}

	private void init() {
		// Rotation center
		this.center = new java.awt.geom.Point2D.Float(getWidth() / 2.0f,
				getHeight() / 2.0f);

		// Hour pointer
		final double HOUR_POINTER_WIDTH = getWidth() * 0.0545454545;
		final double HOUR_POINTER_HEIGHT = getWidth() * 0.3090909091;
		this.hourPointer = new java.awt.geom.Rectangle2D.Double(center.getX()
				- (HOUR_POINTER_WIDTH / 2), (getWidth() * 0.1909090909),
				HOUR_POINTER_WIDTH, HOUR_POINTER_HEIGHT);

		// Minute pointer
		final double MINUTE_POINTER_WIDTH = getWidth() * 0.0454545455;
		final double MINUTE_POINTER_HEIGHT = getWidth() * 0.4363636364;
		this.minutePointer = new java.awt.geom.Rectangle2D.Double(center.getX()
				- (MINUTE_POINTER_WIDTH / 2), (getWidth() * 0.0636363636),
				MINUTE_POINTER_WIDTH, MINUTE_POINTER_HEIGHT);

		// Second pointer
		final java.awt.geom.GeneralPath SECOND_AREA = new java.awt.geom.GeneralPath();
		SECOND_AREA.moveTo(getWidth() * 0.4863636364, center.getY());
		SECOND_AREA.lineTo(getWidth() * 0.5136363636, center.getY());
		SECOND_AREA
				.lineTo(getWidth() * 0.5045454545, getWidth() * 0.0363636364);
		SECOND_AREA
				.lineTo(getWidth() * 0.4954545455, getWidth() * 0.0363636364);
		SECOND_AREA.closePath();
		java.awt.geom.Area second = new java.awt.geom.Area(SECOND_AREA);
		second.add(new java.awt.geom.Area(new java.awt.geom.Ellipse2D.Double(
				getWidth() * 0.4545454545, getWidth() * 0.1454545455,
				getWidth() * 0.0909090909, getWidth() * 0.0909090909)));
		second.subtract(new java.awt.geom.Area(
				new java.awt.geom.Ellipse2D.Double(getWidth() * 0.4636363636,
						getWidth() * 0.1545454545, getWidth() * 0.0727272727,
						getWidth() * 0.0727272727)));
		this.secondPointer = new java.awt.geom.GeneralPath(second);

		// Center knob
		final double CENTER_KNOB_DIAMETER = getWidth() * 0.090909;
		this.centerKnob = new java.awt.geom.Ellipse2D.Double(center.getX()
				- CENTER_KNOB_DIAMETER / 2, center.getY()
				- CENTER_KNOB_DIAMETER / 2, CENTER_KNOB_DIAMETER,
				CENTER_KNOB_DIAMETER);

		// Minute tick mark
		final double SMALL_TICK_WIDTH = getWidth() * 0.0181818;
		final double SMALL_TICK_HEIGHT = getWidth() * 0.0363636;
		this.smallTick = new java.awt.geom.Rectangle2D.Double(center.getX()
				- (SMALL_TICK_WIDTH / 2), SMALL_TICK_HEIGHT, SMALL_TICK_WIDTH,
				SMALL_TICK_HEIGHT);

		// Hour tick mark
		final double BIG_TICK_WIDTH = getWidth() * 0.0363636;
		final double BIG_TICK_HEIGHT = getWidth() * 0.10909090;
		this.bigTick = new java.awt.geom.Rectangle2D.Double(center.getX()
				- (BIG_TICK_WIDTH / 2), SMALL_TICK_HEIGHT, BIG_TICK_WIDTH,
				BIG_TICK_HEIGHT);

		switch (type) {
		case LIGHT:
			this.currentForegroundColor = java.awt.Color.BLACK;
			this.currentBackgroundColor = new java.awt.Color[] {
					new java.awt.Color(0xF7F7F7), new java.awt.Color(0xF0F0F0) };
			break;

		case DARK:
			this.currentForegroundColor = java.awt.Color.WHITE;
			this.currentBackgroundColor = new java.awt.Color[] {
					new java.awt.Color(0x3E3B32), new java.awt.Color(0x232520) };
			break;

		default:
			this.currentForegroundColor = java.awt.Color.WHITE;
			this.currentBackgroundColor = new java.awt.Color[] {
					new java.awt.Color(0x3E3B32), new java.awt.Color(0x232520) };
			break;
		}
		repaint(0, 0, getWidth(), getHeight());
	}

	@Override
	protected void paintComponent(java.awt.Graphics g) {
		java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;

		g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
				java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION,
				java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING,
				java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL,
				java.awt.RenderingHints.VALUE_STROKE_PURE);

		java.awt.geom.AffineTransform oldTransform = g2.getTransform();

		// Draw background image
		createBackgroundImage(g2, oldTransform);

		// Draw hour pointer
		g2.setColor(currentForegroundColor);
		g2.rotate(Math.toRadians(hourPointerAngle), center.getX(),
				center.getY());
		g2.fill(hourPointer);

		// Draw minute pointer
		if (getType().equals(TYPE.DARK)) {
			g2.setTransform(oldTransform);
			g2.setColor(SHADOW_COLOR);
			g2.rotate(Math.toRadians(minutePointerAngle
					+ (1 * Math.sin(Math.toRadians(minutePointerAngle)))),
					center.getX(), center.getY());
			g2.fill(minutePointer);
		}
		g2.setTransform(oldTransform);

		g2.setColor(currentForegroundColor);
		g2.rotate(Math.toRadians(minutePointerAngle), center.getX(),
				center.getY());
		g2.fill(minutePointer);

		if (secondPointerVisible) {
			// Draw second pointer
			g2.setTransform(oldTransform);
			g2.setColor(SHADOW_COLOR);
			g2.rotate(Math.toRadians(secondPointerAngle
					+ (2 * Math.sin(Math.toRadians(secondPointerAngle)))),
					center.getX(), center.getY());
			g2.fill(secondPointer);

			g2.setTransform(oldTransform);
			g2.setColor(SECOND_COLOR);
			g2.rotate(Math.toRadians(secondPointerAngle), center.getX(),
					center.getY());
			g2.fill(secondPointer);
		}
		g2.setTransform(oldTransform);

		g2.setColor(currentForegroundColor);
		g2.fill(centerKnob);

	}

	public TYPE getType() {
		return this.type;
	}

	public void setType(TYPE type) {
		this.type = type;
		init();
	}

	public boolean isSecondPointerVisible() {
		return this.secondPointerVisible;
	}

	public void setSecondPointerVisible(boolean secondPointerVisible) {
		this.secondPointerVisible = secondPointerVisible;

		/*
		 * Adjust the timer delay due to the visibility of the second pointer.
		 */
		if (secondPointerVisible) {
			CLOCK_TIMER.stop();
			CLOCK_TIMER.setDelay(100);
			CLOCK_TIMER.start();
		} else {
			CLOCK_TIMER.stop();
			CLOCK_TIMER.setDelay(1000);
			CLOCK_TIMER.start();
		}
		init();
	}

	public boolean isAutoType() {
		return this.autoType;
	}

	public void setAutoType(boolean autoType) {
		this.autoType = autoType;
	}

	public int getTimeZoneOffsetHour() {
		return this.timeZoneOffsetHour;
	}

	public void setTimeZoneOffsetHour(int timeZoneOffsetHour) {
		this.timeZoneOffsetHour = timeZoneOffsetHour;
	}

	public int getTimeZoneOffsetMinute() {
		return this.timeZoneOffsetMinute;
	}

	public void setTimeZoneOffsetMinute(int timeZoneOffsetMinute) {
		this.timeZoneOffsetMinute = timeZoneOffsetMinute;
	}

	private void createBackgroundImage(java.awt.Graphics2D g2,
			java.awt.geom.AffineTransform oldTransform) {

		// Draw clock background
		final java.awt.geom.Point2D LIGHT_START = new java.awt.geom.Point2D.Float(
				0, 0);
		final java.awt.geom.Point2D LIGHT_STOP = new java.awt.geom.Point2D.Float(
				0, getHeight());
		final float[] LIGHT_FRACTIONS = { 0.0f, 1.0f };
		final java.awt.Color[] LIGHT_COLORS = { new java.awt.Color(0x000000),
				new java.awt.Color(0x645E54) };
		final java.awt.LinearGradientPaint LIGHT_GRADIENT = new java.awt.LinearGradientPaint(
				LIGHT_START, LIGHT_STOP, LIGHT_FRACTIONS, LIGHT_COLORS);
		g2.setPaint(LIGHT_GRADIENT);
		g2.fill(new java.awt.geom.Ellipse2D.Float(0, 0, getWidth(), getHeight()));

		final java.awt.geom.Point2D BACKGROUND_START = new java.awt.geom.Point2D.Float(
				0, 1);
		final java.awt.geom.Point2D BACKGROUND_STOP = new java.awt.geom.Point2D.Float(
				0, getHeight() - 2);
		final float[] BACKGROUND_FRACTIONS = { 0.0f, 1.0f };
		final java.awt.Color[] BACKGROUND_COLORS = currentBackgroundColor;
		final java.awt.LinearGradientPaint BACKGROUND_GRADIENT = new java.awt.LinearGradientPaint(
				BACKGROUND_START, BACKGROUND_STOP, BACKGROUND_FRACTIONS,
				BACKGROUND_COLORS);
		g2.setPaint(BACKGROUND_GRADIENT);
		g2.fill(new java.awt.geom.Ellipse2D.Float(1, 1, getWidth() - 2,
				getHeight() - 2));

		// Draw minutes tickmarks
		g2.setColor(currentForegroundColor);
		for (int tickAngle = 0; tickAngle < 360; tickAngle += 6) {
			g2.setTransform(oldTransform);
			g2.rotate(Math.toRadians(tickAngle), center.getX(), center.getY());
			g2.fill(smallTick);
		}

		// Draw hours tickmarks
		for (int tickAngle = 0; tickAngle < 360; tickAngle += 30) {
			g2.setTransform(oldTransform);
			g2.rotate(Math.toRadians(tickAngle), center.getX(), center.getY());
			g2.fill(bigTick);
		}

		g2.setTransform(oldTransform);

	}

	@Override
	public void setPreferredSize(java.awt.Dimension dimension) {
		if (dimension.width >= dimension.height) {
			super.setSize(new java.awt.Dimension(dimension.width,
					dimension.width));
		} else {
			super.setSize(new java.awt.Dimension(dimension.height,
					dimension.height));
		}
		init();
	}

	@Override
	public void setSize(java.awt.Dimension dimension) {
		if (dimension.width >= dimension.height) {
			super.setSize(new java.awt.Dimension(dimension.width,
					dimension.width));
		} else {
			super.setSize(new java.awt.Dimension(dimension.height,
					dimension.height));
		}
		init();
	}

	@Override
	public void setSize(int width, int height) {
		if (width >= height) {
			super.setPreferredSize(new java.awt.Dimension(width, width));
			super.setSize(width, width);
		} else {
			super.setPreferredSize(new java.awt.Dimension(height, height));
			super.setSize(height, height);
		}
		init();
	}

	@Override
	public void actionPerformed(java.awt.event.ActionEvent event) {
		if (event.getSource().equals(CLOCK_TIMER)) {
			// Seconds
			secondPointerAngle = java.util.Calendar.getInstance().get(
					java.util.Calendar.SECOND)
					* ANGLE_STEP
					+ java.util.Calendar.getInstance().get(
							java.util.Calendar.MILLISECOND) * ANGLE_STEP / 1000;

			// Hours
			hour = java.util.Calendar.getInstance()
					.get(java.util.Calendar.HOUR) - this.timeZoneOffsetHour;
			if (hour > 12) {
				hour -= 12;
			}
			if (hour < 0) {
				hour += 12;
			}

			// Minutes
			minute = java.util.Calendar.getInstance().get(
					java.util.Calendar.MINUTE)
					+ this.timeZoneOffsetMinute;
			if (minute > 60) {
				minute -= 60;
				hour++;
			}
			if (minute < 0) {
				minute += 60;
				hour--;
			}

			// Calculate angles from current hour and minute values
			hourPointerAngle = hour * ANGLE_STEP * 5 + (0.5) * minute;
			minutePointerAngle = minute * ANGLE_STEP;

			// AutoType
			if (autoType) {
				if (java.util.Calendar.getInstance().get(
						java.util.Calendar.HOUR_OF_DAY)
						- timeZoneOffsetHour < 4) {
					// Night
					int oldTimeOfDay = this.timeOfDay;
					setType(TYPE.DARK);
					this.timeOfDay = -2;
					firePropertyChange(TIMEOFDAY_PROPERTY, oldTimeOfDay,
							timeOfDay);
				} else if (java.util.Calendar.getInstance().get(
						java.util.Calendar.HOUR_OF_DAY)
						- timeZoneOffsetHour < 6) {
					// Sunrise
					int oldTimeOfDay = this.timeOfDay;
					setType(TYPE.DARK);
					this.timeOfDay = -1;
					firePropertyChange(TIMEOFDAY_PROPERTY, oldTimeOfDay,
							timeOfDay);
				} else if (java.util.Calendar.getInstance().get(
						java.util.Calendar.HOUR_OF_DAY)
						- this.timeZoneOffsetHour >= 20) {
					// Night
					int oldTimeOfDay = this.timeOfDay;
					setType(TYPE.DARK);
					this.timeOfDay = -2;
					firePropertyChange(TIMEOFDAY_PROPERTY, oldTimeOfDay,
							timeOfDay);
				} else if (java.util.Calendar.getInstance().get(
						java.util.Calendar.HOUR_OF_DAY)
						- this.timeZoneOffsetHour >= 18) {
					// Sunset
					int oldTimeOfDay = this.timeOfDay;
					setType(TYPE.DARK);
					this.timeOfDay = 1;
					firePropertyChange(TIMEOFDAY_PROPERTY, oldTimeOfDay,
							timeOfDay);
				} else {
					// Day
					int oldTimeOfDay = this.timeOfDay;
					setType(TYPE.LIGHT);
					this.timeOfDay = 0;
					firePropertyChange(TIMEOFDAY_PROPERTY, oldTimeOfDay,
							timeOfDay);
				}

				if (java.util.Calendar.getInstance().get(
						java.util.Calendar.HOUR_OF_DAY)
						- this.timeZoneOffsetHour >= 24) {
					int oldDayOffset = this.dayOffset;
					this.dayOffset = 1;

					firePropertyChange(DAYOFFSET_PROPERTY, oldDayOffset,
							dayOffset);
				} else if (java.util.Calendar.getInstance().get(
						java.util.Calendar.HOUR_OF_DAY)
						- this.timeZoneOffsetHour < 0) {
					int oldDayOffset = this.dayOffset;
					this.dayOffset = -1;
					firePropertyChange(DAYOFFSET_PROPERTY, oldDayOffset,
							dayOffset);
				} else {
					int oldDayOffset = this.dayOffset;
					this.dayOffset = 0;
					firePropertyChange(DAYOFFSET_PROPERTY, oldDayOffset,
							dayOffset);
				}
			}
			repaint(0, 0, getWidth(), getHeight());
		}
	}

	@Override
	public String toString() {
		return "Analog Clock";
	}
}
