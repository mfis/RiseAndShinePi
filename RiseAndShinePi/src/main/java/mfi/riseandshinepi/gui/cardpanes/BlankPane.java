package mfi.riseandshinepi.gui.cardpanes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;

import mfi.riseandshinepi.gui.components.Gui;
import mfi.riseandshinepi.gui.components.TouchButton;
import mfi.riseandshinepi.listeners.BlankPaneMouseListener;
import mfi.riseandshinepi.logic.Processor;

public class BlankPane extends JDesktopPane {

	private static final long serialVersionUID = 1L;
	private TouchButton button;

	public BlankPane(Processor processor) throws HeadlessException {

		button = new TouchButton("");
		button.setBounds(0, 0, Gui.applicationSize.width, Gui.applicationSize.height);
		button.addMouseListener(new BlankPaneMouseListener(processor));
		button.setName(ClockPane.class.getName());
		button.setBackground(Color.BLACK);
		button.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		this.setBackground(Color.BLACK);
		this.setForeground(Color.BLACK);
		this.setFocusable(false);
		this.add(button);
	}

	@Override
	public Dimension getPreferredSize() {
		return (Gui.applicationSize);
	}

}