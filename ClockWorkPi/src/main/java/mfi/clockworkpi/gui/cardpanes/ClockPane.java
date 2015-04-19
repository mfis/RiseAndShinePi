package mfi.clockworkpi.gui.cardpanes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JPanel;

import mfi.clockworkpi.gui.components.AnalogClock;
import mfi.clockworkpi.listeners.SwitchButtonListener;
import mfi.clockworkpi.main.CWPMain;

public class ClockPane extends JDesktopPane implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JButton switchButton;

	public ClockPane(SwitchButtonListener switchButtonListener)
			throws HeadlessException {

		switchButton = new JButton("");
		switchButton.setBounds(0, 240, 240, 80);
		switchButton.addActionListener(switchButtonListener);
		switchButton.setName(SettingsPane.class.getName());
		switchButton.setBorderPainted(false);
		switchButton.setBackground(Color.BLACK);
		switchButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		
		JPanel p = new JPanel();
		p.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		p.setSize(240, 80);
		p.setPreferredSize(new Dimension(240, 80));
		p.setForeground(Color.DARK_GRAY);
		p.setBackground(Color.BLACK);
		
		JLabel l1 = new JLabel("So, 19. Apr 2015");
		l1.setBounds(0, 0, 240, 80);
		l1.setForeground(Color.LIGHT_GRAY);
		Font font = new Font("Arial", Font.PLAIN, 16);
		l1.setFont(font);
		l1.setBounds(0, 0, 150, 20);
		p.add(l1);
		JLabel l2 = new JLabel("Wecker: Mo, 5:12 Uhr");
//		l1.setFont(...);
		l2.setBounds(0, 30, 150, 20);
		l2.setForeground(Color.LIGHT_GRAY);
		l2.setFont(font);
		p.add(l2);
		switchButton.add(p);

		this.setBackground(Color.BLACK);
		this.add(switchButton);

		AnalogClock clock = new AnalogClock();
		clock.setName("clock");
		clock.setPreferredSize(new java.awt.Dimension(240, 240));
		clock.setBounds(0, 0, 240, 240);
		clock.setType(AnalogClock.TYPE.DARK);
		clock.setSecondPointerVisible(false);
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