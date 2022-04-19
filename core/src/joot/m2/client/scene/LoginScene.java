package joot.m2.client.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
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
	
	@Override
	public void show() {
		stage = new Stage();
		
	    var table = new Table();
	    table.setFillParent(true);
	    stage.addActor(table);
	    
	    var generator = new FreeTypeFontGenerator(Gdx.files.absolute("/System/Library/Fonts/STHeiti Light.ttc"));
	    var parameter = new FreeTypeFontParameter();
	    parameter.size = 18;
	    parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "用户名密码登陆：林星二条用户名不存在或密码错误";
	    BitmapFont font18 = generator.generateFont(parameter); // font size 12 pixels
	    generator.dispose(); // don't forget to dispose to avoid memory leaks!
	    
	    var lblUna = new Label("用户名：", new Label.LabelStyle(font18, Color.GREEN));

	    
	    txtUna = new TextField("", new TextField.TextFieldStyle(font18, Color.RED, null, null, null));
	    txtUna.setMessageText("legendarycici/ll01131458");
	    var lblPassword = new Label("密码：", new Label.LabelStyle(font18, Color.GREEN));
	    txtPassword = new TextField("123456", new TextField.TextFieldStyle(font18, Color.RED, null, null, null));
	    txtPassword.setPasswordMode(true);
	    
	    lblLoginButton = new Label("登陆", new Label.LabelStyle(font18, Color.GREEN));
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
	    
	    lblTip = new Label("", new Label.LabelStyle(font18, Color.RED));
	    table.add(lblTip).colspan(2).align(Align.center);
	    
		super.show();
	}
	
	@Override
	public void render(float delta) {
		for (var msg : NetworkUtil.getRecvMsgList()) {
			if (msg.type() == MessageType.LOGIN_RESP) {
				var loginResp = (LoginResp) msg;
				var code = loginResp.code();
				if (code == 0) {
					//App.SceneManager.toChrSel();
					App.MyName = loginResp.roles()[0].name;
					App.SceneManager.toGame();
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