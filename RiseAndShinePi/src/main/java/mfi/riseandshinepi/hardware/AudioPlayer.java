package mfi.riseandshinepi.hardware;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer.Info;

public class AudioPlayer {

	private final static int MIXER_INDEX = 0;
	private static ArrayList<String> FILE_SUFFIXES = new ArrayList<String>();
	static {
		FILE_SUFFIXES.add("mp3");
		FILE_SUFFIXES.add("ogg");
	}

	private List<File> files;
	private Info mixerInfo;

	private AudioStreamingThread streamingThread;

	public AudioPlayer() {
		files = new LinkedList<File>();
		mixerInfo = AudioSystem.getMixerInfo()[MIXER_INDEX];
	}

	public static void main(String[] args) throws InterruptedException {
		AudioPlayer ap = new AudioPlayer();
		ap.start();
		Thread.sleep(5000);
		ap.stop();
	}

	public void start() {

		String home = System.getProperty("user.home");
		File dir = new File(home + "/Downloads/music");
		if (!dir.exists()) {
			dir = new File(home + "/music");
		}

		files.clear();
		File[] filesx = dir.listFiles();
		for (File file : filesx) {
			for (String suffix : FILE_SUFFIXES) {
				if (file.getName().toLowerCase().endsWith(suffix)) {
					files.add(file);
					break;
				}
			}
		}
		Collections.shuffle(files);

		streamingThread = new AudioStreamingThread(mixerInfo, files);
		streamingThread.start();
	}

	public void stop() {
		if (streamingThread != null && streamingThread.isAlive()) {
			streamingThread.stopStreaming();
		}
	}

	public void setVolumePercent(int percent) {
		if (percent < 0) {
			percent = 0;
		}
		if (percent > 100) {
			percent = 100;
		}
		if (streamingThread != null && streamingThread.isAlive()) {
			streamingThread.setVolume(percent);
		}
	}

}
