package mfi.clockworkpi.gui.components;

import java.awt.BorderLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.UIManager;

import mfi.clockworkpi.gui.components.AnalogClock;

public class ClockPane extends JDesktopPane implements ActionListener {

	private JButton switchButton;

	public ClockPane() throws HeadlessException {

		switchButton = new JButton("switch to settings");
		switchButton.setBounds(20, 120, 150, 30);
		switchButton.addActionListener(this);

		this.setBackground(null);
		this.add(switchButton);

		AnalogClock clock = new AnalogClock();
		clock.setName("clock");
		clock.setPreferredSize(new java.awt.Dimension(300, 300));
		clock.setBounds(200, 100, 300, 300);
		clock.setType(AnalogClock.TYPE.DARK);
		clock.setSecondPointerVisible(false);
		clock.setAutoType(false);
		
		this.add(clock);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("action ClockPane " + e);
		if (e.getSource() == switchButton) {
		}

	}

}