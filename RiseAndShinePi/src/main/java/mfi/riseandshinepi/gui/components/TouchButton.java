package mfi.riseandshinepi.gui.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.JButton;

public class TouchButton extends JButton {

	private static final long serialVersionUID = 1L;
	private Font fontBold = new Font("Arial", Font.BOLD, 16);
	private Font fontInactive = new Font("Arial", Font.ITALIC, 16);

	public TouchButton() {
		super();
		init();
	}

	public TouchButton(String text) {
		super(text);
		init();
	}

	private void init() {
		this.setBorderPainted(false);
		this.setBackground(Color.DARK_GRAY);
		this.setForeground(Color.WHITE);
		this.setFont(fontBold);
		this.setFocusable(false);
		this.setMargin(new Insets(0, 0, 0, 0));
		this.setBorder(null);
	}

	public void setInactiveLook() {
		this.setFont(fontInactive);
		this.setBackground(Color.GRAY);
		this.setForeground(Color.BLACK);
	}

	public void setActiveLook() {
		this.setFont(fontBold);
		this.setBackground(Color.DARK_GRAY);
		this.setForeground(Color.WHITE);
	}
}
