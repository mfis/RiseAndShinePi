package mfi.clockworkpi.main;

import java.awt.BorderLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;

import mfi.clockworkpi.gui.components.ClockPane;

public class CWPMainFull extends JFrame implements ActionListener {

	private ClockPane clockPane = new ClockPane();
	private GraphicsDevice device;

	public CWPMainFull() throws HeadlessException {

		this.setLayout(new BorderLayout());
		this.setSize(500, 500);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		device = ge.getDefaultScreenDevice();

		if (device.isFullScreenSupported()) {
			setUndecorated(true);
			device.setFullScreenWindow(this);
		}

		
		
		this.add(clockPane);
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager
					.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		new CWPMainFull().setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		System.out.println("action CWPMainFull " + e);
	}
}