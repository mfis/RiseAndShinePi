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

public class SettingsPane extends JDesktopPane implements ActionListener {

	private static final long serialVersionUID = 1L;

	private Processor processor;
	private TouchButton switchButton;
	private TouchButton[] alarmButton;

	public SettingsPane(Processor processor) throws HeadlessException {

		this.processor = processor;

		alarmButton = new TouchButton[processor.getAlarms().size()];
		for (int i = 0; i < processor.getAlarms().size(); i++) {
			alarmButton[i] = new TouchButton(processor.getAlarms().get(0).toString());
			alarmButton[i].setBounds((i * 121), 0, 119, 40);
			alarmButton[i].setName(String.valueOf(i));
			alarmButton[i].addActionListener(this);
			this.add(alarmButton[i]);
		}
		refreshAlarmButtons();

		switchButton = new TouchButton("Uhr anzeigen");
		switchButton.setBounds(0, 280, 240, 40);
		switchButton.addActionListener(processor.getGui().getSwitchButtonListener());
		switchButton.setName(ClockPane.class.getName());
		this.add(switchButton);

		this.setBackground(Color.BLACK);

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		int index = Integer.parseInt(((JButton) e.getSource()).getName());
		Integer newAlarmIndex;
		if (processor.getActiveAlarm() != null && index == processor.getActiveAlarm()) {
			newAlarmIndex = null;
		} else {
			newAlarmIndex = index;
		}
		processor.setActiveAlarm(newAlarmIndex);
		refreshAlarmButtons();
	}

	private void refreshAlarmButtons() {

		for (int i = 0; i < processor.getAlarms().size(); i++) {
			if (processor.getActiveAlarm() != null && processor.getActiveAlarm() == i) {
				alarmButton[i].setForeground(Color.WHITE);
			} else {
				alarmButton[i].setForeground(Color.DARK_GRAY);
			}
			alarmButton[i].setText(processor.getAlarms().get(i).toString());
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return (Gui.applicationSize);
	}

}