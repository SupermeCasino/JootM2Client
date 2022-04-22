package joot.m2.client.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import joot.m2.client.App;
import joot.m2.client.util.NetworkUtil;

public class DesktopLauncher {
	public static void main (String[] arg) {
		var config = new Lwjgl3ApplicationConfiguration();
		config.setResizable(true);
		config.setWindowedMode(800, 600);
		config.setWindowSizeLimits(800, 600, -1, -1);
		config.setWindowIcon(FileType.Internal, "mir.jpg");
		config.setTitle("将唐传奇");
		config.useVsync(true);
	    config.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate);
		new Lwjgl3Application(new App(), config);
		
		NetworkUtil.shutdown();
	}
}
