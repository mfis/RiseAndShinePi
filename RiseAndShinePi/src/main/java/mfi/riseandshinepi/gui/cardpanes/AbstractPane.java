package mfi.riseandshinepi.gui.cardpanes;

import java.awt.Dimension;
import javax.swing.JDesktopPane;
import mfi.riseandshinepi.gui.components.Gui;

public abstract class AbstractPane extends JDesktopPane {

	private static final long serialVersionUID = 1L;

	public abstract void refresh();

	public abstract boolean showsWeatherInformation();

	public AbstractPane() {
		super();
		setVisible(false);
	}

	@Override
	public Dimension getPreferredSize() {
		return (Gui.applicationSize);
	}

}