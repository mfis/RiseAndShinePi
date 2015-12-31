package mfi.riseandshinepi.gui.cardpanes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import mfi.riseandshinepi.gui.components.AnalogClock;
import mfi.riseandshinepi.gui.components.TouchButton;
import mfi.riseandshinepi.listeners.SnoozeListener;
import mfi.riseandshinepi.logic.Processor;

public class AlarmPane extends AbstractPane {

	private static final long serialVersionUID = 1L;

	private JLabel weatherTodayTemperature;
	private JLabel weatherTodayCondition;
	private JLabel weatherActualTemperature;
	private JLabel weatherInfo1;
	private JLabel weatherInfo2;
	private Processor processor;
	private TouchButton touchButton;
	private AnalogClock clock = new AnalogClock();
	private Font font = new Font("Arial", Font.BOLD, 18);
	private Font fontSmall = new Font("Arial", Font.BOLD, 12);

	public AlarmPane(Processor processor) {

		super();
		this.processor = processor;
		this.setBackground(Color.BLACK);

		JDesktopPane panel = new JDesktopPane();
		panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		panel.setSize(240, 320);
		panel.setPreferredSize(new Dimension(240, 320));
		panel.setBackground(Color.BLACK);

		touchButton = new TouchButton("");
		touchButton.setBounds(0, 0, 240, 320);
		touchButton.setBackground(Color.BLACK);
		touchButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		touchButton.addMouseListener(new SnoozeListener(processor));
		touchButton.setName("");
		touchButton.add(panel);

		clock.setName("clock");
		clock.setPreferredSize(new java.awt.Dimension(180, 180));
		clock.setBounds(30, 10, 180, 180);
		panel.add(clock);

		weatherActualTemperature = new JLabel("", SwingConstants.CENTER);
		weatherActualTemperature.setForeground(Color.LIGHT_GRAY);
		weatherActualTemperature.setFont(font);
		weatherActualTemperature.setBounds(0, 200, 240, 30);
		panel.add(weatherActualTemperature);

		weatherTodayTemperature = new JLabel("", SwingConstants.CENTER);
		weatherTodayTemperature.setForeground(Color.LIGHT_GRAY);
		weatherTodayTemperature.setFont(font);
		weatherTodayTemperature.setBounds(0, 235, 240, 30);
		panel.add(weatherTodayTemperature);

		weatherTodayCondition = new JLabel("", SwingConstants.CENTER);
		weatherTodayCondition.setBounds(0, 255, 240, 30);
		weatherTodayCondition.setForeground(Color.LIGHT_GRAY);
		weatherTodayCondition.setFont(font);
		panel.add(weatherTodayCondition);

		weatherInfo1 = new JLabel("", SwingConstants.CENTER);
		weatherInfo1.setForeground(Color.GRAY);
		weatherInfo1.setFont(fontSmall);
		weatherInfo1.setBounds(0, 280, 240, 30);
		panel.add(weatherInfo1);

		weatherInfo2 = new JLabel("", SwingConstants.CENTER);
		weatherInfo2.setBounds(0, 295, 240, 30);
		weatherInfo2.setForeground(Color.GRAY);
		weatherInfo2.setFont(fontSmall);
		panel.add(weatherInfo2);

		this.add(touchButton);
	}

	@Override
	public void refresh() {

		weatherActualTemperature.setText("Aktuell " + processor.getWeatherController().getActualTemperature());
		if (processor.getWeatherController().isDataAvailable()) {
			weatherTodayTemperature.setText(
					"Heute " + processor.getWeatherController().getTodayMinTemperature() + " bis " + processor.getWeatherController().getTodayMaxTemperature());
		} else {
			weatherTodayTemperature.setText("");
		}
		weatherTodayCondition.setText(processor.getWeatherController().getTodayCondition());

		if (processor.isAlarmNowOn()) {
			weatherInfo1.setVisible(false);
			weatherInfo2.setVisible(false);
		} else {
			weatherInfo1.setVisible(true);
			weatherInfo2.setVisible(true);
			weatherInfo1.setText(processor.getWeatherController().getWeatherLocation());
			weatherInfo2.setText(processor.getWeatherController().getProvider());
		}
	}

	@Override
	public boolean showsWeatherInformation() {
		return true;
	}

	@Override
	public void setVisible(boolean aFlag) {
		super.setVisible(aFlag);
		if (clock != null) {
			clock.setVisible(aFlag);
		}
	}

}