package joot.m2.client.ui;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Null;

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
	/** 逐行显示历史消息的控件 */
	private Table[] linesMsg;
	private String[] strsMsg; // 历史消息内容
	private Drawable[] bgsMsg; // 历史消息背景色
	private Color[] colorsMsg; // 历史消息文字颜色
	private long msgsCount; // 历史消息总数
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
				DrawableUtil.Bg_LightGray,
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
						App.SmoothMoving = true;
						appendMsg("enable smooth moving", Color.GREEN, null);
						txtChat.setText("");
						return true;
					}
					if (say.equals("@smoothoff")) {
						App.SmoothMoving = false;
						appendMsg("disable smooth moving", Color.WHITE, DrawableUtil.Bg_Red);
						txtChat.setText("");
						return true;
					}
					appendMsg(say, null, null);
					txtChat.setText("");
					return true;
				}
				return super.keyUp(event, keycode);
			}
		});
		linesMsg = new Table[8]; // 显示8行文字
		for (var i = 0; i < linesMsg.length; ++i) {
			linesMsg[i] = new Table();
			linesMsg[i].background(DrawableUtil.Bg_White);
			linesMsg[i].setSize(380, 13);
			linesMsg[i].setPosition(16, 117 - i * 13); // 每行13像素，其中12像素文字加1像素padding
			addActor(linesMsg[i]);
		}
		strsMsg = new String[100]; // 最多记录历史数据条目数，可以改动此值来增加历史消息数目
		bgsMsg = new Drawable[strsMsg.length];
		colorsMsg = new Color[strsMsg.length];

		AssetUtil.<M2Texture>get(texs -> {
			int texIdx = 0;
			var slrMsgStyle = new SliderStyle(new TextureRegionDrawable(texs[texIdx++]),
					new TextureRegionDrawable(texs[texIdx++]));
			slrMsgStyle.knobOver = new TextureRegionDrawable(texs[texIdx++]);
			addActor(slrMsg = new Slider(0, strsMsg.length - 1, 1, true, slrMsgStyle));

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
		
		// libgdx的slider虽然可以竖直显示，但只能以上为增大，下为减小。因此后文为兼容数据与界面，用“max-”来设置和获取实际数值
		slrMsg.setValue(slrMsg.getMaxValue());
		slrMsg.setSize(16, 100);
		slrMsg.setPosition(366, 28);
		slrMsg.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				refreshMsgShow();
			}
			
		});
		btnMsgUp.setPosition(366, 128);
		btnMsgUp.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				slrMsg.setValue(slrMsg.getValue() + slrMsg.getStepSize());
			}
		});
		btnMsgDown.setPosition(366, 20);
		btnMsgDown.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				slrMsg.setValue(slrMsg.getValue() - slrMsg.getStepSize());
			}
		});
		btnExpandMsg.setPosition(366, 4);
	}
	
	/**
	 * 追加新消息到消息框
	 * 
	 * @param msg 消息文字内容
	 * @param fontColor 文字颜色，默认为黑色
	 * @param bg 背景色，默认为{@link DrawableUtil#Bg_White}，系统消息可能为{@link DrawableUtil#Bg_Red}
	 */
	public void appendMsg(String msg, @Null Color fontColor, @Null Drawable bg) {
		if (fontColor == null) {
			fontColor = Color.BLACK;
		}
		if (bg == null) {
			bg = DrawableUtil.Bg_White;
		}
		int _writeIdx = (int) (msgsCount++ % 100);
		strsMsg[_writeIdx] = msg;
		colorsMsg[_writeIdx] = fontColor;
		bgsMsg[_writeIdx] = bg;
		slrMsg.setProgrammaticChangeEvents(false);
		if (msgsCount > strsMsg.length) {
			// 超过最大缓存条目后永远显示最后几条
			slrMsg.setValue(0);
		} else {
			// 显示最后几条
			slrMsg.setValue(slrMsg.getMaxValue() - Math.max(0, msgsCount - linesMsg.length));
		}
		slrMsg.setProgrammaticChangeEvents(true);
		refreshMsgShow();
	}
	
	private void refreshMsgShow() {
		int readIdxMsg = (int) (slrMsg.getMaxValue() - slrMsg.getValue());
		for (var i = 0; i < linesMsg.length; ++i) {
			linesMsg[i].reset();
		}
		for (int i = 0, j = readIdxMsg; i < linesMsg.length; ++i, ++j) {
			if (j >= strsMsg.length) j = 0;
			if (strsMsg[j] == null) break;
			var lblMsg = new Label(strsMsg[j], new LabelStyle(FontUtil.Song_12_all_colored, colorsMsg[j]));
			linesMsg[i].background(bgsMsg[j])
				.add(lblMsg)
				.left()
				.growX()
				.padTop(1);
			lblMsg.addListener(new ClickListener() {
				
				public void clicked(InputEvent event, float x, float y) {
					txtChat.setText(lblMsg.getText().toString());
				}
				
			});
		}
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
		slrMsg.setX(getWidth() - 14);
		btnMsgUp.setX(getWidth() - 14);
		btnMsgDown.setX(getWidth() - 14);
		btnExpandMsg.setX(getWidth() - 14);
		for (var i = 0; i < linesMsg.length; ++i) {
			linesMsg[i].setWidth(getWidth() - 30);
		}
	}
}
