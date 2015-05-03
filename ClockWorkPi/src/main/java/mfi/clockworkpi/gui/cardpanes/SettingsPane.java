package mfi.clockworkpi.gui.cardpanes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDesktopPane;

import mfi.clockworkpi.gui.components.Gui;
import mfi.clockworkpi.gui.components.TouchButton;
import mfi.clockworkpi.logic.Processor;
import mfi.clockworkpi.logic.Utils;

public class SettingsPane extends JDesktopPane implements ActionListener {

	private static final long serialVersionUID = 1L;

	private Processor processor;
	private TouchButton switchButton;
	private TouchButton[] alarmButton;

	private TouchButton[] settingButton;
	String[] settingButtonText = new String[] { "+1 Stunde", "+1 Minute", "-1 Stunde", "-1 Minute", "Intervall", "Einmalig" };
	String[] settingButtonName = new String[] { "+h", "+m", "-h", "-m", "interval", "once" };

	public SettingsPane(Processor processor) throws HeadlessException {

		this.processor = processor;

		alarmButton = new TouchButton[processor.getAlarms().size()];
		for (int i = 0; i < processor.getAlarms().size(); i++) {
			alarmButton[i] = new TouchButton(processor.getAlarms().get(0).toString());
			alarmButton[i].setBounds(0, i * 42, 240, 40);
			alarmButton[i].setName(String.valueOf(i));
			alarmButton[i].addActionListener(this);
			this.add(alarmButton[i]);
		}

		settingButton = new TouchButton[6];
		for (int i = 0; i < settingButton.length; i++) {
			settingButton[i] = new TouchButton(settingButtonText[i]);
			int x = i % 2 == 0 ? 0 : 121;
			int y = (processor.getAlarms().size() * 42) + (i / 2 * 42) + 12;
			settingButton[i].setBounds(x, y, 119, 40);
			settingButton[i].setName(settingButtonName[i]);
			settingButton[i].setForeground(Color.LIGHT_GRAY);
			settingButton[i].addActionListener(this);
			this.add(settingButton[i]);
		}

		refreshButtons();

		switchButton = new TouchButton("Uhr anzeigen");
		switchButton.setBounds(0, 280, 240, 40);
		switchButton.addActionListener(processor.getGui().getSwitchButtonListener());
		switchButton.setName(ClockPane.class.getName());
		this.add(switchButton);

		this.setBackground(Color.BLACK);

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		String name = ((JButton) e.getSource()).getName();

		if (Utils.isStringValueNumeric(name)) {
			int index = Integer.parseInt(name);
			Integer newAlarmIndex;
			if (processor.getActiveAlarm() != null && index == processor.getActiveAlarm()) {
				newAlarmIndex = null;
			} else {
				newAlarmIndex = index;
			}
			processor.setActiveAlarm(newAlarmIndex);
		} else {
			int h = processor.getAlarms().get(processor.getActiveAlarm()).getHour();
			int m = processor.getAlarms().get(processor.getActiveAlarm()).getMinute();
			switch (name) {
			case "+h":
				if (h == 23) {
					h = 0;
				} else {
					h++;
				}
				break;
			case "+m":
				if (m == 59) {
					m = 0;
				} else {
					m++;
				}
				break;
			case "-h":
				if (h == 0) {
					h = 23;
				} else {
					h--;
				}
				break;
			case "-m":
				if (m == 0) {
					m = 59;
				} else {
					m--;
				}
				break;
			case "interval":
				processor.getAlarms().get(processor.getActiveAlarm()).setOnWeekdaysOnly(!processor.getAlarms().get(processor.getActiveAlarm()).isOnWeekdaysOnly());
				processor.getAlarms().get(processor.getActiveAlarm()).setOnce(false);
				break;
			case "once":
				processor.getAlarms().get(processor.getActiveAlarm()).setOnce(true);
				break;
			}
			processor.getAlarms().get(processor.getActiveAlarm()).setHour(h);
			processor.getAlarms().get(processor.getActiveAlarm()).setMinute(m);
		}
		refreshButtons();
	}

	public void refreshButtons() {

		for (int i = 0; i < processor.getAlarms().size(); i++) {
			if (processor.getActiveAlarm() != null && processor.getActiveAlarm() == i) {
				alarmButton[i].setForeground(Color.WHITE);
			} else {
				alarmButton[i].setForeground(Color.BLACK);
			}
			alarmButton[i].setText(processor.getAlarms().get(i).toString());
		}

		for (int i = 0; i < settingButton.length; i++) {
			settingButton[i].setVisible(processor.getActiveAlarm() != null);
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return (Gui.applicationSize);
	}

}