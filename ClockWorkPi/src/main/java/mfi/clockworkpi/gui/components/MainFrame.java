package mfi.clockworkpi.gui.components;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import mfi.clockworkpi.gui.cardpanes.ClockPane;
import mfi.clockworkpi.gui.cardpanes.SettingsPane;
import mfi.clockworkpi.listeners.SwitchButtonListener;
import mfi.clockworkpi.logic.Processor;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	public final static Dimension applicationSize = new Dimension(240, 320);

	private GraphicsDevice device;
	private ClockPane clockPane;
	private SettingsPane settingsPane;
	private SwitchButtonListener switchButtonListener;
	private DevelopmentPanel developmentPanel;
	private Processor processor;

	public MainFrame(Processor processor) {
		this.processor = processor;
		paintGui();
	}

	private void paintGui() {

		try {
			UIManager.setLookAndFeel(UIManager
					.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		JFrame frame = new JFrame("ClockWorkPi");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setBackground(Color.BLACK);

		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		device = ge.getDefaultScreenDevice();

		if (device.isFullScreenSupported() && !processor.isDevelopmentMode()) {
			frame.setUndecorated(true);
			device.setFullScreenWindow(frame);
			int appPixels = new Double(applicationSize.getHeight()).intValue()
					* new Double(applicationSize.getWidth()).intValue();
			int displayPixels = device.getDisplayMode().getWidth()
					* device.getDisplayMode().getHeight();
			if (appPixels == displayPixels) {
				Toolkit toolkit = Toolkit.getDefaultToolkit();
				Point hotSpot = new Point(0, 0);
				BufferedImage cursorImage = new BufferedImage(1, 1,
						BufferedImage.TRANSLUCENT);
				Cursor invisibleCursor = toolkit.createCustomCursor(
						cursorImage, hotSpot, "InvisibleCursor");
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

		if (processor.isDevelopmentMode()) {
			developmentPanel = new DevelopmentPanel(contentPane,
					applicationSize);
			frame.setSize(developmentPanel.getDevelopmentWindowSize());
			frame.setPreferredSize(developmentPanel.getDevelopmentWindowSize());
			frame.getContentPane().add(developmentPanel, BorderLayout.CENTER);
		} else {
			frame.setSize(applicationSize);
			frame.setPreferredSize(applicationSize);
			frame.getContentPane().add(contentPane, BorderLayout.CENTER);
		}

		frame.pack();
		frame.setVisible(true);
	}

	public DevelopmentPanel getDevelopmentPanel() {
		return developmentPanel;
	}

}