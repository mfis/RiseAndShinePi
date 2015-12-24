package mfi.riseandshinepi.gui.cardpanes;

import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import mfi.riseandshinepi.gui.components.TouchButton;
import mfi.riseandshinepi.gui.components.TouchLabel;
import mfi.riseandshinepi.logic.ApplicationProperties;
import mfi.riseandshinepi.logic.Processor;

public class DisplayAutoOffSettingsPane extends AbstractPane implements ActionListener {

	private static final long serialVersionUID = 1L;

	private Processor processor;

	private TouchButton switchButton;

	private TouchLabel[] displayOnLabel;
	private String[] displayOnLabelText = new String[] { "Display einschalten", "##1 Std vor Weckzeit", "oder um ##2 Uhr" };
	private TouchButton[] displayOnButton;
	private String[] displayOnButtonText = new String[] { "+1 Stunde", "-1 Stunde" };
	private String[] displayOnButtonName = new String[] { "dispOn+h", "dispOn-h" };

	private TouchLabel[] displayOffLabel;
	private String[] displayOffLabelText = new String[] { "Display ausschalten", "nach ##1 Min Inaktivität", "oder um ##2 Uhr" };
	private TouchButton[] displayOffButton;
	private String[] displayOffButtonText = new String[] { "+1 Stunde", "-1 Stunde" };
	private String[] displayOffButtonName = new String[] { "dispOff+h", "dispOff-h" };

	public DisplayAutoOffSettingsPane(Processor processor) throws HeadlessException {

		super();
		this.processor = processor;

		displayOnLabel = new TouchLabel[displayOnLabelText.length];
		for (int i = 0; i < displayOnLabelText.length; i++) {
			displayOnLabel[i] = new TouchLabel("");
			displayOnLabel[i].setBounds(0, 20 * i, 240, 20);
			this.add(displayOnLabel[i]);
		}
		displayOnButton = new TouchButton[displayOnButtonText.length];
		for (int i = 0; i < displayOnButtonText.length; i++) {
			displayOnButton[i] = new TouchButton(displayOnButtonText[i]);
			int x = i % 2 == 0 ? 0 : 121;
			displayOnButton[i].setBounds(x, 60, 119, 40);
			displayOnButton[i].setName(displayOnButtonName[i]);
			displayOnButton[i].addActionListener(this);
			this.add(displayOnButton[i]);
		}

		displayOffLabel = new TouchLabel[displayOffLabelText.length];
		for (int i = 0; i < displayOffLabelText.length; i++) {
			displayOffLabel[i] = new TouchLabel("");
			displayOffLabel[i].setBounds(0, 120 + (20 * i), 240, 20);
			this.add(displayOffLabel[i]);
		}
		displayOffButton = new TouchButton[displayOffButtonText.length];
		for (int i = 0; i < displayOffButtonText.length; i++) {
			displayOffButton[i] = new TouchButton(displayOffButtonText[i]);
			int x = i % 2 == 0 ? 0 : 121;
			displayOffButton[i].setBounds(x, 180, 119, 40);
			displayOffButton[i].setName(displayOffButtonName[i]);
			displayOffButton[i].addActionListener(this);
			this.add(displayOffButton[i]);
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

		String name = ((TouchButton) e.getSource()).getName();

		int hOn = processor.getDisplayOffController().getDisplayOnFixHour();
		int hOff = processor.getDisplayOffController().getDisplayOffFixHour();
		switch (name) {
		case "dispOn+h":
			if (hOn == 23) {
				hOn = 0;
			} else {
				hOn++;
			}
			break;
		case "dispOn-h":
			if (hOn == 0) {
				hOn = 23;
			} else {
				hOn--;
			}
			break;
		case "dispOff+h":
			if (hOff == 23) {
				hOff = 0;
			} else {
				hOff++;
			}
			break;
		case "dispOff-h":
			if (hOff == 0) {
				hOff = 23;
			} else {
				hOff--;
			}
			break;
		}

		processor.getDisplayOffController().setDisplayOnFixHour(hOn);
		processor.getDisplayOffController().setDisplayOffFixHour(hOff);

		refresh();
	}

	@Override
	public void refresh() {

		for (int i = 0; i < displayOnLabelText.length; i++) {
			String text = displayOnLabelText[i];
			text = text.replace("##1", ApplicationProperties.DISPLAY_ON_X_HOURS_BEFORE_ALARM.toString());
			text = text.replace("##2", String.valueOf(processor.getDisplayOffController().getDisplayOnFixHour()));
			displayOnLabel[i].setText(text);
		}

		for (int i = 0; i < displayOffLabelText.length; i++) {
			String text = displayOffLabelText[i];
			text = text.replace("##1", ApplicationProperties.DISPLAY_OFF_X_MINUTES_IN_INACTIVITY.toString());
			text = text.replace("##2", String.valueOf(processor.getDisplayOffController().getDisplayOffFixHour()));
			displayOffLabel[i].setText(text);
		}

	}

	@Override
	public boolean showsWeatherInformation() {
		return false;
	}
}