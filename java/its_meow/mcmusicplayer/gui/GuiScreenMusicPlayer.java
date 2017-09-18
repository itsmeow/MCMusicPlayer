package its_meow.mcmusicplayer.gui;

import java.io.IOException;

import its_meow.mcmusicplayer.MCMusicPlayerMod;
import its_meow.mcmusicplayer.MusicManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public class GuiScreenMusicPlayer extends GuiScreen {


	/* Local Fields */
	private GuiButton buttonPlay;
	private GuiButton buttonNext;
	private GuiButton buttonBack;
	private GuiButton buttonDone;
	private GuiButton buttonStop;



	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}

	@Override
	public void initGui() {

		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution scaledRes = new ScaledResolution(mc);
		int width = scaledRes.getScaledWidth();
		int height = scaledRes.getScaledHeight();

		buttonPlay = new GuiButton(5606, width / 2 - 100, height / 2, 200, 20, "Play"); // Play: 5606
		buttonNext = new GuiButton(5607, buttonPlay.x + 215, buttonPlay.y, 30, 20, "Next"); // Next 5607
		buttonBack = new GuiButton(5605, buttonPlay.x - 45, buttonPlay.y, 30, 20, "Back"); // Back: 5605
		buttonDone = new GuiButton(5608, width / 2 - 50, (height / 8) * 6, 100, 20, "Done"); // Done: 5608
		buttonStop = new GuiButton(5609, buttonPlay.x, buttonPlay.y + 20, buttonPlay.width, buttonPlay.height, "Stop"); // Stop: 5609

		GuiButton[] buttons = {
				buttonPlay,
				buttonNext,
				buttonBack,
				buttonDone,
				buttonStop
		};

		buttonStop.visible = false; // Stop is at the same location as play, so it is hidden.
		buttonStop.enabled = false; // And disabled, so it doesn't get "pressed" while not visible.

		for(GuiButton button : buttons) {
			this.buttonList.add(button);
		}
	}

	public void resetButtons() {
		this.buttonList.clear();
		
		GuiButton[] buttons = {
				buttonPlay,
				buttonNext,
				buttonBack,
				buttonDone,
				buttonStop
		};

		for(GuiButton button : buttons) {
			this.buttonList.add(button);
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if(button == buttonDone) {
			mc.displayGuiScreen(null);
			this.onGuiClosed();
		}
		buttonPlay.enabled = true;
		if(button == buttonPlay && buttonPlay.enabled) {
			MCMusicPlayerMod.musicManager.playSong();
			buttonPlay.visible = false;
			buttonPlay.enabled = false;
			buttonStop.visible = true;
			buttonStop.enabled = true;
		}
		if(button == buttonNext) {
			MCMusicPlayerMod.musicManager.nextSong();
		}
		if(button == buttonBack) {
			MCMusicPlayerMod.musicManager.lastSong();
		}
		if(button == buttonStop && buttonStop.enabled) {
			MCMusicPlayerMod.musicManager.pauseSong();
			buttonStop.visible = false;
			buttonStop.enabled = false;
			buttonPlay.visible = true;
			buttonPlay.enabled = true;
		}
	}







}
