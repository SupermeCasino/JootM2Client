package joot.m2.client.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;

import joot.m2.client.App;
import joot.m2.client.net.MessageType;
import joot.m2.client.net.messages.LoginResp;
import joot.m2.client.util.NetworkUtil;

/**
 * 登陆场景
 * 
 * @author linxing
 *
 */
public final class LoginScene extends BaseScene {
	private TextField txtUna;
	private TextField txtPassword;
	private Label lblLoginButton;
	private Label lblTip;
	private boolean toGame = false;
	
	@Override
	public void show() {
		Gdx.graphics.setWindowedMode(800, 600);
		
		stage = new Stage();
		
	    var table = new Table();
	    table.setFillParent(true);
	    stage.addActor(table);
	    
	    var bmpFont = new BitmapFont();
	    var lblUna = new Label("username:", new Label.LabelStyle(bmpFont, Color.GREEN));
	    
	    txtUna = new TextField("", new TextField.TextFieldStyle(bmpFont, Color.RED, null, null, null));
	    txtUna.setMessageText("legendarycici/ll01131458");
	    var lblPassword = new Label("password:", new Label.LabelStyle(bmpFont, Color.GREEN));
	    txtPassword = new TextField("123456", new TextField.TextFieldStyle(bmpFont, Color.RED, null, null, null));
		txtPassword.setPasswordCharacter('*');
	    txtPassword.setPasswordMode(true);
	    
	    lblLoginButton = new Label("loginin", new Label.LabelStyle(bmpFont, Color.GREEN));
	    lblLoginButton.addListener(new InputListener() {
	    	@Override
	    	public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
	    		NetworkUtil.sendLoginReq(txtUna.getText(), txtPassword.getText());
	    	}
	    	@Override
	    	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
	    		return true;
	    	}
	    });
	    
	    table.columnDefaults(0).align(Align.right).space(10);
	    table.columnDefaults(1).width(300).space(10);
	    table.add(lblUna);
	    table.add(txtUna);
	    table.row();
	    table.add(lblPassword);
	    table.add(txtPassword);
	    table.row();
	    table.add(lblLoginButton).colspan(2).align(Align.center);
	    table.row();
	    
	    lblTip = new Label("", new Label.LabelStyle(bmpFont, Color.RED));
	    table.add(lblTip).colspan(2).align(Align.center);
	    
		super.show();
	}
	
	@Override
	public void render(float delta) {
		if (toGame) {
			// 当前帧去到游戏主界面
			toGame = false;
			App.SceneManager.toGame();
			return;
		}
		for (var msg : NetworkUtil.getRecvMsgList()) {
			if (msg.type() == MessageType.LOGIN_RESP) {
				var loginResp = (LoginResp) msg;
				var code = loginResp.code();
				if (code == 0) {
					//App.SceneManager.toChrSel();
					GameScene.MyName = loginResp.roles()[0].name;
					// 下一帧去到游戏主届满
					toGame = true;
					// 先清屏，避免重设窗体大小时模糊
					Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
					return;
				}
				var tip = loginResp.serverTip();
				if (tip == null) {
					switch (loginResp.code()) {
					case 1:
						tip = "用户名或密码错误";
						break;
					case 2:
						tip = "用户不存在";
						break;
					}
				}
				lblTip.setText(tip);
			}
		}
		super.render(delta);
	}
}