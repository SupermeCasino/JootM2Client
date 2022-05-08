package joot.m2.client.scene;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import joot.m2.client.image.M2Texture;
import joot.m2.client.ui.NewUserPane;
import joot.m2.client.ui.LoginPane;
import joot.m2.client.ui.ModifyPswPane;
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
	/** 修改密码 */
	private ModifyPswPane modifyPswPane;
	
	@Override
	public void show() {
		stage = new Stage();
		
		AssetUtil.<M2Texture>get(tex -> {
			stage.addActor(new Image(tex));
		}, "ui1/02850");
		
		loginPane = new LoginPane(() -> {
			createPane.setVisible(true);
		}, () -> {
			modifyPswPane.setVisible(true);
		});
		loginPane.setFillParent(true);
		stage.addActor(loginPane);
		
		createPane = new NewUserPane(() -> {
			createPane.setVisible(false);
			loginPane.focusInput();
		});
		createPane.setFillParent(true);
		createPane.setVisible(false);
		stage.addActor(createPane);
		
		modifyPswPane = new ModifyPswPane(() -> {
			modifyPswPane.setVisible(false);
			loginPane.focusInput();
		});
		modifyPswPane.setFillParent(true);
		modifyPswPane.setVisible(false);
		stage.addActor(modifyPswPane);
		
		loginPane.focusInput();
	    
		super.show();
	}
}