package joot.m2.client.scene;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import joot.m2.client.ui.LoginPane;

/**
 * 登陆场景
 * 
 * @author linxing
 *
 */
public final class LoginScene extends BaseScene {
	/** 登录框 */
	private LoginPane loginPane;
	
	@Override
	public void show() {
		stage = new Stage();
		
		stage.addActor(new Image(new Texture("ui1/02850.png")));
		
		loginPane = new LoginPane(null, null);
		loginPane.setFillParent(true);
		stage.addActor(loginPane);
		loginPane.focusInput();
	    
		super.show();
	}
}