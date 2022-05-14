package joot.m2.client.util;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * 文字工具类
 * 
 * @author linxing
 *
 */
public final class FontUtil {
	private static AssetManager bmFontLoader = new AssetManager();
	
	/** 默认字体 */
	public static BitmapFont Default = new BitmapFont();
	
	/** bmp文字12像素宋体<br>所有文字28522<br>边缘平滑 */
	public static BitmapFont Song_12_all_outline = null;
	/** bmp文字12像素宋体<br>所有文字28522<br>可以染色 */
	public static BitmapFont Song_12_all_colored = null;
	
	static {
		
		bmFontLoader.load("fonts/song12/all_outline.fnt", BitmapFont.class);
		while(!bmFontLoader.isLoaded("fonts/song12/all_outline.fnt")) bmFontLoader.update();
		Song_12_all_outline = bmFontLoader.get("fonts/song12/all_outline.fnt");
		
		bmFontLoader.load("fonts/song12/all.fnt", BitmapFont.class);
		while(!bmFontLoader.isLoaded("fonts/song12/all.fnt")) bmFontLoader.update();
		Song_12_all_colored = bmFontLoader.get("fonts/song12/all.fnt");
		Song_12_all_colored.getData().markupEnabled = true;
		Song_12_all_colored.getData().lineHeight = 14;
	}
	
	/** 停止 */
	public static void shutdown() {
		bmFontLoader.dispose();
	}
}
