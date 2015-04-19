package mfi.clockworkpi.main;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import mfi.clockworkpi.gui.cardpanes.ClockPane;
import mfi.clockworkpi.gui.cardpanes.SettingsPane;
import mfi.clockworkpi.listeners.SwitchButtonListener;

public class CWPMain {

	public final static String WINDOW_MODE = "windowMode";
	public final static Dimension applicationSize = new Dimension(240, 320);

	private GraphicsDevice device;
	private ClockPane clockPane;
	private SettingsPane settingsPane;
	SwitchButtonListener switchButtonListener;
	private boolean fullScreen = true;

	private void displayGUI() {

		try {
			UIManager.setLookAndFeel(UIManager
					.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		JFrame frame = new JFrame("ClockWorkPi");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(applicationSize);
		frame.setPreferredSize(applicationSize);
		frame.setLocationRelativeTo(null);
		frame.setBackground(Color.BLACK);

		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		device = ge.getDefaultScreenDevice();

		if (device.isFullScreenSupported() && fullScreen) {
			frame.setUndecorated(true);
			device.setFullScreenWindow(frame);
		}

		JPanel contentPane = new JPanel();
		contentPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		contentPane.setLayout(new CardLayout());

		switchButtonListener = new SwitchButtonListener(contentPane);

		clockPane = new ClockPane(switchButtonListener);
		settingsPane = new SettingsPane(switchButtonListener);

		contentPane.add(clockPane, clockPane.getClass().getName());
		contentPane.add(settingsPane, settingsPane.getClass().getName());

		frame.getContentPane().add(contentPane, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				CWPMain cwpMain = new CWPMain();
				boolean windowMode = args != null && args.length > 0
						&& args[0]!=null &&  args[0].equals(WINDOW_MODE);
				cwpMain.setFullScreen(!windowMode);
				cwpMain.displayGUI();
			}
		});
	}

	private void setFullScreen(boolean fullScreen) {
		this.fullScreen = fullScreen;
	}

}