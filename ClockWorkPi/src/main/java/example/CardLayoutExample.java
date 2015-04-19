package example;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class CardLayoutExample {
	private MyPanel panel1;
	private MyPanel2 panel2;
	private MyPanel2 panel3;
	private JComboBox choiceBox;
	private String[] choices = { "Panel 1", "Panel 2", "Panel 3" };

	private void displayGUI() {
		JFrame frame = new JFrame("Card Layout Example");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel contentPane = new JPanel();
		contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new CardLayout());

		choiceBox = new JComboBox(choices);

		panel1 = new MyPanel(contentPane, this);
		panel2 = new MyPanel2(contentPane, Color.GREEN.darker().darker(), this);
		panel3 = new MyPanel2(contentPane, Color.DARK_GRAY, this);

		contentPane.add(panel1, "Panel 1");
		contentPane.add(panel2, "Panel 2");
		contentPane.add(panel3, "Panel 3");

		frame.getContentPane().add(choiceBox, BorderLayout.PAGE_START);
		frame.getContentPane().add(contentPane, BorderLayout.CENTER);
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
	}

	public JComboBox getChoiceBox() {
		return choiceBox;
	}

	public static void main(String... args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new CardLayoutExample().displayGUI();
			}
		});
	}
}
