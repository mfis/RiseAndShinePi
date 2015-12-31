package mfi.riseandshinepi.gui.cardpanes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import mfi.riseandshinepi.gui.components.TouchButton;
import mfi.riseandshinepi.logic.Processor;

public class SnoozePane extends AbstractPane implements ActionListener {

	private static final long serialVersionUID = 1L;

	private static final int COUNTDOWN_LENGTH_SEC = 30;
	private static final int COUNTDOWN_MIN_LENGTH_AFTER_CLICK_SEC = 5;
	private static final int MAX_SNOOZE_MIN = 120;
	private static final int SNOOZE_STEP_MIN = 10;

	private JLabel labelOff;
	private JLabel labelOffSeconds;
	private JLabel labelSnooze;
	private JLabel labelSnoozeMinutes;
	private Processor processor;
	private TouchButton touchButton;
	private Font fontSmall = new Font("Arial", Font.BOLD, 14);
	private Font fontBig = new Font("Arial", Font.BOLD, 26);
	private final Timer timer = new Timer(1000, this);
	private int countdownSeconds = 0;
	private int snoozeMinutes = 0;

	private boolean lastVisibilityStatus = false;

	public SnoozePane(Processor processor) {

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
		touchButton.addActionListener(this);
		touchButton.setName("");
		touchButton.add(panel);

		labelOff = new JLabel("Licht wird ausgeschaltet in", SwingConstants.CENTER);
		labelOff.setForeground(Color.WHITE);
		labelOff.setFont(fontSmall);
		labelOff.setBounds(0, 20, 240, 30);
		panel.add(labelOff);

		labelOffSeconds = new JLabel("", SwingConstants.CENTER);
		labelOffSeconds.setForeground(Color.WHITE);
		labelOffSeconds.setFont(fontBig);
		labelOffSeconds.setBounds(0, 50, 240, 40);
		panel.add(labelOffSeconds);

		labelSnooze = new JLabel("", SwingConstants.CENTER);
		labelSnooze.setBounds(0, 140, 240, 30);
		labelSnooze.setForeground(Color.WHITE);
		labelSnooze.setFont(fontSmall);
		panel.add(labelSnooze);

		labelSnoozeMinutes = new JLabel("", SwingConstants.CENTER);
		labelSnoozeMinutes.setForeground(Color.WHITE);
		labelSnoozeMinutes.setFont(fontBig);
		labelSnoozeMinutes.setBounds(0, 170, 240, 40);
		panel.add(labelSnoozeMinutes);

		this.add(touchButton);
	}

	@Override
	public void refresh() {

		labelOffSeconds.setText(countdownSeconds + " Sekunden");

		if (snoozeMinutes == 0) {
			labelSnooze.setText("Tippen zum schlummern");
			labelSnooze.setForeground(Color.ORANGE);
			labelSnoozeMinutes.setText("");
		} else {
			labelSnooze.setText("Schlummern für");
			labelSnooze.setForeground(Color.WHITE);
			labelSnoozeMinutes.setText(snoozeMinutes + " Minuten");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() instanceof TouchButton) {
			if (snoozeMinutes == MAX_SNOOZE_MIN) {
				snoozeMinutes = 0;
			} else {
				snoozeMinutes = snoozeMinutes + SNOOZE_STEP_MIN;
			}
			if (countdownSeconds < COUNTDOWN_MIN_LENGTH_AFTER_CLICK_SEC) {
				countdownSeconds = COUNTDOWN_MIN_LENGTH_AFTER_CLICK_SEC;
			}
		} else {
			// Timer
			countdownSeconds--;
			if (countdownSeconds < 1) {
				stopCountdown();
			}
		}

		refresh();
	}

	private synchronized void checkVisibilityStatus(boolean visibilityNew) {

		if (visibilityNew == lastVisibilityStatus) {
			return;
		}
		lastVisibilityStatus = visibilityNew;

		if (visibilityNew) {
			startCountdown();
		} else {
			stopCountdown();
		}
	}

	private void startCountdown() {
		snoozeMinutes = 0;
		countdownSeconds = COUNTDOWN_LENGTH_SEC;
		refresh();
		if (!timer.isRunning()) {
			timer.start();
		}
	}

	private void stopCountdown() {

		if (timer.isRunning()) {
			timer.stop();
		}
		processor.alarmOff();

		if (snoozeMinutes > 0) {
			processor.getAlarmController().activateSnoozeAlarm(snoozeMinutes);
		}

		processor.switchGuiTo(ClockPane.class.getName());
	}

	@Override
	public boolean showsWeatherInformation() {
		return false;
	}

	@Override
	public void setVisible(boolean aFlag) {
		super.setVisible(aFlag);
		checkVisibilityStatus(aFlag);
	}

}