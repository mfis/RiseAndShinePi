package mfi.riseandshinepi.gui.cardpanes;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import mfi.riseandshinepi.gui.components.TouchButton;
import mfi.riseandshinepi.gui.components.TouchLabel;
import mfi.riseandshinepi.hardware.DisplayBacklight;
import mfi.riseandshinepi.logic.Processor;

public class VolumeAndBacklightSettingsPane extends AbstractPane implements ActionListener {

	private static final long serialVersionUID = 1L;

	private Processor processor;

	private TouchLabel[] displayBacklightLabel;
	private String[] displayBacklightLabelText = new String[] { "Display-Helligkeit", "##1 / " + DisplayBacklight.MAX_VALUE };
	private TouchButton[] displayOnButton;
	private String[] displayBacklightButtonText = new String[] { "heller", "dunkler" };
	private String[] displayBlacklightButtonName = new String[] { "dispBL+", "dispBL-" };

	private TouchLabel[] volumeLabel;
	private String[] volumeLabelText = new String[] { "Lautstärke", "##1 %" };
	private TouchButton[] volumeButton;
	private String[] volumeButtonText = new String[] { "lauter", "leiser" };
	private String[] volumeButtonName = new String[] { "vol+", "vol-" };

	private TouchButton playerButton;
	private TouchButton switchButton;

	private final Timer timer = new Timer(1000, this);

	public VolumeAndBacklightSettingsPane(Processor processor) {

		this.processor = processor;

		displayBacklightLabel = new TouchLabel[displayBacklightLabelText.length];
		for (int i = 0; i < displayBacklightLabelText.length; i++) {
			displayBacklightLabel[i] = new TouchLabel("");
			displayBacklightLabel[i].setBounds(0, 20 * i, 240, 20);
			this.add(displayBacklightLabel[i]);
		}
		displayOnButton = new TouchButton[displayBacklightButtonText.length];
		for (int i = 0; i < displayBacklightButtonText.length; i++) {
			displayOnButton[i] = new TouchButton(displayBacklightButtonText[i]);
			int x = i % 2 == 0 ? 0 : 121;
			displayOnButton[i].setBounds(x, 40, 119, 40);
			displayOnButton[i].setName(displayBlacklightButtonName[i]);
			displayOnButton[i].addActionListener(this);
			this.add(displayOnButton[i]);
		}

		volumeLabel = new TouchLabel[volumeLabelText.length];
		for (int i = 0; i < volumeLabelText.length; i++) {
			volumeLabel[i] = new TouchLabel("");
			volumeLabel[i].setBounds(0, 100 + (20 * i), 240, 20);
			this.add(volumeLabel[i]);
		}

		playerButton = new TouchButton("");
		playerButton.setBounds(0, 140, 240, 40);
		playerButton.addActionListener(this);
		playerButton.setName("player");
		this.add(playerButton);

		volumeButton = new TouchButton[volumeButtonText.length];
		for (int i = 0; i < volumeButtonText.length; i++) {
			volumeButton[i] = new TouchButton(volumeButtonText[i]);
			int x = i % 2 == 0 ? 0 : 121;
			volumeButton[i].setBounds(x, 182, 119, 40);
			volumeButton[i].setName(volumeButtonName[i]);
			volumeButton[i].addActionListener(this);
			this.add(volumeButton[i]);
		}

		switchButton = new TouchButton("Uhr anzeigen");
		switchButton.setBounds(0, 280, 240, 40);
		switchButton.addActionListener(processor.getGui().getSwitchButtonListener());
		switchButton.setName(ClockPane.class.getName());
		this.add(switchButton);

		this.setBackground(Color.BLACK);
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() != null && e.getSource() instanceof Timer) {
			if (processor.getAudioPlayer().isPlaying()) {
				refresh();
			}
			return;
		}

		String name = ((TouchButton) e.getSource()).getName();

		int backlight = processor.getDisplayBacklight().getActualValue();
		int vol = processor.getAudioPlayer().getActualVolumePercent() != null ? processor.getAudioPlayer().getActualVolumePercent() : 0;
		int backlightBefore = backlight;
		int volBefore = vol;

		switch (name) {
		case "dispBL+":
			backlight++;
			if (backlight > DisplayBacklight.MAX_VALUE) {
				backlight = DisplayBacklight.MAX_VALUE;
			}
			break;
		case "dispBL-":
			backlight--;
			if (backlight < DisplayBacklight.MIN_VALUE) {
				backlight = DisplayBacklight.MIN_VALUE;
			}
			break;
		case "vol+":
			vol += 2;
			if (vol > 100) {
				vol = 100;
			}
			break;
		case "vol-":
			vol -= 2;
			if (vol < 10) {
				vol = 10;
			}
			break;
		case "player":
			if (processor.getAudioPlayer().isPlaying()) {
				processor.getAudioPlayer().stop();
			} else {
				processor.getAudioPlayer().start();
			}
			break;
		}

		if (backlight != backlightBefore) {
			processor.getDisplayBacklight().dimToValue(backlight);
			processor.getDisplayBacklight().setDefaultValue(backlight);
		}
		if (vol != volBefore) {
			processor.getAudioPlayer().setVolumePercent(vol);
		}

		refresh();
	}

	@Override
	public void refresh() {

		for (int i = 0; i < displayBacklightLabelText.length; i++) {
			String text = displayBacklightLabelText[i];
			text = text.replace("##1", Integer.toString(processor.getDisplayBacklight().getActualValue()));
			displayBacklightLabel[i].setText(text);
		}

		if (processor.getAudioPlayer().isPlaying()) {
			playerButton.setText("Musik stoppen");
		} else {
			playerButton.setText("Lautstärke testen");
		}

		for (int i = 0; i < volumeLabelText.length; i++) {
			String text = volumeLabelText[i];
			text = text.replace("##1", processor.getAudioPlayer().getActualVolumePercent() != null ? Integer.toString(processor.getAudioPlayer().getActualVolumePercent())
					: "unbekannt");
			volumeLabel[i].setText(text);
		}

		for (int i = 0; i < volumeButtonText.length; i++) {
			volumeButton[i].setVisible(processor.getAudioPlayer().isPlaying());
		}

	}
	
	@Override
	public boolean showsWeatherInformation() {
		return false;
	}

}