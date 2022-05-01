package joot.m2.client.ui;

import javax.swing.JOptionPane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.github.jootnet.m2.core.net.MessageType;
import com.github.jootnet.m2.core.net.messages.LoginResp;

import joot.m2.client.App;
import joot.m2.client.image.M2Texture;
import joot.m2.client.util.AssetUtil;
import joot.m2.client.util.DrawableUtil;
import joot.m2.client.util.FontUtil;
import joot.m2.client.util.NetworkUtil;

/**
 * 登录框
 */
public final class LoginPane extends WidgetGroup {
	private Image bg;
	/** 新用户按钮 */
	private ImageButton btnNew;
	/** 修改密码按钮 */
	private ImageButton btnModifyPsw;
	/** 登录按钮 */
	private ImageButton btnLogin;
	/** 退出按钮 */
	private ImageButton btnExit;
	/** 用户名 */
	private TextField txtUna;
	/** 密码 */
	private TextField txtPsw;
	
	private OperationConsumer requestNewUser;
	private OperationConsumer requestModifyPsw;
	
	@FunctionalInterface
	public interface OperationConsumer {
		void op();
	}

	/**
	 * 
	 * @param newUserOperationConsumer 点击“新用户”按钮进行的操作
	 * @param modifyPswOperationConsumer 点击“修改密码”按钮进行的操作
	 */
	public LoginPane(OperationConsumer newUserOperationConsumer, OperationConsumer modifyPswOperationConsumer) {
		requestNewUser = newUserOperationConsumer;
		requestModifyPsw = modifyPswOperationConsumer;
		
		AssetUtil.<M2Texture>get(texs -> {
			var texIdx = 0;
			addActor(bg = new Image(texs[texIdx++]));
			addActor(btnNew = new ImageButton(new ImageButtonStyle(new TextureRegionDrawable(texs[texIdx++]), null, null, null, null, null)));
			addActor(btnModifyPsw = new ImageButton(new ImageButtonStyle(new TextureRegionDrawable(texs[texIdx++]), null, null, null, null, null)));
			addActor(btnLogin = new ImageButton(new ImageButtonStyle(null, new TextureRegionDrawable(texs[texIdx++]), null, null, null, null)));
			addActor(btnExit = new ImageButton(new ImageButtonStyle(null, new TextureRegionDrawable(texs[texIdx++]), null, null, null, null)));
		}
				, "prguse/60"
				, "prguse/61"
				, "prguse/53"
				, "prguse/62"
				, "prguse/64");
		btnNew.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (requestNewUser != null) requestNewUser.op();
			}
			
		});
		btnModifyPsw.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (requestModifyPsw != null) requestModifyPsw.op();
			}
			
		});
		btnExit.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
			
		});
		btnLogin.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				String una = txtUna.getText().trim();
				String psw = txtPsw.getText().trim();
				if (una.isEmpty() || psw.isEmpty()) {
					return;
				}
				NetworkUtil.sendLoginReq(una, psw);
			}
			
		});
		
		addActor(txtUna = new TextField("", new TextFieldStyle(FontUtil.Song_12_all_colored,
				Color.WHITE,
				DrawableUtil.Cursor_White,
				DrawableUtil.Bg_LightGray,
				null)));
		txtUna.setWidth(130);
		txtUna.setMaxLength(20);
		addActor(txtPsw = new TextField("", new TextFieldStyle(FontUtil.Song_12_all_colored,
				Color.WHITE,
				DrawableUtil.Cursor_White,
				DrawableUtil.Bg_LightGray,
				null)));
		txtPsw.setWidth(130);
		txtPsw.setMaxLength(20);
		txtPsw.setPasswordMode(true);
		txtPsw.setPasswordCharacter('*');
		txtPsw.addListener(new InputListener() {
			
			@Override
			public boolean keyUp(InputEvent event, int keycode) {
				if (keycode == Keys.ENTER || keycode == Keys.NUMPAD_ENTER) {
					String una = txtUna.getText().trim();
					String psw = txtPsw.getText().trim();
					if (!una.isEmpty() && !psw.isEmpty()) {
						NetworkUtil.sendLoginReq(una, psw);
					}
					return true;
				}
				return false;
			}
			
		});
	}
	
	@Override
	public void act(float delta) {
		NetworkUtil.recv(msg -> {
			if (msg.type() == MessageType.LOGIN_RESP) {
				var loginResp = (LoginResp) msg;
				var code = loginResp.code;
				if (code == 0) {
					App.Roles = loginResp.roles;
					App.lastName = loginResp.lastName;
					App.toChrSel();
				} else {
					var tip = loginResp.serverTip;
					if (tip == null) {
						switch (loginResp.code) {
						case 1:
							tip = "用户名或密码错误";
							break;
						case 2:
							tip = "用户不存在";
							break;
						}
					}
					JOptionPane.showMessageDialog(null, tip);
				}
				return true;
			}
			return false;
		});
		
		super.act(delta);
	}
	
	/**
	 * 将焦点给到输入框
	 */
	public void focusInput() {
		getStage().setKeyboardFocus(txtUna);
	}
	
	@Override
	public void layout() {
		bg.setPosition((getWidth() - bg.getWidth()) / 2, (getHeight() - bg.getHeight()) / 2);
		btnNew.setPosition((getWidth() - bg.getWidth()) / 2 + 25, (getHeight() - bg.getHeight()) / 2 + 17);
		btnModifyPsw.setPosition((getWidth() - bg.getWidth()) / 2 + 130, (getHeight() - bg.getHeight()) / 2 + 17);
		btnLogin.setPosition((getWidth() - bg.getWidth()) / 2 + 169, (getHeight() - bg.getHeight()) / 2 + 57);
		btnExit.setPosition((getWidth() - bg.getWidth()) / 2 + 252, (getHeight() - bg.getHeight()) / 2 + 203);
		txtUna.setPosition((getWidth() - bg.getWidth()) / 2 + 100, (getHeight() - bg.getHeight()) / 2 + 154);
		txtPsw.setPosition((getWidth() - bg.getWidth()) / 2 + 100, (getHeight() - bg.getHeight()) / 2 + 122);
	}
}
