package mfi.clockworkpi.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import mfi.clockworkpi.logic.Processor;

public class SwitchButtonListener implements ActionListener {

	private Processor processor;

	public SwitchButtonListener(Processor processor) {
		this.processor = processor;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		processor.switchGuiTo(((JButton) e.getSource()).getName());
	}

}
