package its_meow.mcmusicplayer.music;

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

import its_meow.mcmusicplayer.Ref;
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

	public File songFolder;
	File songFolderOgg;
	public File[] mp3s;
	public int trackNum = -1;
	public File mp3File;
	SoundSystem ss;
	UUID uuid;
	public boolean isPlaying;
	public Thread thread = null;
	public double vol = 0.1;
	public boolean justPaused;

	public void init() {
		songFolder = new File(CommonProxy.configDirectory, "/mcmusicplayer/songs/");
		if(!songFolder.exists()) {
			songFolder.mkdirs();
		}
		mp3s = songsInFolder();
	}




	public File[] songsInFolder() {
		File[] files = songFolder.listFiles(new FileFilterMp3());
		/*
		 * System.out.print("[" + Ref.MOD_ID +"] Songs loaded: [");
		 * for(File file : files) {
		 *    System.out.print(file.getName());
		 *    System.out.print(",");
		 * }
		 * System.out.print("]");
		 * System.out.println();
		 */
		return files;
	}

	public void nextSong() {
		if(trackNum == -1) {
			stopSong(true); // Close old song
			playSong();
		} else {
			if(trackNum < mp3s.length - 1) {
				trackNum++;
				stopSong(true); // Close old song
				playSong();
			} else {
				trackNum = 0;
				stopSong(true);
				playSong();
			}
		}
	}

	public void lastSong() {
		if(trackNum == -1) {
			playSong();
		} else {
			if(trackNum > 0) {
				trackNum--;
				stopSong(true); // Close old song
				playSong();
			} else {
				trackNum = mp3s.length - 1;
				stopSong(true); // Close old song
				playSong();
			}
		}
	}

	public void pauseSong() {
		if(trackNum == -1) {
			playSong();
		} else {
			if(mp3File != null) {
				isPlaying = false;
				ss.pause(uuid.toString());
				justPaused = true;
			}
		}
	}

	public void stopSong(boolean continues) {
		justPaused = false;
		if(trackNum == -1) {
			playSong();
		} else {
			if(mp3File != null) {
				if(!continues) {
					isPlaying = false;
				}
				ss.stop(uuid.toString());
				mp3File = null;
				ss.removeSource(uuid.toString());
			}
		}
	}

	public void playSong() {
		if(ss == null) {
			getsndSystem();
		}
		if(trackNum == -1) {
			if(mp3s.length > 0) {
				trackNum = 0;
			} else {
				return;
			}
		}
		if(ss.playing()) {
			stopSong(true);
		}
		/*mp3File = mp3s[trackNum];
		if(mp3File == null) {
			for(int i = 0; i < mp3s.length; i++) {
				if(mp3s[i] != null) {
					trackNum = i;
					i = mp3s.length;
				}
			}
		}
		mp3File = mp3s[trackNum];
		if(mp3File == null) {
			boolean breakme = false;
			for(int i = mp3s.length - 1; i > 0 | breakme; i--) {
				if(mp3s[i] != null) {
					trackNum = i;
					breakme = true;
				}
			}
		}*/
		mp3File = mp3s[trackNum];
		while(!mp3File.exists()) {
			trackNum++;
			mp3File = mp3s[trackNum];
		}
		// Play Song
		if(!justPaused) {
			uuid = uuid.randomUUID();
			try {
				ss.newStreamingSource(true, uuid.toString(), mp3File.toURI().toURL(), mp3File.getName(), false, 0, 0, 0, 1, 1);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			Minecraft.getMinecraft().getSoundHandler().stopSounds();
		} else {
			justPaused = false;
		}
		ss.setVolume(uuid.toString(), (float) vol);
		System.out.println("Preparing to play: " + mp3File.getName());
		ss.play(uuid.toString());
		isPlaying = true;
		thread = new Thread(new Runnable() {

			@Override
			public void run() {
				while(ss.initialized()) {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(!ss.playing() && isPlaying && mp3File == null) {
						nextSong();
					}
				}
			} 
		});
		thread.start();
	}

	public void volDown() {
		if(Math.round((vol - 0.02) * 100.0) / 100.0 >= 0) {
			double newVol = Math.round((vol - 0.02) * 100.0) / 100.0;
			ss.setVolume(uuid.toString(), (float) newVol);
			vol = newVol;
		}
	}

	public void volUp() {
		if(Math.round((vol + 0.02) * 100.0) / 100.0 <= 0.2) {
			double newVol = Math.round((vol + 0.02) * 100.0) / 100.0;
			ss.setVolume(uuid.toString(), (float) newVol);
			vol = newVol;
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
