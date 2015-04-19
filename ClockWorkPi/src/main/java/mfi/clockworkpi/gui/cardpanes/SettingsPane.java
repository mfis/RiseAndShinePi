package mfi.clockworkpi.gui.cardpanes;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDesktopPane;

import mfi.clockworkpi.listeners.SwitchButtonListener;
import mfi.clockworkpi.main.CWPMain;

public class SettingsPane extends JDesktopPane implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JButton switchButton;

	public SettingsPane(SwitchButtonListener switchButtonListener)
			throws HeadlessException {

		switchButton = new JButton("switch to clock");
		switchButton.setBounds(20, 120, 150, 30);
		switchButton.addActionListener(switchButtonListener);
		switchButton.setName(ClockPane.class.getName());

		this.setBackground(null);
		this.add(switchButton);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("action SettingsPane " + e);
		if (e.getSource() == switchButton) {
			// ...
		}

	}

	@Override
	public Dimension getPreferredSize() {
		return (CWPMain.applicationSize);
	}

}