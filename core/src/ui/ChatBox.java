package ui;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import joot.m2.client.image.M2Texture;
import joot.m2.client.util.AssetUtil;
import joot.m2.client.util.DrawableUtil;
import joot.m2.client.util.FontUtil;

/**
 * 聊天框
 * 
 * @author linxing
 *
 */
public final class ChatBox extends WidgetGroup {
	private M2Texture newopui10; // 聊天框左上角
	private M2Texture newopui11; // 聊天框上边框
	private M2Texture newopui12; // 聊天框右上角
	private M2Texture newopui13; // 聊天框左边框
	private M2Texture newopui14; // 聊天框右边框
	private M2Texture newopui15; // 聊天框左下角
	private M2Texture newopui16; // 聊天框上边框
	private M2Texture newopui17; // 聊天框右下角
	
	/** 文本输入 */
	private TextField txtChat;
	/** 历史消息展示 */
	private TextArea txtMsg;
	/** 历史消息滚动栏 */
	private Slider slrMsg;
	private Button btnMsgUp; // 向上单次滚动按钮
	private Button btnMsgDown; // 向上单次滚动按钮
	/** 扩展消息框高度，暂未实现 */
	private Button btnExpandMsg;

	public ChatBox() {
		// 经测试，最大能使用12px，此时光标和选中背景色与文本框本身没有间隙不好看
		// 10像素比较合适，但文字很小看起来眼睛疼。
		// 后面看看是不是在文字本身上做点功夫。比如加粗加阴影，颜色固定为黑色，可以保留轮廓信息，且固定颜色可以用8位图节省大小
		//  当然，也可能不行
		addActor((txtChat = new TextField("", new TextField.TextFieldStyle(FontUtil.HeiTi_10_all,
				Color.BLACK,
				DrawableUtil.Cursor_DarkGray,
				DrawableUtil.Selection_LightGray,
				null))));
		txtChat.setPosition(16, 7);
		txtChat.setWidth(380);
		txtChat.setMaxLength(100);
		txtChat.addListener(new InputListener() {
			@Override
			public boolean keyUp(InputEvent event, int keycode) {
				if (keycode == Keys.ENTER) {
					txtMsg.appendText(txtChat.getText());
					txtMsg.appendText(System.lineSeparator());
					txtChat.setText("");
					return true;
				}
				return super.keyUp(event, keycode);
			}
		});
		
		addActor((txtMsg = new TextArea("", new TextField.TextFieldStyle(FontUtil.HeiTi_10_all_colored,
				Color.BLACK,
				DrawableUtil.Cursor_DarkGray,
				DrawableUtil.Selection_LightGray,
				DrawableUtil.Bg_White))));
		txtMsg.setPosition(16, 22);
		txtMsg.setSize(380, 110);
		txtMsg.setDisabled(true);

		while(!AssetUtil.isLoaded("newopui/20")) AssetUtil.update();
		while(!AssetUtil.isLoaded("newopui/21")) AssetUtil.update();
		while(!AssetUtil.isLoaded("newopui/22")) AssetUtil.update();
		while(!AssetUtil.isLoaded("newopui/24")) AssetUtil.update();
		while(!AssetUtil.isLoaded("newopui/25")) AssetUtil.update();
		while(!AssetUtil.isLoaded("newopui/27")) AssetUtil.update();
		while(!AssetUtil.isLoaded("newopui/28")) AssetUtil.update();
		while(!AssetUtil.isLoaded("newopui/29")) AssetUtil.update();
		while(!AssetUtil.isLoaded("newopui/30")) AssetUtil.update();
		while(!AssetUtil.isLoaded("newopui/31")) AssetUtil.update();
		var slrMsgStyle = new SliderStyle(new TextureRegionDrawable(AssetUtil.<M2Texture>get("newopui/20")),
				new TextureRegionDrawable(AssetUtil.<M2Texture>get("newopui/27")));
		slrMsgStyle.knobOver = new TextureRegionDrawable(AssetUtil.<M2Texture>get("newopui/28"));
		addActor((slrMsg = new Slider(0, 100, 1, true, slrMsgStyle)));
		slrMsg.setSize(16, 100);
		slrMsg.setPosition(366, 28);
		addActor((btnMsgUp = new Button(new ButtonStyle(new TextureRegionDrawable(AssetUtil.<M2Texture>get("newopui/21")),
				new TextureRegionDrawable(AssetUtil.<M2Texture>get("newopui/22")), null))));
		btnMsgUp.setSize(16, 10);
		btnMsgUp.setPosition(366, 128);
		addActor((btnMsgDown = new Button(new ButtonStyle(new TextureRegionDrawable(AssetUtil.<M2Texture>get("newopui/24")),
				new TextureRegionDrawable(AssetUtil.<M2Texture>get("newopui/25")), null))));
		btnMsgDown.setSize(16, 10);
		btnMsgDown.setPosition(366, 20);
		addActor((btnExpandMsg = new Button(new ButtonStyle(new TextureRegionDrawable(AssetUtil.<M2Texture>get("newopui/30")),
				new TextureRegionDrawable(AssetUtil.<M2Texture>get("newopui/31")), null))));
		btnExpandMsg.setSize(16, 16);
		btnExpandMsg.setPosition(366, 4);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (newopui10 == null) {
			if (AssetUtil.isLoaded("newopui/10")) {
				newopui10 = AssetUtil.get("newopui/10");
			}
		}
		if (newopui11 == null) {
			if (AssetUtil.isLoaded("newopui/11")) {
				newopui11 = AssetUtil.get("newopui/11");
			}
		}
		if (newopui12 == null) {
			if (AssetUtil.isLoaded("newopui/12")) {
				newopui12 = AssetUtil.get("newopui/12");
			}
		}
		if (newopui13 == null) {
			if (AssetUtil.isLoaded("newopui/13")) {
				newopui13 = AssetUtil.get("newopui/13");
			}
		}
		if (newopui14 == null) {
			if (AssetUtil.isLoaded("newopui/14")) {
				newopui14 = AssetUtil.get("newopui/14");
			}
		}
		if (newopui15 == null) {
			if (AssetUtil.isLoaded("newopui/15")) {
				newopui15 = AssetUtil.get("newopui/15");
			}
		}
		if (newopui16 == null) {
			if (AssetUtil.isLoaded("newopui/16")) {
				newopui16 = AssetUtil.get("newopui/16");
			}
		}
		if (newopui17 == null) {
			if (AssetUtil.isLoaded("newopui/17")) {
				newopui17 = AssetUtil.get("newopui/17");
			}
		}
		if (newopui11 != null) {
			// 绘制聊天框上边框
			for (var x = 0; x < getWidth(); x += newopui11.getWidth()) {
				var restWidth = getWidth() - x;
				batch.draw(newopui11, getX() + x, getHeight() - newopui11.getHeight(), 0, 0, (int) Math.min(newopui11.getWidth(), restWidth), newopui11.getHeight());
			}
		}
		// 聊天框左边框
		if (newopui13 != null) {
			// 高度是一定的，画两次就够了
			batch.draw(newopui13, getX(), 40);
			batch.draw(newopui13, getX(), 80);
		}
		// 聊天框右边框
		if (newopui14 != null) {
			batch.draw(newopui14, getX() + getWidth() - newopui14.getWidth(), 40);
			batch.draw(newopui14, getX() + getWidth() - newopui14.getWidth(), 80);
		}
		// 聊天框下边框
		if (newopui16 != null) {
			for (var x = 0; x < getWidth(); x += newopui16.getWidth()) {
				var restWidth = getWidth() - x;
				batch.draw(newopui16, getX() + x, 0, 0, 0, (int) Math.min(newopui16.getWidth(), restWidth), newopui16.getHeight());
			}
		}
		// 四个角
		batch.draw(newopui10, getX(), getHeight() - newopui10.getHeight());
		batch.draw(newopui12, getX() + getWidth() - newopui12.getWidth(), getHeight() - newopui12.getHeight());
		batch.draw(newopui15, getX(), 0);
		batch.draw(newopui17, getX() + getWidth() - newopui12.getWidth(), 0);
		
		
		super.draw(batch, parentAlpha);
	}
	
	@Override
	public void layout() {
		txtChat.setWidth(getWidth() - 30);
		txtMsg.setWidth(getWidth() - 30);
		slrMsg.setX(getWidth() - 14);
		btnMsgUp.setX(getWidth() - 14);
		btnMsgDown.setX(getWidth() - 14);
		btnExpandMsg.setX(getWidth() - 14);
	}
}
