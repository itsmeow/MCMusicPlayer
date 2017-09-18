package its_meow.mcmusicplayer;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import its_meow.mcmusicplayer.proxy.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import paulscode.sound.IStreamListener;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;

public class MusicManager {

	File songFolder;
	File songFolderOgg;
	File[] oggs;
	int trackNum = -1;
	//Music music;
	
	public void init() {
		songFolder = new File(CommonProxy.configDirectory, "/mcmusicplayer/songs/");
		if(!songFolder.exists()) {
			songFolder.mkdirs();
		}
		songFolderOgg = new File(CommonProxy.configDirectory, "/mcmusicplayer/songs/oggs");
		if(!songFolderOgg.exists()) {
			songFolderOgg.mkdirs();
		}
		File[] mp3s = songsInFolder();
		List<File> oggsList = new ArrayList<File>();
		for(File mp3 : mp3s) {
			if(mp3 != null) {
				File ogg = new File(songFolderOgg.getAbsolutePath() + mp3.getName().substring(0, mp3.getName().indexOf(".mp3")) + ".ogg");
				mp3.renameTo(ogg);
				oggsList.add(ogg);
			}
		}
		oggs = (File[]) oggsList.toArray();
	}
	
	public File[] songsInFolder() {
		int mp3count = 0;
		File[] files = songFolder.listFiles(new FileFilterMp3());
		return files;
	}
	
	public void nextSong() {
		if(trackNum == -1) {
			pauseSong(); // Close old song
			playSong();
		} else {
			File[] songs = songsInFolder();
			if(trackNum != songs.length) {
				trackNum++;
				pauseSong(); // Close old song
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
				pauseSong(); // Close old song
				playSong();
			} else {
				File[] songs = songsInFolder();
				trackNum = songs.length;
				pauseSong(); // Close old song
				playSong();
			}
		}
	}
	
	public void pauseSong() {
		if(trackNum == -1) {
			playSong();
		} else {
			pauseSong();
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
		pauseSong();
		// Play Song
			
	}
	
	
	
	
	

	
	
	
}
