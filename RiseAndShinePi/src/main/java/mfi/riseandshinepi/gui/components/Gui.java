package mfi.riseandshinepi.gui.components;

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

import mfi.riseandshinepi.gui.cardpanes.BlankPane;
import mfi.riseandshinepi.gui.cardpanes.ClockPane;
import mfi.riseandshinepi.gui.cardpanes.SettingsPane;
import mfi.riseandshinepi.listeners.SwitchButtonListener;
import mfi.riseandshinepi.logic.Processor;

public class Gui extends JFrame {

	private static final long serialVersionUID = 1L;

	public final static Dimension applicationSize = new Dimension(240, 320);

	private GraphicsDevice device;
	private ClockPane clockPane;
	private SettingsPane settingsPane;
	private BlankPane blankPane;
	private JPanel contentPane;
	private SwitchButtonListener switchButtonListener;
	private DevelopmentPanel developmentPanel;
	private Processor processor;

	private boolean fullscreen;

	public Gui(Processor processor) {
		this.processor = processor;
	}

	public void paintGui() {

		try {
			UIManager.setLookAndFeel(UIManager
					.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		JFrame frame = new JFrame("RiseAndShinePi");
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

		contentPane = new JPanel();
		contentPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		contentPane.setLayout(new CardLayout());

		switchButtonListener = new SwitchButtonListener(processor);

		clockPane = new ClockPane(processor);
		settingsPane = new SettingsPane(processor);
		blankPane = new BlankPane(processor);

		contentPane.add(clockPane, clockPane.getClass().getName());
		contentPane.add(settingsPane, settingsPane.getClass().getName());
		contentPane.add(blankPane, blankPane.getClass().getName());

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

	public void switchGuiTo(String name) {
		if (name.equals(clockPane.getClass().getName())) {
			clockPane.refreshLabels();
		} else if (name.equals(settingsPane.getClass().getName())) {
			settingsPane.refreshButtons();
		}
		((CardLayout) contentPane.getLayout()).show(contentPane, name);
	}

	public DevelopmentPanel getDevelopmentPanel() {
		return developmentPanel;
	}

	public SwitchButtonListener getSwitchButtonListener() {
		return switchButtonListener;
	}

	@Override
	public JPanel getContentPane() {
		return contentPane;
	}

	public CardLayout getCardLayout() {
		return (CardLayout) contentPane.getLayout();
	}

	public boolean isFullscreen() {
		return fullscreen;
	}

	public GraphicsDevice getDevice() {
		return device;
	}

}
