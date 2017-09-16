package its_meow.mcmusicplayer.config;

import its_meow.mcmusicplayer.MCMusicPlayerMod;
import its_meow.mcmusicplayer.proxy.CommonProxy;

import org.apache.logging.log4j.Level;

import net.minecraftforge.common.config.Configuration;

public class MCMusicPlayerConfig {
	
	public static void readConfig(){
		Configuration cfg = CommonProxy.config;
		try {
			cfg.load();
			initConfig(cfg);
		} catch (Exception e1) {
			MCMusicPlayerMod.logger.log(Level.ERROR, "Problem Loading Config!!", e1);
		} finally {
		if(cfg.hasChanged()){
			cfg.save();
		}
	}
	}
	
	private static void initConfig(Configuration cfg) {
        
	}
	
}
