package mfi.riseandshinepi.gui.cardpanes;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import mfi.riseandshinepi.gui.components.TouchButton;
import mfi.riseandshinepi.logic.AlarmType;
import mfi.riseandshinepi.logic.Processor;
import mfi.riseandshinepi.logic.Utils;

public class AlarmSettingsPane extends AbstractPane implements ActionListener {

	private static final long serialVersionUID = 1L;

	private Processor processor;
	private TouchButton switchButton;
	private TouchButton[] alarmButton;

	private TouchButton[] settingButton;
	String[] settingButtonText = new String[] { "-1 Std", "+1 Std", "-1 Min", "+1 Min", "Mo-Fr / tägl.", "einmalig" };
	String[] settingButtonName = new String[] { "-h", "+h", "-m", "+m", "interval", "once" };

	private int editIndex = 0;

	public AlarmSettingsPane(Processor processor) {

		super();
		this.processor = processor;

		alarmButton = new TouchButton[processor.getAlarmController().getAlarms().size()];
		for (int i = 0; i < processor.getAlarmController().getAlarms().size(); i++) {
			alarmButton[i] = new TouchButton(processor.getAlarmController().getAlarms().get(0).toDisplayableString());
			alarmButton[i].setBounds(0, i * 42, 240, 40);
			alarmButton[i].setName(String.valueOf(i));
			alarmButton[i].addActionListener(this);
			this.add(alarmButton[i]);
		}

		settingButton = new TouchButton[6];
		for (int i = 0; i < settingButton.length; i++) {
			settingButton[i] = new TouchButton(settingButtonText[i]);
			int x = i % 2 == 0 ? 0 : 121;
			int y = (processor.getAlarmController().getAlarms().size() * 42) + (i / 2 * 42) + 12;
			settingButton[i].setBounds(x, y, 119, 40);
			settingButton[i].setName(settingButtonName[i]);
			settingButton[i].addActionListener(this);
			this.add(settingButton[i]);
		}

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
			editIndex = Integer.parseInt(name);
			processor.getAlarmController().getAlarms().get(editIndex).setActive(!processor.getAlarmController().getAlarms().get(editIndex).isActive());
		} else {
			int h = processor.getAlarmController().getAlarms().get(editIndex).getHour();
			int m = processor.getAlarmController().getAlarms().get(editIndex).getMinute();
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
				if (processor.getAlarmController().getAlarms().get(editIndex).getAlarmType() == AlarmType.DAILY) {
					processor.getAlarmController().getAlarms().get(editIndex).setAlarmType(AlarmType.WEEKDAYS);
				} else {
					processor.getAlarmController().getAlarms().get(editIndex).setAlarmType(AlarmType.DAILY);
				}
				break;
			case "once":
				processor.getAlarmController().getAlarms().get(editIndex).setAlarmType(AlarmType.ONCE);
				break;
			}
			processor.getAlarmController().getAlarms().get(editIndex).setHour(h);
			processor.getAlarmController().getAlarms().get(editIndex).setMinute(m);
		}
		refresh();
	}

	@Override
	public void refresh() {

		for (int i = 0; i < processor.getAlarmController().getAlarms().size(); i++) {
			if (editIndex == i) {
				alarmButton[i].setText("[ " + processor.getAlarmController().getAlarms().get(i).toDisplayableString() + " ]");
			} else {
				alarmButton[i].setText(processor.getAlarmController().getAlarms().get(i).toDisplayableString());
			}
			if (processor.getAlarmController().getAlarms().get(i).isActive()) {
				alarmButton[i].setActiveLook();
			} else {
				alarmButton[i].setInactiveLook();
			}
		}

		settingButton[4].setVisible(processor.getAlarmController().getAlarms().get(editIndex).getAlarmType() != AlarmType.SNOOZE);
		settingButton[5].setVisible(processor.getAlarmController().getAlarms().get(editIndex).getAlarmType() != AlarmType.SNOOZE);

	}

	@Override
	public boolean showsWeatherInformation() {
		return false;
	}

}