package mfi.riseandshinepi.gui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import javax.swing.JComponent;

public class Circle extends JComponent {

	private static final long serialVersionUID = 1L;
	private Color color;

	public Circle(Color color) {
		this.color = color;
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		Ellipse2D bulb = new Ellipse2D.Double(0, 0, 100, 100);
		g2.setColor(color);
		g2.fill(bulb);
		g2.dispose();
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
