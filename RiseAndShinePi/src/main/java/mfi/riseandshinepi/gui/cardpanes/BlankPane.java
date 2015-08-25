package mfi.riseandshinepi.gui.cardpanes;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Timer;

import mfi.riseandshinepi.gui.components.Gui;
import mfi.riseandshinepi.gui.components.TouchButton;
import mfi.riseandshinepi.logic.Processor;

public class BlankPane extends AbstractPane implements ActionListener {

	private static final long serialVersionUID = 1L;
	private TouchButton button;
	private final Timer timer = new Timer(10000, this);
	private Color color = Color.BLACK;

	public BlankPane(Processor processor) {

		button = new TouchButton("");
		button.setBounds(0, 0, Gui.applicationSize.width, Gui.applicationSize.height);
		button.addActionListener(processor.getGui().getSwitchButtonListener());
		button.setName(ClockPane.class.getName());
		button.setBackground(color);
		button.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		this.setBackground(color);
		this.setForeground(color);
		this.setFocusable(false);
		this.add(button);

		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (color.equals(Color.BLACK)) {
			color = Color.WHITE;
		} else {
			color = Color.BLACK;
		}
		if (button != null) {
			button.setBackground(color);
		}

		this.setBackground(Color.BLACK);
		this.setForeground(Color.BLACK);
	}

	@Override
	public void refresh() {
		// noop
	}
	
	@Override
	public boolean showsWeatherInformation() {
		return false;
	}

}