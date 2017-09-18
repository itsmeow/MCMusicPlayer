package its_meow.mcmusicplayer;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import its_meow.mcmusicplayer.proxy.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.util.ResourceLocation;
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
	File ogg;
	SoundSystem ss;

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
		oggs = new File[300];
		for(int i = 0; i < mp3s.length; i++) {
			File mp3 = mp3s[i];
			if(mp3 != null) {
				File ogg = new File(songFolderOgg.getAbsolutePath() + "/" + mp3.getName().substring(0, mp3.getName().indexOf(".mp3")) + ".ogg");
				try {
					FileUtils.copyFile(mp3, new File(mp3.getAbsolutePath().substring(0, mp3.getAbsolutePath().indexOf(".mp3") - 1) + "1.mp3"));
					mp3.renameTo(ogg);
					oggs[i] = ogg;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		//Sound System Init

	}




	public File[] songsInFolder() {
		File[] files = songFolder.listFiles(new FileFilterMp3());
		System.out.println(files);
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
			if(ogg != null) {
				ResourceLocation soundResLoc = new ResourceLocation(ogg.getAbsolutePath());
				ISound sound = new SoundThing(soundResLoc, String.valueOf(trackNum));
				Minecraft.getMinecraft().getSoundHandler().stopSound(sound);
			} else {
				playSong();
			}
		}
	}

	public void playSong() {
		if(ss == null) {
			getsndSystem();
		}
		if(trackNum == -1) {
			if(oggs.length > 0) {
				trackNum = 0;
			} else {
				return;
			}
		}
		ogg = oggs[trackNum];
		if(ogg == null) {
			for(int i = 0; i < 300; i++) {
				if(oggs[i] != null) {
					trackNum = i;
					i = 300;
				}
			}
		}
		ogg = oggs[trackNum];
		System.out.println("Playing File: " + ogg.getAbsoluteFile());
		pauseSong();
		// Play Song

		try {
			ss.newSource(true, ogg.getName(), ogg.toURI().toURL(), String.valueOf(trackNum), false, 0, 0, 0, 1, 1);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		ss.play(String.valueOf(trackNum));
		/*
		ResourceLocation soundResLoc = new ResourceLocation(ogg.getAbsolutePath());
		ISound sound = new SoundThing(soundResLoc, String.valueOf(trackNum));
		Minecraft.getMinecraft().getSoundHandler().playSound(sound);
		 */
	}



	@SideOnly(Side.CLIENT)
	protected  void getsndSystem(){
		synchronized(Minecraft.getMinecraft().getSoundHandler()){
			try {
				Field[] soundHandlerfields = SoundHandler.class.getDeclaredFields();
				SoundManager manager = null;

				for(Field f : soundHandlerfields){
					f.setAccessible(true);
					Object o = f.get(Minecraft.getMinecraft().getSoundHandler());
					if(o instanceof SoundManager ){
						manager = (SoundManager) o;
						break;
					}
				}


				Field[] sndSystemFields = SoundManager.class.getDeclaredFields();
				for(Field f : sndSystemFields){
					f.setAccessible(true);
					Object o = f.get(manager);
					if(o instanceof SoundSystem ){
						ss = (SoundSystem) o;
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
	}







}
