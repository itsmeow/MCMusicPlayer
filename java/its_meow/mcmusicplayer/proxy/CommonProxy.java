package its_meow.mcmusicplayer.proxy;

import its_meow.mcmusicplayer.EventHandler;
import its_meow.mcmusicplayer.MCMusicPlayerMod;
import its_meow.mcmusicplayer.config.MCMusicPlayerConfig;

import java.io.File;

import javax.sound.sampled.LineUnavailableException;

import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.oredict.OreDictionary;

public class CommonProxy {

	public static Configuration config;
	public static File configDirectory;
	
	public void preInit(FMLPreInitializationEvent e) {
		File directory = e.getModConfigurationDirectory();
		configDirectory = directory;
        config = new Configuration(new File(directory.getPath(), "/mcmusicplayer/mcmusicplayer.cfg")); 
        MCMusicPlayerConfig.readConfig();
        EventHandler handler = new EventHandler();
		MinecraftForge.EVENT_BUS.register(handler);
	}
	
	public void Init(FMLInitializationEvent e) {
	}
	
	public void postInit(FMLPostInitializationEvent e){
		MCMusicPlayerMod.musicManager.init();
		if(config.hasChanged()){
			config.save();
		}
	}

	
	
}
