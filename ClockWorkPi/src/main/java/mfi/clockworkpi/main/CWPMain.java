package mfi.clockworkpi.main;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

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
		;

		int appPixels = new Double(applicationSize.getHeight()).intValue()
				* new Double(applicationSize.getWidth()).intValue();
		int displayPixels = device.getDisplayMode().getWidth() * device.getDisplayMode().getHeight();

		if (device.isFullScreenSupported() && fullScreen) {
			frame.setUndecorated(true);
			device.setFullScreenWindow(frame);
			if(appPixels == displayPixels) {
				Toolkit toolkit = Toolkit.getDefaultToolkit();
				Point hotSpot = new Point(0, 0);
				BufferedImage cursorImage = new BufferedImage(1, 1,
						BufferedImage.TRANSLUCENT);
				Cursor invisibleCursor = toolkit.createCustomCursor(cursorImage,
						hotSpot, "InvisibleCursor");
				frame.setCursor(invisibleCursor);
			}
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

		final boolean windowMode = args != null && args.length > 0
				&& args[0] != null && args[0].equals(WINDOW_MODE);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				CWPMain cwpMain = new CWPMain();
				cwpMain.setFullScreen(!windowMode);
				cwpMain.displayGUI();
			}
		});
	}

	private void setFullScreen(boolean fullScreen) {
		this.fullScreen = fullScreen;
	}

}