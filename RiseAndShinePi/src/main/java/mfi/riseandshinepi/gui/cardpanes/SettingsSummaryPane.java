package mfi.riseandshinepi.gui.cardpanes;

import java.awt.Color;

import mfi.riseandshinepi.gui.components.TouchButton;
import mfi.riseandshinepi.listeners.ExitButtonMouseListener;
import mfi.riseandshinepi.logic.Processor;

public class SettingsSummaryPane extends AbstractPane {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private Processor processor;

	private TouchButton alarmSettingsButton;
	private TouchButton volumeAndBacklightSettingsButton;
	private TouchButton displayAutoOffSettingsButton;
	private TouchButton weatherButton;
	private TouchButton exitButton;
	private TouchButton switchButton;

	public SettingsSummaryPane(Processor processor) {

		this.processor = processor;

		alarmSettingsButton = new TouchButton("Wecker einstellen");
		alarmSettingsButton.setBounds(0, 0, 240, 40);
		alarmSettingsButton.addActionListener(processor.getGui().getSwitchButtonListener());
		alarmSettingsButton.setName(AlarmSettingsPane.class.getName());
		this.add(alarmSettingsButton);

		volumeAndBacklightSettingsButton = new TouchButton("Lautstärke & Helligkeit");
		volumeAndBacklightSettingsButton.setBounds(0, 55, 240, 40);
		volumeAndBacklightSettingsButton.addActionListener(processor.getGui().getSwitchButtonListener());
		volumeAndBacklightSettingsButton.setName(VolumeAndBacklightSettingsPane.class.getName());
		this.add(volumeAndBacklightSettingsButton);

		displayAutoOffSettingsButton = new TouchButton("Energieeinstellungen");
		displayAutoOffSettingsButton.setBounds(0, 110, 240, 40);
		displayAutoOffSettingsButton.addActionListener(processor.getGui().getSwitchButtonListener());
		displayAutoOffSettingsButton.setName(DisplayAutoOffSettingsPane.class.getName());
		this.add(displayAutoOffSettingsButton);

		weatherButton = new TouchButton("Wetterinformation");
		weatherButton.setBounds(0, 165, 240, 40);
		weatherButton.addActionListener(processor.getGui().getSwitchButtonListener());
		weatherButton.setName(AlarmPane.class.getName());
		this.add(weatherButton);
		
		exitButton = new TouchButton("Ausschalten (halten)");
		exitButton.setBounds(0, 220, 240, 40);
		exitButton.addMouseListener(new ExitButtonMouseListener(processor));
		exitButton.setName(DisplayAutoOffSettingsPane.class.getName());
		exitButton.setBackground(Color.RED);
		this.add(exitButton);

		switchButton = new TouchButton("Uhr anzeigen");
		switchButton.setBounds(0, 280, 240, 40);
		switchButton.addActionListener(processor.getGui().getSwitchButtonListener());
		switchButton.setName(ClockPane.class.getName());
		this.add(switchButton);

		this.setBackground(Color.BLACK);
	}

	@Override
	public void refresh() {
		// noop
	}

}