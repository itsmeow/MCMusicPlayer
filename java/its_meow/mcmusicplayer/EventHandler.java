package its_meow.mcmusicplayer;

import its_meow.mcmusicplayer.gui.GuiScreenMusicPlayer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class EventHandler {

	@SubscribeEvent
	public void onGuiInit(InitGuiEvent.Post event) {
		if(event.getGui() instanceof GuiIngameMenu) { // Make sure GUI is Escape menu
			//System.out.println("Pause Menu opened");
			Minecraft mc = Minecraft.getMinecraft();
			ScaledResolution scaledRes = new ScaledResolution(mc);
			int width = scaledRes.getScaledWidth();
			int height = scaledRes.getScaledHeight();
			GuiButton button = new GuiButton(5604, width / 2 - 100, (height / 8) * 6, "Music");
			event.getButtonList().add(button);
		}
	}
	
	@SubscribeEvent
	public void onGuiActionPerformed(ActionPerformedEvent event) {
		if(event.getGui() instanceof GuiIngameMenu && event.getButton().id == 5604) { //Confirm my button was pressed
			Minecraft mc = Minecraft.getMinecraft();
			mc.displayGuiScreen(new GuiScreenMusicPlayer());
		}
	}

}
