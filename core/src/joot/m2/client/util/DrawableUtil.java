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
	/** 文本选中背景色<br>浅灰 */
	public static Drawable Selection_LightGray = null;
	/** 光标颜色<br>黑色 */
	public static Drawable Cursor_DarkGray = null;
	
	static {
		var colorBits_LightGray = new byte[] {(byte) (Color.LIGHT_GRAY.r * 255), (byte) (Color.LIGHT_GRAY.g * 255), (byte) (Color.LIGHT_GRAY.b * 255), -1};
		var colorBits_Black = new byte[] {(byte) (Color.DARK_GRAY.r * 255), (byte) (Color.DARK_GRAY.g * 255), (byte) (Color.DARK_GRAY.b * 255), -1};
		var colorBits_Trans = new byte[4];
		
		var pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		//pm.getPixels().put(new byte[] {(byte) 0x66,(byte) 0x66,(byte) 0x66});
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
	}
}
