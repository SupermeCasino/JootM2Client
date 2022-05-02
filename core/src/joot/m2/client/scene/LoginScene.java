package joot.m2.client.scene;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import joot.m2.client.image.M2Texture;
import joot.m2.client.ui.LoginPane;
import joot.m2.client.util.AssetUtil;

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
		
		AssetUtil.<M2Texture>get(tex -> {
			stage.addActor(new Image(tex));
		}, "ui1/02850");
		
		loginPane = new LoginPane(null, null);
		loginPane.setFillParent(true);
		stage.addActor(loginPane);
		loginPane.focusInput();
	    
		super.show();
	}
}