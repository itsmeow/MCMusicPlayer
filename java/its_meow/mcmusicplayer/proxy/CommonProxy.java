package its_meow.mcmusicplayer.proxy;

import its_meow.mcmusicplayer.MCMusicPlayerMod;
import its_meow.mcmusicplayer.config.MCMusicPlayerConfig;

import java.io.File;

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
	
	public void preInit(FMLPreInitializationEvent e) {
		System.out.println("Common Pre-Init");
		File directory = e.getModConfigurationDirectory();
        config = new Configuration(new File(directory.getPath(), "/mcmusicplayer/mcmusicplayer.cfg")); 
        MCMusicPlayerConfig.readConfig();
	}
	
	public void Init(FMLInitializationEvent e) {
		System.out.println("Common Init(blank)");
	}
	
	public void postInit(FMLPostInitializationEvent e){
		if(config.hasChanged()){
			config.save();
		}
	}

	
	
}
