package mfi.riseandshinepi.hardware;

import java.io.File;
import java.util.List;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Mixer.Info;
import javax.sound.sampled.SourceDataLine;

public class AudioStreamingThread extends Thread {

	private Info mixerInfo;
	private List<File> files;
	private int startFileIndex;
	private int actualFileIndex;

	private AudioInputStream encodedInput;
	private AudioInputStream decodedInput;
	private AudioFormat decodedFormat;
	private SourceDataLine sourceDataLine;

	private FloatControl volumeControl;
	private int actualVolumePercent;
	private Integer targetVolume;

	private boolean continuePlaying;
	private boolean runningStream;

	public AudioStreamingThread(Info mixerInfo, List<File> files, Integer targetVolume, int fileIndex) {
		super();
		this.mixerInfo = mixerInfo;
		this.files = files;
		this.continuePlaying = true;
		this.runningStream = false;
		this.targetVolume = targetVolume;
		this.startFileIndex = fileIndex;
	}

	@Override
	public void run() {

		super.run();
		if (startFileIndex >= files.size()) {
			startFileIndex = 0;
		}
		while (continuePlaying) {
			for (int i = startFileIndex; i < files.size(); i++) {
				if (continuePlaying) {
					actualFileIndex = i;
					stream(files.get(i));
				}
			}
			if (continuePlaying) {
				startFileIndex = 0;
			}
		}
		stopStreaming();
	}

	private void stream(File file) {

		try {
			encodedInput = AudioSystem.getAudioInputStream(file);
			decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, encodedInput.getFormat().getSampleRate(),
					16, encodedInput.getFormat().getChannels(), encodedInput.getFormat().getChannels() * 2,
					encodedInput.getFormat().getSampleRate(), false);
			decodedInput = AudioSystem.getAudioInputStream(decodedFormat, encodedInput);

			sourceDataLine = AudioSystem.getSourceDataLine(decodedFormat, mixerInfo);
			sourceDataLine.open(decodedFormat);

			volumeControl = (FloatControl) sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);

			setVolume(0);
			sourceDataLine.start();

			int bytesRead = 0;
			byte[] data = new byte[4096];
			boolean firstLoop = true;
			runningStream = true;

			while (bytesRead != -1 && continuePlaying) {
				bytesRead = decodedInput.read(data, 0, data.length);
				if (bytesRead != -1) {
					sourceDataLine.write(data, 0, bytesRead);
				}
				if (firstLoop) {
					setVolume(targetVolume);
					firstLoop = false;
				}
			}

			if (continuePlaying) {
				sourceDataLine.drain();
			}

		} catch (Exception e) {
			throw new RuntimeException("Error playing audio stream:", e);
		} finally {
			try {
				sourceDataLine.stop();
				sourceDataLine.close();
			} catch (Exception e) {/**/
			}
			try {
				decodedInput.close();
				encodedInput.close();
			} catch (Exception e) {/**/
			}
			runningStream = false;
		}
	}

	public int stopStreaming() {
		continuePlaying = false;
		return ++actualFileIndex;
	}

	public void setVolume(int percent) {

		float min = volumeControl.getMinimum();
		float max = volumeControl.getMaximum();
		float range = max - min;

		float fraction = percent / 100.0f;
		float value = min + (range * fraction);

		// clearing round-off errors
		if (value < min) {
			value = min;
		}
		if (value > max) {
			value = max;
		}

		// System.out.println("VOLUME NEW=" + percent + " / " + value);
		volumeControl.setValue(value);
		actualVolumePercent = percent;
	}

	public boolean isRunningStream() {
		return runningStream;
	}

	public int getActualVolumePercent() {
		return actualVolumePercent;
	}
}
