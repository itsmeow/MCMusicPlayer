package its_meow.mcmusicplayer;

import its_meow.mcmusicplayer.proxy.CommonProxy;

import org.apache.logging.log4j.Logger;

import net.minecraft.client.resources.Language;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = Ref.MOD_ID, name = Ref.NAME, version = Ref.VERSION, acceptedMinecraftVersions = "[1.11,)", updateJSON = Ref.updateJSON) // Shows this is the mod class and defines the mod's variables.
public class MCMusicPlayerMod {

	@Instance(Ref.MOD_ID) 
	public static MCMusicPlayerMod mod;
	
	@SidedProxy(clientSide = Ref.CLIENT_PROXY_C, serverSide = Ref.SERVER_PROXY_C) // I have no clue what these do, ha
	public static CommonProxy proxy;
	
	public static Logger logger;

	

	
	@EventHandler // Declares this function as an event handler.
	public void preInit(FMLPreInitializationEvent event) { //Run Before Initialization.
		proxy.preInit(event);
	}
	
	@EventHandler // Declares this function as an event handler.
	public void init(FMLInitializationEvent e) { // Run After Pre-Initialization
		
	}
	
	@EventHandler // Declares this function as an event handler.
	public void postInit(FMLPostInitializationEvent e) { //Run after Initialization.
		
	}
	
	
}
