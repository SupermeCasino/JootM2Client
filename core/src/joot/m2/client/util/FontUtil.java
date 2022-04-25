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
	
	/** bmp文字10像素黑体<br>所有文字28522 */
	public static BitmapFont HeiTi_10_all = null;
	/** bmp文字10像素黑体<br>所有文字28522<br>可以染色 */
	public static BitmapFont HeiTi_10_all_colored = null;
	
	static {
		bmFontLoader.load("fonts/heiti10/heiti10.fnt", BitmapFont.class);
		while(!bmFontLoader.isLoaded("fonts/heiti10/heiti10.fnt")) bmFontLoader.update();
		HeiTi_10_all = bmFontLoader.get("fonts/heiti10/heiti10.fnt");
		
		bmFontLoader.load("fonts/heiti10/heiti10_colored.fnt", BitmapFont.class);
		while(!bmFontLoader.isLoaded("fonts/heiti10/heiti10_colored.fnt")) bmFontLoader.update();
		HeiTi_10_all_colored = bmFontLoader.get("fonts/heiti10/heiti10_colored.fnt");
		HeiTi_10_all_colored.getData().markupEnabled = true;
	}
	
	/** 停止 */
	public static void shutdown() {
		bmFontLoader.dispose();
	}
}
