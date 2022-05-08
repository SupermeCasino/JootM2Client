package joot.m2.client.util;

import de.tomgrill.gdxdialogs.core.GDXDialogs;
import de.tomgrill.gdxdialogs.core.GDXDialogsSystem;
import de.tomgrill.gdxdialogs.core.dialogs.GDXButtonDialog;
import de.tomgrill.gdxdialogs.core.listener.ButtonClickListener;

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

	
	@FunctionalInterface
	public interface OperationConsumer {
		void op();
	}
	
	/**
	 * 确认信息
	 * 
	 * @param title 标题
	 * @param message 信息内容
	 * @param ok 点击“确定”按钮后执行的操作
	 */
	public static void confirm(String title, String message, OperationConsumer ok) {
		GDXButtonDialog bDialog = dialogs.newDialog(GDXButtonDialog.class);
		bDialog.setTitle(title);
		bDialog.setMessage(message);

		bDialog.addButton("确定");
		bDialog.addButton("取消");
		
		bDialog.setClickListener(new ButtonClickListener() {
			
			@Override
			public void click(int button) {
				if (button == 0 && ok != null) ok.op();
			}
		});

		bDialog.build().show();
	}
}
