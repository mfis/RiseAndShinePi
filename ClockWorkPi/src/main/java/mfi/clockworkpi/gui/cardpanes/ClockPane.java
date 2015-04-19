package mfi.clockworkpi.gui.cardpanes;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDesktopPane;

import mfi.clockworkpi.gui.components.AnalogClock;
import mfi.clockworkpi.listeners.SwitchButtonListener;
import mfi.clockworkpi.main.CWPMain;

public class ClockPane extends JDesktopPane implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JButton switchButton;

	public ClockPane(SwitchButtonListener switchButtonListener)
			throws HeadlessException {

		switchButton = new JButton("switch to settings");
		switchButton.setBounds(20, 260, 150, 30);
		switchButton.addActionListener(switchButtonListener);
		switchButton.setName(SettingsPane.class.getName());

		this.setBackground(null);
		this.add(switchButton);

		AnalogClock clock = new AnalogClock();
		clock.setName("clock");
		clock.setPreferredSize(new java.awt.Dimension(240, 240));
		clock.setBounds(0, 0, 240, 240);
		clock.setType(AnalogClock.TYPE.DARK);
		clock.setSecondPointerVisible(true);
		clock.setAutoType(false);

		this.add(clock);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("action ClockPane " + e);
		if (e.getSource() == switchButton) {
			// ...
		}

	}

	@Override
	public Dimension getPreferredSize() {
		return (CWPMain.applicationSize);
	}

}