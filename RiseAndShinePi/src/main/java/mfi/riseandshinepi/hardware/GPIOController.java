package mfi.riseandshinepi.hardware;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Based on WiringPi commands, so WiringPi must be installed
 */
public class GPIOController {

	private int pin; // standard RaspberryPi pin numbering

	private boolean isPWM; // if false, then i/o switch

	public GPIOController(int pin, boolean isPWM) {
		super();
		this.pin = pin;
		this.isPWM = isPWM;
		modeCommand();
	}

	public void setIO(boolean value, int delay) {

		if (isPWM) {
			throw new IllegalArgumentException("No I/O swittching for PWM ports");
		}

		set(value ? 1 : 0, delay);
	}

	public void setPWM(int value, int delay) {

		if (!isPWM) {
			throw new IllegalArgumentException("No PWM swittching for I/O ports");
		}

		set(value, delay);
	}

	private void set(int value, int delay) {

		if (delay == 0) {
			valueCommand(value);
			return;
		}

		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				valueCommand(value);
			}
		}, delay);
	}

	private void modeCommand() {
		if (isPWM) {
			executeCommand("gpio -g mode " + pin + " pwm");
		} else {
			executeCommand("gpio -g mode " + pin + " output");
		}
	}

	private void valueCommand(int value) {
		if (isPWM) {
			executeCommand("gpio -g pwm " + pin + " " + value);
		} else {
			executeCommand("gpio -g write " + pin + " " + value);
		}
	}

	private void executeCommand(String command) {

		try {
			Process p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";
			StringBuilder output = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				output.append(line);
			}
			if (!output.toString().trim().equals("")) {
				throw new IOException("Got output: ");
			}

		} catch (Exception e) {
			throw new IllegalStateException("Error executing command:", e);
		}
	}
}
