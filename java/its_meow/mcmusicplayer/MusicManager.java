package its_meow.mcmusicplayer;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import its_meow.mcmusicplayer.proxy.CommonProxy;
import net.minecraft.client.Minecraft;

public class MusicManager {

	File songFolder;
	int trackNum = -1;
	AudioInputStream ais;
	Clip clip;
	
	public void init() throws LineUnavailableException {
		songFolder = new File(CommonProxy.configDirectory, "/mcmusicplayer/songs/");
		clip = AudioSystem.getClip();
		if(!songFolder.exists()) {
			songFolder.mkdirs();
		}
	}

	public File[] songsInFolder() {
		int mp3count = 0;
		File[] files = songFolder.listFiles();
		File[] mp3s = {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};
		if(files.length != 0) {
			for(int i = 0; i <= files.length; i++) {
				File fileIn = files[i];
				if(fileIn.getAbsolutePath().endsWith(".mp3")) {
					mp3count++;
					mp3s[i] = fileIn;
				}
			}
		}
		return mp3s;
	}
	
	public void nextSong() {
		if(trackNum == -1) {
			clip.stop(); // Close old song
			playSong();
		} else {
			File[] songs = songsInFolder();
			if(trackNum != songs.length) {
				trackNum++;
				clip.stop(); // Close old song
				playSong();
			}
		}
	}
	
	public void lastSong() {
		if(trackNum == -1) {
			playSong();
		} else {
			if(trackNum != 0) {
				trackNum--;
				clip.stop(); // Close old song
				playSong();
			} else {
				File[] songs = songsInFolder();
				trackNum = songs.length;
				clip.stop(); // Close old song
				playSong();
			}
		}
	}
	
	public void pauseSong() {
		if(trackNum == -1) {
			playSong();
		} else {
			clip.stop();
		}
	}
	
	public void playSong() {
		File[] songs = songsInFolder();
		if(trackNum == -1) {
			if(songs.length > 0) {
				trackNum = 1;
			} else {
				return;
			}
		}
		
		File song = songs[trackNum];
		try {
			ais = AudioSystem.getAudioInputStream(song);
			clip.open(ais);
			clip.start();
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
		
	}
	
}
