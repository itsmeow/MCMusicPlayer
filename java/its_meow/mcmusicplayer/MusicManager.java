package its_meow.mcmusicplayer;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.connormahaffey.JavaMusicExample.Music;

import its_meow.mcmusicplayer.proxy.CommonProxy;
import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;
import net.minecraft.client.Minecraft;

public class MusicManager {

	File songFolder;
	int trackNum = -1;
	Music music;
	
	public void init() throws LineUnavailableException {
		songFolder = new File(CommonProxy.configDirectory, "/mcmusicplayer/songs/");
		if(!songFolder.exists()) {
			songFolder.mkdirs();
		}
		music = new Music();
	}

	public File[] songsInFolder() {
		int mp3count = 0;
		File[] files = songFolder.listFiles();
		File[] mp3s = new File[300];
		if(files.length != 0) {
			System.out.println("Files found in songs folder: " + files.length);
			for(int i = 0; i < files.length; i++) {
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
			music.stop(); // Close old song
			playSong();
		} else {
			File[] songs = songsInFolder();
			if(trackNum != songs.length) {
				trackNum++;
				music.stop(); // Close old song
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
				music.stop(); // Close old song
				playSong();
			} else {
				File[] songs = songsInFolder();
				trackNum = songs.length;
				music.stop(); // Close old song
				playSong();
			}
		}
	}
	
	public void pauseSong() {
		if(trackNum == -1) {
			playSong();
		} else {
			music.stop();
		}
	}
	
	public void playSong() {
		File[] songs = songsInFolder();
		if(trackNum == -1) {
			if(songs.length > 0) {
				trackNum = 0;
			} else {
				return;
			}
		}
		File song = songs[trackNum];
		if(song == null) {
			for(int i = 0; i < 300; i++) {
				if(songs[i] != null) {
					trackNum = i;
				}
			}
		}
		System.out.println("Playing File: " + song.getAbsoluteFile());
		if(music.isPlaying()) {
			music.stop();
		}
		music.loadFile(song.getAbsolutePath());
		music.play();
			
	}
}
