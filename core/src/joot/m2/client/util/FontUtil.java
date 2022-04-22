package joot.m2.client.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * 文字工具类
 * 
 * @author linxing
 *
 */
public final class FontUtil {
	/** bmp文字12像素黑体<br>所有文字28522 */
	public static BitmapFont HeiTi_12_all = null;
	
	static {
		HeiTi_12_all = new BitmapFont(Gdx.files.internal("fonts/heiti12/heiti12.fnt"));
	}
}
