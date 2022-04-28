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

import joot.m2.client.App;
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
	/** 文本输入 */
	TextField txtChat;
	/** 历史消息展示 */
	private TextArea txtMsg;
	/** 历史消息滚动栏 */
	private Slider slrMsg;
	private Button btnMsgUp; // 向上单次滚动按钮
	private Button btnMsgDown; // 向上单次滚动按钮
	/** 扩展消息框高度，暂未实现 */
	private Button btnExpandMsg;

	public ChatBox() {
		addActor(txtChat = new TextField("", new TextField.TextFieldStyle(FontUtil.Song_12_all_outline,
				Color.BLACK,
				DrawableUtil.Cursor_DarkGray,
				DrawableUtil.Selection_LightGray,
				null)));
		txtChat.setPosition(16, 6);
		txtChat.setWidth(380);
		txtChat.setMaxLength(100);
		txtChat.addListener(new InputListener() {
			@Override
			public boolean keyUp(InputEvent event, int keycode) {
				if (keycode == Keys.ENTER) {
					String say = txtChat.getText().trim();
					if (say.isEmpty()) return true;
					if (say.equals("@smoothon")) {
						txtMsg.appendText("[GREEN]enable smooth moving");
						App.SmoothMoving = true;
						txtMsg.appendText(System.lineSeparator());
						txtChat.setText("");
						return true;
					}
					if (say.equals("@smoothoff")) {
						txtMsg.appendText("[RED]disable smooth moving");
						App.SmoothMoving = false;
						txtMsg.appendText(System.lineSeparator());
						txtChat.setText("");
						return true;
					}
					txtMsg.appendText(say);
					txtMsg.appendText(System.lineSeparator());
					txtChat.setText("");
					return true;
				}
				return super.keyUp(event, keycode);
			}
		});
		
		addActor(txtMsg = new TextArea("", new TextField.TextFieldStyle(FontUtil.Song_12_all_colored,
				Color.BLACK,
				DrawableUtil.Cursor_DarkGray,
				DrawableUtil.Selection_LightGray,
				DrawableUtil.Bg_White)));
		txtMsg.setPosition(16, 22);
		txtMsg.setSize(380, 106);
		txtMsg.setDisabled(true);

		AssetUtil.<M2Texture>get(texs -> {
			int texIdx = 0;
			var slrMsgStyle = new SliderStyle(new TextureRegionDrawable(texs[texIdx++]),
					new TextureRegionDrawable(texs[texIdx++]));
			slrMsgStyle.knobOver = new TextureRegionDrawable(texs[texIdx++]);
			addActor(slrMsg = new Slider(0, 100, 1, true, slrMsgStyle));

			addActor(btnMsgUp = new Button(new ButtonStyle(new TextureRegionDrawable(texs[texIdx++]),
					new TextureRegionDrawable(texs[texIdx++]), null)));

			addActor(btnMsgDown = new Button(new ButtonStyle(new TextureRegionDrawable(texs[texIdx++]),
					new TextureRegionDrawable(texs[texIdx++]), null)));

			addActor(btnExpandMsg = new Button(new ButtonStyle(new TextureRegionDrawable(texs[texIdx++]),
					new TextureRegionDrawable(texs[texIdx++]), null)));
		}
				, "newopui/20"
				, "newopui/27"
				, "newopui/28"
				, "newopui/21"
				, "newopui/22"
				, "newopui/24"
				, "newopui/25"
				, "newopui/30"
				, "newopui/31");
		
		slrMsg.setSize(16, 100);
		slrMsg.setPosition(366, 28);
		btnMsgUp.setSize(16, 10);
		btnMsgUp.setPosition(366, 128);
		btnMsgDown.setSize(16, 10);
		btnMsgDown.setPosition(366, 20);
		btnExpandMsg.setSize(16, 16);
		btnExpandMsg.setPosition(366, 4);
	}

	private M2Texture newopui10; // 聊天框左上角
	private M2Texture newopui11; // 聊天框上边框
	private M2Texture newopui12; // 聊天框右上角
	private M2Texture newopui13; // 聊天框左边框
	private M2Texture newopui14; // 聊天框右边框
	private M2Texture newopui15; // 聊天框左下角
	private M2Texture newopui16; // 聊天框上边框
	private M2Texture newopui17; // 聊天框右下角
	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (newopui10 == null) {
			newopui10 = AssetUtil.get("newopui/10");
		}
		if (newopui11 == null) {
			newopui11 = AssetUtil.get("newopui/11");
		}
		if (newopui12 == null) {
			newopui12 = AssetUtil.get("newopui/12");
		}
		if (newopui13 == null) {
			newopui13 = AssetUtil.get("newopui/13");
		}
		if (newopui14 == null) {
			newopui14 = AssetUtil.get("newopui/14");
		}
		if (newopui15 == null) {
			newopui15 = AssetUtil.get("newopui/15");
		}
		if (newopui16 == null) {
			newopui16 = AssetUtil.get("newopui/16");
		}
		if (newopui17 == null) {
			newopui17 = AssetUtil.get("newopui/17");
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
		if (newopui10 != null) {
			batch.draw(newopui10, getX(), getHeight() - newopui10.getHeight());
		}
		if (newopui12 != null) {
			batch.draw(newopui12, getX() + getWidth() - newopui12.getWidth(), getHeight() - newopui12.getHeight());
		}
		if (newopui15 != null) {
			batch.draw(newopui15, getX(), 0);
		}
		if (newopui17 != null) {
			batch.draw(newopui17, getX() + getWidth() - newopui12.getWidth(), 0);
		}
		
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
