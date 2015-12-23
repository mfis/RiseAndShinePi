package mfi.riseandshinepi.gui.components;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
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
import org.apache.commons.lang3.StringUtils;
import mfi.riseandshinepi.gui.cardpanes.AbstractPane;
import mfi.riseandshinepi.gui.cardpanes.AlarmPane;
import mfi.riseandshinepi.gui.cardpanes.AlarmSettingsPane;
import mfi.riseandshinepi.gui.cardpanes.BlankPane;
import mfi.riseandshinepi.gui.cardpanes.ClockPane;
import mfi.riseandshinepi.gui.cardpanes.DisplayAutoOffSettingsPane;
import mfi.riseandshinepi.gui.cardpanes.SettingsSummaryPane;
import mfi.riseandshinepi.gui.cardpanes.VolumeAndBacklightSettingsPane;
import mfi.riseandshinepi.listeners.SwitchButtonListener;
import mfi.riseandshinepi.logic.Processor;

public class Gui extends JFrame {

	private static final long serialVersionUID = 1L;

	public final static Dimension applicationSize = new Dimension(240, 320);

	private GraphicsDevice device;
	private ClockPane clockPane;
	private AlarmPane alarmPane;
	private AlarmSettingsPane alarmSettingsPane;
	private DisplayAutoOffSettingsPane displayAutoOffSettingsPane;
	private BlankPane blankPane;
	private SettingsSummaryPane settingsSummaryPane;
	private VolumeAndBacklightSettingsPane volumeAndBacklightSettingsPane;
	private JPanel contentPane;
	private SwitchButtonListener switchButtonListener;
	private DevelopmentPanel developmentPanel;
	private Processor processor;

	private String actualPane;
	private boolean fullscreen;

	private String alarmTimeString;

	public Gui(Processor processor) {
		this.processor = processor;
	}

	public void paintGui() {

		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		JFrame frame = new JFrame("RiseAndShinePi");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setBackground(Color.BLACK);

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		device = ge.getDefaultScreenDevice();

		if (device.isFullScreenSupported() && !processor.isDevelopmentMode()) {
			frame.setUndecorated(true);
			device.setFullScreenWindow(frame);
			int appPixels = new Double(applicationSize.getHeight()).intValue() * new Double(applicationSize.getWidth()).intValue();
			int displayPixels = device.getDisplayMode().getWidth() * device.getDisplayMode().getHeight();
			if (appPixels == displayPixels) {
				Toolkit toolkit = Toolkit.getDefaultToolkit();
				Point hotSpot = new Point(0, 0);
				BufferedImage cursorImage = new BufferedImage(1, 1, BufferedImage.TRANSLUCENT);
				Cursor invisibleCursor = toolkit.createCustomCursor(cursorImage, hotSpot, "InvisibleCursor");
				frame.setCursor(invisibleCursor);
			}
		}

		contentPane = new JPanel();
		contentPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		contentPane.setLayout(new CardLayout());

		switchButtonListener = new SwitchButtonListener(processor);

		clockPane = new ClockPane(processor);
		alarmPane = new AlarmPane(processor);
		alarmSettingsPane = new AlarmSettingsPane(processor);
		displayAutoOffSettingsPane = new DisplayAutoOffSettingsPane(processor);
		blankPane = new BlankPane(processor);
		settingsSummaryPane = new SettingsSummaryPane(processor);
		volumeAndBacklightSettingsPane = new VolumeAndBacklightSettingsPane(processor);

		contentPane.add(clockPane, clockPane.getClass().getName());
		contentPane.add(alarmPane, alarmPane.getClass().getName());
		contentPane.add(alarmSettingsPane, alarmSettingsPane.getClass().getName());
		contentPane.add(displayAutoOffSettingsPane, displayAutoOffSettingsPane.getClass().getName());
		contentPane.add(blankPane, blankPane.getClass().getName());
		contentPane.add(settingsSummaryPane, settingsSummaryPane.getClass().getName());
		contentPane.add(volumeAndBacklightSettingsPane, volumeAndBacklightSettingsPane.getClass().getName());

		if (processor.isDevelopmentMode()) {
			developmentPanel = new DevelopmentPanel(contentPane, applicationSize);
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

		actualPane = ClockPane.class.getName();
	}

	public void switchGuiTo(String name) {
		for (Component c : contentPane.getComponents()) {
			if (c != null && c.getClass().getName().equals(name) && c instanceof AbstractPane) {
				((AbstractPane) c).refresh();
				break;
			}
		}
		((CardLayout) contentPane.getLayout()).show(contentPane, name);
		actualPane = name;
	}

	public AbstractPane getActualPane() {
		if (StringUtils.isBlank(actualPane)) {
			return null;
		}
		for (Component c : contentPane.getComponents()) {
			if (c != null && c.getClass().getName().equals(actualPane) && c instanceof AbstractPane) {
				return ((AbstractPane) c);
			}
		}
		return null;
	}

	public void refreshGuiValues() {
		AbstractPane pane = getActualPane();
		if (pane != null) {
			pane.refresh();
		}
	}

	public boolean isActualPaneShowingWeatherInformation() {
		AbstractPane pane = getActualPane();
		if (pane != null) {
			return pane.showsWeatherInformation();
		} else {
			return false;
		}
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

	public String getActualPaneName() {
		return actualPane;
	}

	public String getAlarmTimeString() {
		return alarmTimeString;
	}

	public void setAlarmTimeString(String alarmTimeString) {
		this.alarmTimeString = alarmTimeString;
	}

}
