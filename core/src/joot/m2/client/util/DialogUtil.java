package joot.m2.client.util;

import de.tomgrill.gdxdialogs.core.GDXDialogs;
import de.tomgrill.gdxdialogs.core.GDXDialogsSystem;
import de.tomgrill.gdxdialogs.core.dialogs.GDXButtonDialog;

/**
 * 弹窗工具类
 * 
 * @author linxing
 *
 */
public final class DialogUtil {

	private static GDXDialogs dialogs = GDXDialogsSystem.install();
	
	/**
	 * 警告信息
	 * 
	 * @param title 标题
	 * @param message 信息内容
	 */
	public static void alert(String title, String message) {
		GDXButtonDialog bDialog = dialogs.newDialog(GDXButtonDialog.class);
		bDialog.setTitle(title);
		bDialog.setMessage(message);

		bDialog.addButton("确定");

		bDialog.build().show();
	}
}
