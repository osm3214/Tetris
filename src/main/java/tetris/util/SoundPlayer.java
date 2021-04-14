package tetris.util;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

public class SoundPlayer {

	public static Clip createClip(File path) {
		try (AudioInputStream ais = AudioSystem.getAudioInputStream(path)){

			AudioFormat af = ais.getFormat();

			DataLine.Info dataLine = new DataLine.Info(Clip.class, af);

			Clip c = (Clip)AudioSystem.getLine(dataLine);

			c.open(ais);

			return c;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
