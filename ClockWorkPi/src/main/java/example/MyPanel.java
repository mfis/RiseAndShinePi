package example;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

class MyPanel extends JPanel {
	private JButton jcomp4;
	private JPanel contentPane;
	private JComboBox choiceBox;

	public MyPanel(JPanel panel, CardLayoutExample cle) {
		choiceBox = cle.getChoiceBox();
		contentPane = panel;
		setOpaque(true);
		setBackground(Color.RED.darker().darker());
		// construct components
		jcomp4 = new JButton("Show New Panel");
		jcomp4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String changeToPanel = (String) choiceBox.getSelectedItem();
				CardLayout cardLayout = (CardLayout) contentPane.getLayout();
				cardLayout.show(contentPane, changeToPanel);
			}
		});
		add(jcomp4);
	}

	@Override
	public Dimension getPreferredSize() {
		return (new Dimension(500, 500));
	}
}