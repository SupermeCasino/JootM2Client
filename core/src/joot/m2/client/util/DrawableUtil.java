package joot.m2.client.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * 背景/光标等资源工具类
 * 
 * @author linxing
 *
 */
public final class DrawableUtil {
	/** 文本选中背景<br>浅灰 */
	public static Drawable Selection_LightGray = null;
	/** 光标<br>深灰 */
	public static Drawable Cursor_DarkGray = null;
	/** 背景<br>纯白 */
	public static Drawable Bg_White = null;
	/** 背景<br>红色 */
	public static Drawable Bg_Red = null;
	
	static {
		var colorBits_LightGray = new byte[] {(byte) (Color.LIGHT_GRAY.r * 255), (byte) (Color.LIGHT_GRAY.g * 255), (byte) (Color.LIGHT_GRAY.b * 255), -1};
		var colorBits_Black = new byte[] {(byte) (Color.DARK_GRAY.r * 255), (byte) (Color.DARK_GRAY.g * 255), (byte) (Color.DARK_GRAY.b * 255), -1};
		var colorBits_Trans = new byte[4];
		var colorBits_White = new byte[] {(byte) (Color.WHITE.r * 255), (byte) (Color.WHITE.g * 255), (byte) (Color.WHITE.b * 255), -1};
		var colorBits_Red = new byte[] {(byte) (Color.RED.r * 255), (byte) (Color.RED.g * 255), (byte) (Color.RED.b * 255), -1};
		
		var pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		pm.getPixels().put(colorBits_LightGray);
		pm.getPixels().flip();
		Selection_LightGray = new TextureRegionDrawable(new Texture(pm));
		
		pm = new Pixmap(3, 3, Pixmap.Format.RGBA8888);
		pm.getPixels().put(colorBits_Black);
		pm.getPixels().put(colorBits_Black);
		pm.getPixels().put(colorBits_Black);
		pm.getPixels().put(colorBits_Trans);
		pm.getPixels().put(colorBits_Black);
		pm.getPixels().put(colorBits_Trans);
		pm.getPixels().put(colorBits_Black);
		pm.getPixels().put(colorBits_Black);
		pm.getPixels().put(colorBits_Black);
		pm.getPixels().flip();
		Cursor_DarkGray = new NinePatchDrawable(new NinePatch(new Texture(pm), 1, 1, 1, 1));
		
		pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		pm.getPixels().put(colorBits_White);
		pm.getPixels().flip();
		Bg_White = new TextureRegionDrawable(new Texture(pm));
		
		pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		pm.getPixels().put(colorBits_Red);
		pm.getPixels().flip();
		Bg_Red = new TextureRegionDrawable(new Texture(pm));
	}
}
