package mfi.riseandshinepi.gui.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.JButton;

public class TouchLabel extends JButton {

	private static final long serialVersionUID = 1L;
	private Font font = new Font("Arial", Font.BOLD, 16);

	public TouchLabel() {
		super();
		init();
	}

	public TouchLabel(String text) {
		super(text);
		init();
	}

	private void init() {
		this.setBorderPainted(false);
		this.setBackground(Color.BLACK);
		this.setForeground(Color.WHITE);
		this.setFont(font);
		this.setFocusable(false);
		this.setMargin(new Insets(0, 0, 0, 0));
		this.setBorder(null);
	}
}
