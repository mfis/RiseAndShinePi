package mfi.riseandshinepi.hardware;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer.Info;

import mfi.riseandshinepi.logic.ApplicationProperties;
import mfi.riseandshinepi.logic.Processor;

public class AudioPlayer {

	private final static int MIXER_INDEX = 0;
	private static ArrayList<String> FILE_SUFFIXES = new ArrayList<String>();
	static {
		FILE_SUFFIXES.add("mp3");
		FILE_SUFFIXES.add("ogg");
	}

	private GPIOController speakerPowerSwitch;

	private List<File> files;
	private Info mixerInfo;

	private AudioStreamingThread streamingThread;

	private Processor processor;

	public AudioPlayer(Processor processor) {
		this.processor = processor;
		files = new LinkedList<File>();
		mixerInfo = AudioSystem.getMixerInfo()[MIXER_INDEX];
		if (!this.processor.isDevelopmentMode()) {
			speakerPowerSwitch = new GPIOController(
					ApplicationProperties.SPEAKER_POWER_GPIO_PIN_NUMBER
							.valueAsInt(),
					false);
			speakerPowerSwitch.setIO(false, 0);
		}
	}

	public static void main(String[] args) throws InterruptedException {
		AudioPlayer ap = new AudioPlayer(new Processor(true));
		ap.start();
		Thread.sleep(5000);
		ap.stop();
	}

	public void start() {

		if (!processor.isDevelopmentMode()) {
			speakerPowerSwitch.setIO(true,
					ApplicationProperties.SPEAKER_POWER_ON_DELAY_MILLIES
							.valueAsInt());
		}

		String home = System.getProperty("user.home");
		File dir = new File(home
				+ ApplicationProperties.MUSIC_DIR_1_RELATIVE_TO_USER_HOME);
		if (!dir.exists()) {
			dir = new File(home
					+ ApplicationProperties.MUSIC_DIR_2_RELATIVE_TO_USER_HOME);
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

		if (!processor.isDevelopmentMode()) {
			speakerPowerSwitch.setIO(false, 0);
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
