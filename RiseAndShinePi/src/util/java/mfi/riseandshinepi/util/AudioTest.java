package mfi.riseandshinepi.util;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Mixer.Info;

public class AudioTest {

	public static void main(String[] args) throws Exception {

		Info[] mixerInfo = AudioSystem.getMixerInfo();
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < mixerInfo.length; i++) {
			System.out.println("- " + i + " -");
			sb.append("(" + i + ") " + mixerInfo[i].getName() + "\n    "
					+ mixerInfo[i].getDescription());
			try {
				audio(mixerInfo[i]);
			} catch (Exception e) {
				sb.append(e.toString());
			}
			sb.append("\n\n\n");
			Thread.sleep(5000);
		}
		if (new File("/home/pi/").exists()) {
			Files.write(Paths.get("/home/pi/audio.txt"), sb.toString()
					.getBytes());
		}

		// audio();
	}

	private static void audio(Info info) throws Exception {
		URL url = null;
		if (new File("/home/pi/").exists()) {
			url = new File("/home/pi/test.wav").toURI().toURL();
		} else {
			url = new File("/Users/mfi/Downloads/test.wav").toURI().toURL();
		}

		AudioInputStream audioInputStream = AudioSystem
				.getAudioInputStream(url);
		Clip clip = AudioSystem.getClip(info);
		clip.open(audioInputStream);
		clip.start();
	}

}
