package mfi.clockworkpi.gui.cardpanes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JPanel;

import mfi.clockworkpi.gui.components.AnalogClock;
import mfi.clockworkpi.gui.components.Gui;
import mfi.clockworkpi.gui.components.TouchButton;
import mfi.clockworkpi.listeners.AnalogClockMouseListener;
import mfi.clockworkpi.logic.Processor;

public class ClockPane extends JDesktopPane {

	private static final long serialVersionUID = 1L;
	private TouchButton clockButton;
	private TouchButton switchButton;

	public ClockPane(Processor processor) throws HeadlessException {

		AnalogClock clock = new AnalogClock();
		clock.setName("clock");
		clock.setPreferredSize(new java.awt.Dimension(240, 240));
		clock.setBounds(0, 0, 240, 240);
		clock.setType(AnalogClock.TYPE.DARK);
		clock.setSecondPointerVisible(false);
		clock.setAutoType(false);

		JPanel clockPanel = new JPanel();
		clockPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		clockPanel.setSize(240, 240);
		clockPanel.setPreferredSize(new Dimension(240, 240));
		clockPanel.setBackground(Color.BLACK);
		clockPanel.add(clock);

		clockButton = new TouchButton("");
		clockButton.setBounds(0, 0, 240, 240);
		clockButton.addMouseListener(new AnalogClockMouseListener(processor));
		clockButton.setName(SettingsPane.class.getName());
		clockButton.setBackground(Color.BLACK);
		clockButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		clockButton.add(clockPanel);

		switchButton = new TouchButton("");
		switchButton.setBounds(0, 240, 240, 80);
		switchButton.addActionListener(processor.getGui().getSwitchButtonListener());
		switchButton.setName(SettingsPane.class.getName());
		switchButton.setBackground(Color.BLACK);
		switchButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		JPanel p = new JPanel();
		p.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		p.setSize(240, 80);
		p.setPreferredSize(new Dimension(240, 80));
		p.setForeground(Color.DARK_GRAY);
		p.setBackground(Color.BLACK);

		JLabel l1 = new JLabel("Sam, 25. Apr 2015");
		l1.setBounds(0, 0, 240, 80);
		l1.setForeground(Color.LIGHT_GRAY);
		Font font = new Font("Arial", Font.BOLD, 16);
		l1.setFont(font);
		l1.setBounds(0, 0, 150, 20);
		p.add(l1);
		JLabel l2 = new JLabel("Wecker: Mon, 5:12 Uhr");
		l2.setBounds(0, 30, 150, 20);
		l2.setForeground(Color.LIGHT_GRAY);
		l2.setFont(font);
		p.add(l2);
		switchButton.add(p);

		this.setBackground(Color.BLACK);
		this.add(clockButton);
		this.add(switchButton);

	}

	@Override
	public Dimension getPreferredSize() {
		return (Gui.applicationSize);
	}

}