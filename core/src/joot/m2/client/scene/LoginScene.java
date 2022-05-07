package joot.m2.client.scene;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import joot.m2.client.image.M2Texture;
import joot.m2.client.ui.NewUserPane;
import joot.m2.client.ui.LoginPane;
import joot.m2.client.util.AssetUtil;

/**
 * 登陆框
 * 
 * @author linxing
 *
 */
public final class LoginScene extends BaseScene {
	/** 登录框 */
	private LoginPane loginPane;
	/** 创建账号 */
	private NewUserPane createPane;
	
	@Override
	public void show() {
		stage = new Stage();
		
		AssetUtil.<M2Texture>get(tex -> {
			stage.addActor(new Image(tex));
		}, "ui1/02850");
		
		loginPane = new LoginPane(() -> {
			createPane.setVisible(true);
		}, null);
		loginPane.setFillParent(true);
		stage.addActor(loginPane);
		
		createPane = new NewUserPane(() -> {
			createPane.setVisible(false);
			loginPane.focusInput();
		});
		createPane.setFillParent(true);
		createPane.setVisible(false);
		stage.addActor(createPane);
		
		loginPane.focusInput();
	    
		super.show();
	}
}