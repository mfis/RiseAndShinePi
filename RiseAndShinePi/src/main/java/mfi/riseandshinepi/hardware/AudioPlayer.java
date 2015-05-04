package mfi.riseandshinepi.hardware;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Mixer.Info;

public class AudioPlayer {

	private Clip clip;
	private List<URL> urls;
	private int listIndex = -1;
	private Timer timer;
	private Info mixerInfo;

	public AudioPlayer() {
		urls = new LinkedList<URL>();
	}

	public void start() {

		try {
			String home = System.getProperty("user.home");
			File dir = new File(home + "/Downloads/music");
			if (!dir.exists()) {
				dir = new File(home + "/music");
			}

			File[] files = dir.listFiles();
			for (File file : files) {
				URL url = file.toURI().toURL();
				urls.add(url);
			}
			Collections.shuffle(urls);
			listIndex = 0;

			Info[] mixerInfos = AudioSystem.getMixerInfo();
			int MIXER = 0;
			mixerInfo = mixerInfos[MIXER];

			play();
		} catch (Exception e) {
			e.printStackTrace(); // FIXME
		}

		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (!clip.isRunning()) {
					if (listIndex + 1 == urls.size()) {
						listIndex = 0;
					} else {
						listIndex++;
					}
					play();
				}
			}
		}, 2019, 2019);

	}

	private void play() {

		try {
			// System.out.println("playing: " + urls.get(listIndex).getFile());
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(urls.get(listIndex));
			clip = AudioSystem.getClip(mixerInfo);
			clip.open(audioInputStream);
			clip.start();
		} catch (Exception e) {
			e.printStackTrace(); // FIXME
		}
	}

	public void stop() {
		timer.cancel();
		timer.purge();
		if (clip.isRunning()) {
			try {
				clip.stop();
			} catch (Exception e) {
				e.printStackTrace(); // FIXME
			}
		}
	}
}
