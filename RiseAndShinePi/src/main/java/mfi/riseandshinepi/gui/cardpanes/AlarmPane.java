package mfi.riseandshinepi.gui.cardpanes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import mfi.riseandshinepi.gui.components.AnalogClock;
import mfi.riseandshinepi.gui.components.TouchButton;
import mfi.riseandshinepi.logic.Processor;
import mfi.riseandshinepi.logic.Utils;
import mfi.riseandshinepi.webservices.YahooWeatherWebService;

public class AlarmPane extends AbstractPane implements ActionListener {

	private static final long serialVersionUID = 1L;
	private final Timer timer = new Timer(1000, this);
	private JLabel labelActualDate;
	private JLabel labelNextAlarm;
	private JLabel actDate;
	private JLabel actTemp;
	private Processor processor;
	private TouchButton musicOffButton;
	private TouchButton alarmOffButton;

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

		labelActualDate = new JLabel("", SwingConstants.CENTER);
		labelActualDate.setForeground(Color.LIGHT_GRAY);
		Font font = new Font("Arial", Font.BOLD, 16);
		labelActualDate.setFont(font);
		labelActualDate.setBounds(0, 150, 240, 30);
		this.add(labelActualDate);

		labelNextAlarm = new JLabel("", SwingConstants.CENTER);
		labelNextAlarm.setBounds(0, 175, 240, 30);
		labelNextAlarm.setForeground(Color.LIGHT_GRAY);
		labelNextAlarm.setFont(font);
		this.add(labelNextAlarm);

		actDate = new JLabel("24.10.", SwingConstants.LEFT);
		actDate.setBounds(10, 0, 50, 30);
		actDate.setForeground(Color.DARK_GRAY);
		actDate.setFont(font);
		this.add(actDate);
		
		actTemp = new JLabel("24" + YahooWeatherWebService.DEGREE + "C", SwingConstants.RIGHT);
		actTemp.setBounds(180, 0, 50, 30);
		actTemp.setForeground(Color.DARK_GRAY);
		actTemp.setFont(font);
		this.add(actTemp);

		musicOffButton = new TouchButton("Musik ausschalten");
		musicOffButton.setBounds(0, 220, 240, 40);
		musicOffButton.addActionListener(processor.getGui().getSwitchButtonListener());
		musicOffButton.setName(AlarmSettingsPane.class.getName());
		musicOffButton.setBackground(Color.DARK_GRAY);
		musicOffButton.setForeground(Color.LIGHT_GRAY);
		this.add(musicOffButton);

		alarmOffButton = new TouchButton("Wecker ausschalten");
		alarmOffButton.setBounds(0, 280, 240, 40);
		alarmOffButton.addActionListener(processor.getGui().getSwitchButtonListener());
		alarmOffButton.setName(AlarmSettingsPane.class.getName());
		alarmOffButton.setBackground(Color.DARK_GRAY);
		alarmOffButton.setForeground(Color.LIGHT_GRAY);
		this.add(alarmOffButton);

		timer.start();
	}

	@Override
	public void refresh() {

		labelActualDate.setText( "Heute 19" + YahooWeatherWebService.DEGREE + "C bis 23" + YahooWeatherWebService.DEGREE + "C");
		labelNextAlarm.setText("Überwiegend sonnig");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		refresh();

	}

}