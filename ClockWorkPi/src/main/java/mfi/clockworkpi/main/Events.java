package mfi.clockworkpi.main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Events {

	public void gui() {

		final JFrame f = new JFrame();
		JButton button = new JButton("click!");
		f.add(button);

		button.addMouseListener(new MouseAdapter() {
			private java.util.Timer t;

			private boolean eventFired;

			@Override
			public void mousePressed(MouseEvent e) {
				eventFired = false;
				if (t == null) {
					t = new java.util.Timer();
				}
				t.schedule(new TimerTask() {
					@Override
					public void run() {
						eventFired = true;
						System.out.println("LONG click");
					}
				}, 2000);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (t != null) {
					t.cancel();
					t.purge();
					t = null;
					if (!eventFired) {
						eventFired = true;
						System.out.println("SHORT click");
					}
				}
			}
		});

		f.pack();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				f.setVisible(true);
			}
		});
	}

	public static void main(String[] args) {

		new Events().gui();
	}
}
