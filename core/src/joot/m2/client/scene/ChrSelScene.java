package joot.m2.client.scene;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * 角色选择场景
 * 
 * @author linxing
 *
 */
public final class ChrSelScene extends BaseScene {
	/** 选择第一个角色 */
	private ImageButton btnSelectChr1;
	/** 选择第二个角色 */
	private ImageButton btnSelectChr2;
	/** 开始 */
	private ImageButton btnEnter;
	/** 创建人物 */
	private ImageButton btnCreate;
	/** 恢复人物 */
	private ImageButton btnReback;
	/** 删除人物 */
	private ImageButton btnRemove;
	/** 退出 */
	private ImageButton btnExit;

	@Override
	public void show() {
		
		stage = new Stage();
		stage.addActor(new Image(new Texture("ui1/02851.png")));
		stage.addActor(btnSelectChr1 = new ImageButton(new ImageButtonStyle()));
		stage.addActor(btnSelectChr2 = new ImageButton(new ImageButtonStyle()));
		stage.addActor(btnEnter = new ImageButton(new ImageButtonStyle()));
		stage.addActor(btnCreate = new ImageButton(new ImageButtonStyle()));
		stage.addActor(btnReback = new ImageButton(new ImageButtonStyle()));
		stage.addActor(btnRemove = new ImageButton(new ImageButtonStyle()));
		stage.addActor(btnExit = new ImageButton(new ImageButtonStyle()));
		
		btnSelectChr1.getStyle().over = new TextureRegionDrawable(new Texture("ui1/02854.png"));
		btnSelectChr2.getStyle().over = new TextureRegionDrawable(new Texture("ui1/02856.png"));
		btnEnter.getStyle().over = new TextureRegionDrawable(new Texture("ui1/02852.png"));
		btnCreate.getStyle().over = new TextureRegionDrawable(new Texture("ui1/02858.png"));
		btnCreate.getStyle().up = new TextureRegionDrawable(new Texture("ui1/02859.png"));
		btnReback.getStyle().over = new TextureRegionDrawable(new Texture("ui1/02860.png"));
		btnReback.getStyle().up = new TextureRegionDrawable(new Texture("ui1/02861.png"));
		btnRemove.getStyle().over = new TextureRegionDrawable(new Texture("ui1/02862.png"));
		btnRemove.getStyle().up = new TextureRegionDrawable(new Texture("ui1/02863.png"));
		btnExit.getStyle().over = new TextureRegionDrawable(new Texture("ui1/02864.png"));
		btnExit.getStyle().up = new TextureRegionDrawable(new Texture("ui1/02865.png"));
		
		btnSelectChr1.setPosition(176, 134);
		btnSelectChr2.setPosition(887, 136);
		btnEnter.setPosition(476, 138);
		btnCreate.setPosition(448, 106);
		btnReback.setPosition(448, 77);
		btnRemove.setPosition(448, 49);
		btnExit.setPosition(487, 21);
		
		btnSelectChr1.setSize(102, 45);
		btnSelectChr2.setSize(102, 46);
		btnEnter.setSize(88, 41);
		btnCreate.setSize(145, 25);
		btnReback.setSize(145, 25);
		btnRemove.setSize(145, 25);
		btnExit.setSize(67, 24);
		
		super.show();
	}
	
}