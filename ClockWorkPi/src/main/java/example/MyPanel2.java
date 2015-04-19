package example;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

class MyPanel2 extends JPanel {

	private JButton jcomp1;
	private JPanel contentPane;
	private Color backgroundColour;
	private JComboBox choiceBox;

	public MyPanel2(JPanel panel, Color c, CardLayoutExample cle) {
		contentPane = panel;
		backgroundColour = c;
		choiceBox = cle.getChoiceBox();

		setOpaque(true);
		setBackground(backgroundColour);

		// construct components
		jcomp1 = new JButton("Show New Panel");
		jcomp1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String changeToPanel = (String) choiceBox.getSelectedItem();
				CardLayout cardLayout = (CardLayout) contentPane.getLayout();
				cardLayout.show(contentPane, changeToPanel);
			}
		});

		add(jcomp1);
	}

	@Override
	public Dimension getPreferredSize() {
		return (new Dimension(500, 500));
	}
}