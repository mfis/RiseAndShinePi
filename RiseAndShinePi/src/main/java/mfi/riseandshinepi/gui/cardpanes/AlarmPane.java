package mfi.riseandshinepi.gui.cardpanes;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import mfi.riseandshinepi.gui.components.AnalogClock;
import mfi.riseandshinepi.gui.components.TouchButton;
import mfi.riseandshinepi.logic.Processor;

public class AlarmPane extends AbstractPane implements ActionListener {

	private static final long serialVersionUID = 1L;
	private final static SimpleDateFormat sdfActualDate = new SimpleDateFormat("dd.MM.");
	private JLabel weatherTodayTemperature;
	private JLabel weatherTodayCondition;
	private JLabel weatherActualTemperature;
	private JLabel actualDate;
	private JLabel weatherInfo1;
	private JLabel weatherInfo2;
	private Processor processor;
	private TouchButton musicOffButton;
	private TouchButton alarmOffButton;
	private TouchButton switchButton;

	public AlarmPane(Processor processor) {

		this.processor = processor;
		this.setBackground(Color.BLACK);

		AnalogClock clock = new AnalogClock();
		clock.setName("clock");
		clock.setPreferredSize(new java.awt.Dimension(140, 140));
		clock.setBounds(50, 5, 140, 140);
		clock.setType(AnalogClock.TYPE.DARK);
		clock.setSecondPointerVisible(false);
		clock.setAutoType(false);
		this.add(clock);

		weatherTodayTemperature = new JLabel("", SwingConstants.CENTER);
		weatherTodayTemperature.setForeground(Color.LIGHT_GRAY);
		Font font = new Font("Arial", Font.BOLD, 16);
		weatherTodayTemperature.setFont(font);
		weatherTodayTemperature.setBounds(0, 150, 240, 30);
		this.add(weatherTodayTemperature);

		weatherTodayCondition = new JLabel("", SwingConstants.CENTER);
		weatherTodayCondition.setBounds(0, 175, 240, 30);
		weatherTodayCondition.setForeground(Color.LIGHT_GRAY);
		weatherTodayCondition.setFont(font);
		this.add(weatherTodayCondition);

		actualDate = new JLabel("", SwingConstants.LEFT);
		actualDate.setBounds(10, 0, 50, 30);
		actualDate.setForeground(Color.DARK_GRAY);
		actualDate.setFont(font);
		this.add(actualDate);

		weatherActualTemperature = new JLabel("", SwingConstants.RIGHT);
		weatherActualTemperature.setBounds(180, 0, 50, 30);
		weatherActualTemperature.setForeground(Color.DARK_GRAY);
		weatherActualTemperature.setFont(font);
		this.add(weatherActualTemperature);

		weatherInfo1 = new JLabel("", SwingConstants.CENTER);
		weatherInfo1.setForeground(Color.LIGHT_GRAY);
		weatherInfo1.setFont(font);
		weatherInfo1.setBounds(0, 210, 240, 30);
		this.add(weatherInfo1);

		weatherInfo2 = new JLabel("", SwingConstants.CENTER);
		weatherInfo2.setBounds(0, 235, 240, 30);
		weatherInfo2.setForeground(Color.LIGHT_GRAY);
		weatherInfo2.setFont(font);
		this.add(weatherInfo2);

		musicOffButton = new TouchButton("");
		musicOffButton.setBounds(0, 220, 240, 40);
		musicOffButton.addActionListener(this);
		musicOffButton.setName("music_on_off");
		this.add(musicOffButton);

		alarmOffButton = new TouchButton("Wecker ausschalten");
		alarmOffButton.setBounds(0, 280, 240, 40);
		alarmOffButton.addActionListener(this);
		alarmOffButton.setName("alarm_off");
		this.add(alarmOffButton);

		switchButton = new TouchButton("Uhr anzeigen");
		switchButton.setBounds(0, 280, 240, 40);
		switchButton.addActionListener(processor.getGui().getSwitchButtonListener());
		switchButton.setName(ClockPane.class.getName());
		this.add(switchButton);

	}

	@Override
	public void refresh() {

		actualDate.setText(sdfActualDate.format(processor.getActualCalendar().getTime()));
		weatherActualTemperature.setText(processor.getWeatherController().getActualTemperature());
		if (processor.getWeatherController().isDataAvailable()) {
			weatherTodayTemperature.setText("Heute " + processor.getWeatherController().getTodayMinTemperature()
					+ " bis " + processor.getWeatherController().getTodayMaxTemperature());
		} else {
			weatherTodayTemperature.setText("");
		}
		weatherTodayCondition.setText(processor.getWeatherController().getTodayCondition());

		if (processor.isAlarmNowOn()) {
			musicOffButton.setVisible(true);
			alarmOffButton.setVisible(true);
			switchButton.setVisible(false);
			weatherInfo1.setVisible(false);
			weatherInfo2.setVisible(false);
			if (processor.getAudioPlayer().isPlaying()) {
				musicOffButton.setText("Musik ausschalten");
			} else {
				musicOffButton.setText("Musik einschalten");
			}
		} else {
			musicOffButton.setVisible(false);
			alarmOffButton.setVisible(false);
			switchButton.setVisible(true);
			weatherInfo1.setVisible(true);
			weatherInfo2.setVisible(true);
			weatherInfo1.setText(processor.getWeatherController().getWeatherLocation());
			weatherInfo2.setText(processor.getWeatherController().getProvider());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		String name = ((TouchButton) e.getSource()).getName();

		switch (name) {
		case "music_on_off":
			if (processor.getAudioPlayer().isPlaying()) {
				processor.getAudioPlayer().stop();
			} else {
				processor.getAudioPlayer().start();
			}
			break;
		case "alarm_off":
			processor.alarmOff();
			processor.switchGuiTo(ClockPane.class.getName());
			break;
		}

		refresh();
	}

}