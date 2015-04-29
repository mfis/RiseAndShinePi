package mfi.clockworkpi.main;

import javax.swing.SwingUtilities;

import mfi.clockworkpi.logic.Processor;

public class CWPMain {

	public final static String DEVELOPMENT_MODE = "developmentMode";

	public static void main(String[] args) {

		final boolean developmentMode = args != null && args.length > 0
				&& args[0] != null && args[0].equals(DEVELOPMENT_MODE);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Processor(developmentMode);
			}
		});
	}

}