package its_meow.mcmusicplayer;

import java.io.File;
import java.io.FileFilter;

public class FileFilterMp3 implements FileFilter {

	@Override
	public boolean accept(File arg0) {
		return arg0.getName().endsWith(".mp3");
	}

}
