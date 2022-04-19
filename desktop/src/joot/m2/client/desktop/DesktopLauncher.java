package joot.m2.client.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import joot.m2.client.App;

public class DesktopLauncher {
	public static void main (String[] arg) {
		var config = new LwjglApplicationConfiguration();
		config.resizable = true;
		config.width = 1024;
		config.height = 768;
		config.title = "将唐传奇";
		config.addIcon("mir.jpg", FileType.Internal);
		new LwjglApplication(new App(), config);
	}
}
