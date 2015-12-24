package mfi.riseandshinepi.gui.components;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JDesktopPane;
import javax.swing.JPanel;

public class DevelopmentPanel extends JDesktopPane {

	private static final long serialVersionUID = 1L;
	private JDesktopPane backlightPane;
	private Circle bulb;
	private Circle bulbBackground;
	Dimension developmentWindowSize = new Dimension(280, 500);

	public DevelopmentPanel(JPanel contentPane, Dimension applicationSize) {

		Dimension backlightSize = new Dimension(280, 360);
		contentPane.setBounds(20, 20, new Double(applicationSize.getWidth()).intValue(), new Double(applicationSize.getHeight()).intValue());

		backlightPane = new JDesktopPane();
		backlightPane.setSize(backlightSize);
		backlightPane.setPreferredSize(backlightSize);
		backlightPane.setBackground(Color.RED);
		backlightPane.add(contentPane);
		backlightPane.setBounds(0, 120, 280, 360);

		bulbBackground = new Circle(Color.BLACK);
		bulbBackground.setSize(new Dimension(100, 100));
		bulbBackground.setPreferredSize(new Dimension(100, 100));
		bulbBackground.setBackground(Color.LIGHT_GRAY);
		bulbBackground.setBounds(90, 10, 100, 100);

		bulb = new Circle(Color.YELLOW);
		bulb.setSize(new Dimension(100, 100));
		bulb.setPreferredSize(new Dimension(100, 100));
		bulb.setBackground(Color.LIGHT_GRAY);
		bulb.setBounds(90, 10, 100, 100);

		this.setBackground(Color.LIGHT_GRAY);
		this.add(bulbBackground);
		this.add(bulb);
		this.add(backlightPane);
	}

	public JDesktopPane getBacklightPane() {
		return backlightPane;
	}

	public Circle getBulb() {
		return bulb;
	}

	public Circle getBulbBackground() {
		return bulbBackground;
	}

	public Dimension getDevelopmentWindowSize() {
		return developmentWindowSize;
	}

}
