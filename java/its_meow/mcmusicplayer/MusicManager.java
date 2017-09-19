package its_meow.mcmusicplayer;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.sound.sampled.AudioFormat;

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
	public File[] oggs;
	public int trackNum = -1;
	public File ogg;
	SoundSystem ss;
	UUID uuid;
	File[] mp3s;
	boolean isPlaying;
	Thread thread = null;
	public float vol = 0.5F;
	public boolean update = false;
	boolean justPaused;

	public void init() {
		songFolder = new File(CommonProxy.configDirectory, "/mcmusicplayer/songs/");
		if(!songFolder.exists()) {
			songFolder.mkdirs();
		}
		songFolderOgg = new File(CommonProxy.configDirectory, "/mcmusicplayer/songs/oggs");
		if(!songFolderOgg.exists()) {
			songFolderOgg.mkdirs();
		}
		mp3s = songsInFolder();
		oggs = new File[300];
		System.out.println("Converting songs... Game may take a while to progress loading.");
		for(int i = 0; i < mp3s.length; i++) {
			File mp3 = mp3s[i];
			if(mp3 != null) {
				File ogg = new File(songFolderOgg.getAbsolutePath() + "/" + mp3.getName().substring(0, mp3.getName().indexOf(".mp3")) + ".ogg");
				if(!ogg.exists()) {
					AudioConverter ac = new AudioConverter();
					ac.encodeAudio(mp3, ogg, "audio/mp3");
				}
				oggs[i] = ogg;
			}
		}
		/*
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				getsndSystem();
				for(File ogg : oggs) {
					try {
						ss.loadSound(ogg.toURI().toURL(), ogg.getName());
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
				}
			} 
		});
		thread.start();*/


	}




	public File[] songsInFolder() {
		File[] files = songFolder.listFiles(new FileFilterMp3());
		System.out.print("[" + Ref.MOD_ID +"] Songs loaded: [");
		for(File file : files) {
			System.out.print(file.getName());
			System.out.print(",");
		}
		System.out.print("]");
		System.out.println();
		return files;
	}

	public void nextSong() {
		if(trackNum == -1) {
			stopSong(); // Close old song
			playSong();
		} else {
			if(trackNum != oggs.length) {
				trackNum++;
				stopSong(); // Close old song
				playSong();
			} else {
				trackNum = 0;
				stopSong();
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
				stopSong(); // Close old song
				playSong();
			} else {
				trackNum = oggs.length;
				stopSong(); // Close old song
				playSong();
			}
		}
	}

	public void pauseSong() {
		if(trackNum == -1) {
			playSong();
		} else {
			if(ogg != null) {
				//ss.stop(uuid.toString());
				isPlaying = false;
				ss.pause(uuid.toString());
				justPaused = true;
				//ResourceLocation soundResLoc = new ResourceLocation(ogg.getAbsolutePath());
				//ISound sound = new SoundThing(soundResLoc, String.valueOf(trackNum));
				//Minecraft.getMinecraft().getSoundHandler().stopSound(sound);
			} else {
				//playSong();
			}
		}
	}

	public void stopSong() {
		if(trackNum == -1) {
			playSong();
		} else {
			if(ogg != null) {
				//ss.stop(uuid.toString());
				isPlaying = false;
				ss.stop(uuid.toString());
				//ResourceLocation soundResLoc = new ResourceLocation(ogg.getAbsolutePath());
				//ISound sound = new SoundThing(soundResLoc, String.valueOf(trackNum));
				//Minecraft.getMinecraft().getSoundHandler().stopSound(sound);
			} else {
				//playSong();
			}
		}
	}

	public void playSong() {
		//init();
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
		if(ss.playing()) {
			stopSong();
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
		while(!ogg.exists()) {
			trackNum++;
			ogg = oggs[trackNum];
		}
		System.out.println("Playing File: " + ogg.getAbsoluteFile());
		// Play Song
		uuid = uuid.randomUUID();
		if(!justPaused) {
			try {
				ss.newStreamingSource(true, uuid.toString(), ogg.toURI().toURL(), ogg.getName(), false, 0, 0, 0, 1, 1);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			Minecraft.getMinecraft().getSoundHandler().stopSounds();
		} else {
			justPaused = false;
		}
		ss.setVolume(uuid.toString(), vol);
		System.out.println("Preparing to play: " + ogg.getName());
		ss.play(uuid.toString());
		isPlaying = true;
		thread = new Thread(new Runnable() {

			@Override
			public void run() {
				while(ss.initialized()) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					update = true;
					if(!ss.playing() && isPlaying) {
						nextSong();
					}
				}
			} 
		});
		thread.start();
		/*
		ResourceLocation soundResLoc = new ResourceLocation(ogg.getAbsolutePath());
		ISound sound = new SoundThing(soundResLoc, String.valueOf(trackNum));
		Minecraft.getMinecraft().getSoundHandler().playSound(sound);
		 */
	}

	public void volDown() {
		if(vol - 0.05 >= 0) {
			float newVol = vol - 0.05F;
			ss.setVolume(uuid.toString(), newVol);
		}
	}

	public void volUp() {
		if(vol + 0.05 <= 1) {
			float newVol = vol + 0.05F;
			ss.setVolume(uuid.toString(), newVol);
		}
	}


	@SideOnly(Side.CLIENT)
	protected void getsndSystem(){
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
