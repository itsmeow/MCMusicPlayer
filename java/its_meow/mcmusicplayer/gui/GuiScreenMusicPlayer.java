package its_meow.mcmusicplayer.gui;

import java.io.IOException;

import its_meow.mcmusicplayer.MCMusicPlayerMod;
import its_meow.mcmusicplayer.MusicManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import static its_meow.mcmusicplayer.MCMusicPlayerMod.musicManager;

public class GuiScreenMusicPlayer extends GuiScreen {


	/* Local Fields */
	public static GuiButton buttonPlay;
	public static GuiButton buttonNext;
	public static GuiButton buttonBack;
	public static GuiButton buttonDone;
	public static GuiButton buttonPause;
	public static GuiButton buttonStop;
	public static GuiButton buttonVolUp;
	public static GuiButton buttonVolDown;



	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		initGui();
		resetButtons();
		this.drawDefaultBackground();
		String text = "No Song Playing";
		this.drawCenteredString(fontRenderer, text, buttonPlay.x - (int) (fontRenderer.getStringWidth(text) / 2), buttonPlay.y + 35, 0);
		//this.drawCenteredString(fontRenderer, text, width / 2 - fontRenderer.getStringWidth(text), buttonPlay.y + 30, 0);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	public void updateText() {
		if(musicManager.ogg != null) {
			String name = musicManager.ogg.getName();
			String nameNoExtension = name.substring(0, name.indexOf('.'));
			if(musicManager.ogg != null) {
				String text = musicManager.trackNum + " - " + nameNoExtension + " is Playing. Volume is: " + musicManager.vol;
				this.drawCenteredString(fontRenderer, text, buttonPlay.x - fontRenderer.getStringWidth(text), buttonPlay.y + 35, 0);
				//this.setText(musicManager.trackNum + " - " + nameNoExtension + " is Playing. Volume is: " + musicManager.vol, true);
			} else { 
				String text = "No Song Playing";
				this.drawCenteredString(fontRenderer, text, buttonPlay.x - (int) (fontRenderer.getStringWidth(text) / 2), buttonPlay.y + 35, 0);
			}
		}
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
		buttonDone = new GuiButton(5608, width / 2 - 50, (height / 16) * 13, 100, 20, "Done"); // Done: 5608
		buttonPause = new GuiButton(5609, buttonPlay.x, buttonPlay.y + 20, buttonPlay.width, buttonPlay.height, "Pause"); // Pause: 5609
		buttonStop = new GuiButton(5610, buttonPlay.x + 50, buttonPlay.y + 40, 100, 20, "Stop");
		buttonVolUp = new GuiButton(5611, buttonNext.x + 35, buttonNext.y, 30, 20, "+");
		buttonVolDown = new GuiButton(5612, buttonBack.x - 35, buttonBack.y, 30, 20, "-");

		GuiButton[] buttons = {
				buttonPlay,
				buttonNext,
				buttonBack,
				buttonDone,
				buttonPause,
				buttonStop,
				buttonVolUp,
				buttonVolDown
		};

		//buttonStop.visible = false; // Stop is at the same location as play, so it is hidden.
		//buttonStop.enabled = false; // And disabled, so it doesn't get "pressed" while not visible.

		for(GuiButton button : buttons) {
			this.buttonList.add(button);
		}
	}
	
	
	
	@Override
	public void updateScreen() {
		if(musicManager.update) {
			musicManager.update = false;
			updateText();
		}
		super.updateScreen();
	}

	public void resetButtons() {
		this.buttonList.clear();

		GuiButton[] buttons = {
				buttonPlay,
				buttonNext,
				buttonBack,
				buttonDone,
				buttonPause,
				buttonStop,
				buttonVolUp,
				buttonVolDown
		};

		for(GuiButton button : buttons) {
			this.buttonList.add(button);
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if(musicManager.update) {
			musicManager.update = false;
			updateText();
		}
		
		if(button == buttonDone) {
			mc.displayGuiScreen(null);
			this.onGuiClosed();
		}
		if(button == buttonPlay) {
			musicManager.playSong();
		}
		if(button == buttonNext) {
			musicManager.nextSong();
		}
		if(button == buttonBack) {
			musicManager.lastSong();
		}
		if(button == buttonPause) {
			musicManager.pauseSong();
		}
		if(button == buttonStop) {
			musicManager.stopSong();
		}
		if(button == buttonVolUp) {
			musicManager.volUp();
			updateText();
		}
		if(button == buttonVolDown) {
			musicManager.volDown();
			updateText();
		}
	}







}
