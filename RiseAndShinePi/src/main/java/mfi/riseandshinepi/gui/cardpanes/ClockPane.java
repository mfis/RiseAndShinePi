package mfi.riseandshinepi.gui.cardpanes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import mfi.riseandshinepi.gui.components.AnalogClock;
import mfi.riseandshinepi.gui.components.TouchButton;
import mfi.riseandshinepi.listeners.AnalogClockMouseListener;
import mfi.riseandshinepi.logic.Processor;
import mfi.riseandshinepi.logic.Utils;

public class ClockPane extends AbstractPane implements ActionListener {

	private static final long serialVersionUID = 1L;
	private TouchButton clockButton;
	private TouchButton switchButton;
	private final Timer timer = new Timer(1000, this);
	private JLabel labelActualDate;
	private JLabel labelNextAlarm;
	private Processor processor;
	private SimpleDateFormat sdf = Utils.getSimpleDateFormat("EE, d. MMM yyyy");

	public ClockPane(Processor processor) {

		this.processor = processor;

		AnalogClock clock = new AnalogClock();
		clock.setName("clock");
		clock.setPreferredSize(new java.awt.Dimension(240, 240));
		clock.setBounds(0, 0, 240, 240);
		clock.setType(AnalogClock.TYPE.DARK);
		clock.setSecondPointerVisible(false);
		clock.setAutoType(false);

		JPanel clockPanel = new JPanel();
		clockPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		clockPanel.setSize(240, 240);
		clockPanel.setPreferredSize(new Dimension(240, 240));
		clockPanel.setBackground(Color.BLACK);
		clockPanel.add(clock);

		clockButton = new TouchButton("");
		clockButton.setBounds(0, 0, 240, 240);
		clockButton.addMouseListener(new AnalogClockMouseListener(processor));
		clockButton.setName(AlarmSettingsPane.class.getName());
		clockButton.setBackground(Color.BLACK);
		clockButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		clockButton.add(clockPanel);

		switchButton = new TouchButton("");
		switchButton.setBounds(0, 240, 240, 80);
		switchButton.addActionListener(processor.getGui().getSwitchButtonListener());
		switchButton.setName(SettingsSummaryPane.class.getName());
		switchButton.setBackground(Color.BLACK);
		switchButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		JDesktopPane p = new JDesktopPane();
		p.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		p.setSize(240, 80);
		p.setPreferredSize(new Dimension(240, 80));
		p.setForeground(Color.DARK_GRAY);
		p.setBackground(Color.BLACK);

		labelActualDate = new JLabel("", SwingConstants.CENTER);
		labelActualDate.setForeground(Color.LIGHT_GRAY);
		Font font = new Font("Arial", Font.BOLD, 16);
		labelActualDate.setFont(font);
		labelActualDate.setBounds(0, 10, 240, 30);
		p.add(labelActualDate);

		labelNextAlarm = new JLabel("", SwingConstants.CENTER);
		labelNextAlarm.setBounds(0, 40, 240, 30);
		labelNextAlarm.setForeground(Color.LIGHT_GRAY);
		labelNextAlarm.setFont(font);
		p.add(labelNextAlarm);
		switchButton.add(p);

		this.setBackground(Color.BLACK);
		this.add(clockButton);
		this.add(switchButton);

		timer.start();
	}

	@Override
	public void refresh() {

		labelActualDate.setText(sdf.format(new Date()));

		String alarm;
		if (processor.nextAlarmTimeString() == null) {
			alarm = "Wecker ist aus";
		} else {
			alarm = "Wecker: " + processor.nextAlarmTimeString();
		}
		labelNextAlarm.setText(alarm);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		refresh();

	}

}