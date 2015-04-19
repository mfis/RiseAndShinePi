package mfi.clockworkpi.listeners;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class SwitchButtonListener implements ActionListener {

	private JPanel contentPane;

	public SwitchButtonListener(JPanel panel) {
		contentPane = panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		CardLayout cardLayout = (CardLayout) contentPane.getLayout();
		cardLayout.show(contentPane, ((JButton) e.getSource()).getName());

		// exit:
		// device.setFullScreenWindow(null);
		// System.exit(0);
	}

}
