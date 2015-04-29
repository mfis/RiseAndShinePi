package mfi.clockworkpi.gui.cardpanes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDesktopPane;

import mfi.clockworkpi.gui.components.MainFrame;
import mfi.clockworkpi.listeners.SwitchButtonListener;

public class SettingsPane extends JDesktopPane implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JButton switchButton;
	private JButton exitButton;

	public SettingsPane(SwitchButtonListener switchButtonListener)
			throws HeadlessException {

		switchButton = new JButton("switch to clock");
		switchButton.setBounds(20, 120, 150, 30);
		switchButton.addActionListener(switchButtonListener);
		switchButton.setName(ClockPane.class.getName());

		exitButton = new JButton("exit");
		exitButton.setBounds(20, 170, 150, 30);
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// FIXME
				System.exit(0);
			}
		});
		exitButton.setName(ClockPane.class.getName());

		this.setBackground(Color.BLACK);
		this.add(switchButton);
		this.add(exitButton);

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
		return (MainFrame.applicationSize);
	}

}