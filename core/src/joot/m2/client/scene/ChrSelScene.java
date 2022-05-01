package joot.m2.client.scene;

import javax.swing.JOptionPane;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.github.jootnet.m2.core.net.MessageType;
import com.github.jootnet.m2.core.net.messages.EnterResp;

import joot.m2.client.App;
import joot.m2.client.util.FontUtil;
import joot.m2.client.util.NetworkUtil;

/**
 * 角色选择场景
 * 
 * @author linxing
 *
 */
public final class ChrSelScene extends BaseScene {
	private String curName;
	
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
	/** 标题 */
	private Label lblTitle;
	/** 第一个角色的昵称 */
	private Label lblName1;
	/** 第一个角色的等级 */
	private Label lblLevel1;
	/** 第一个角色的职业 */
	private Label lblOccu1;
	/** 第二个角色的昵称 */
	private Label lblName2;
	/** 第二个角色的等级 */
	private Label lblLevel2;
	/** 第二个角色的职业 */
	private Label lblOccu2;

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
		stage.addActor(lblTitle = new Label("将唐传奇", new LabelStyle(FontUtil.Song_12_all_colored, Color.WHITE)));
		curName = App.LastName;
		if (App.Roles != null && App.Roles.length > 0) {
			stage.addActor(lblName1 = new Label(App.Roles[0].name, new LabelStyle(FontUtil.Song_12_all_colored, Color.WHITE)));
			stage.addActor(lblLevel1 = new Label(String.valueOf(App.Roles[0].level), new LabelStyle(FontUtil.Song_12_all_colored, Color.WHITE)));
			String occuString = App.Roles[0].type == 0 ? "战士" : App.Roles[0].type == 1 ? "法师" : App.Roles[0].type == 2 ? "道士" : "刺客";
			stage.addActor(lblOccu1 = new Label(occuString, new LabelStyle(FontUtil.Song_12_all_colored, Color.WHITE)));
			if (curName == null) {
				curName = App.Roles[0].name;
			}
		}
		if (App.Roles != null && App.Roles.length > 1) {
			stage.addActor(lblName2 = new Label(App.Roles[1].name, new LabelStyle(FontUtil.Song_12_all_colored, Color.WHITE)));
			stage.addActor(lblLevel2 = new Label(String.valueOf(App.Roles[1].level), new LabelStyle(FontUtil.Song_12_all_colored, Color.WHITE)));
			String occuString = App.Roles[1].type == 0 ? "战士" : App.Roles[1].type == 1 ? "法师" : App.Roles[1].type == 2 ? "道士" : "刺客";
			stage.addActor(lblOccu2 = new Label(occuString, new LabelStyle(FontUtil.Song_12_all_colored, Color.WHITE)));
		}
		
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
		lblTitle.setPosition(464, 744);
		if (App.Roles != null && App.Roles.length > 0) {
			lblName1.setPosition(154, 112);
			lblLevel1.setPosition(154, 74);
			lblOccu1.setPosition(154, 36);
		}
		if (App.Roles != null && App.Roles.length > 1) {
			lblName2.setPosition(862, 108);
			lblLevel2.setPosition(862, 70);
			lblOccu2.setPosition(862, 32);
		}
		
		btnSelectChr1.setSize(102, 45);
		btnSelectChr2.setSize(102, 46);
		btnEnter.setSize(88, 41);
		btnCreate.setSize(145, 25);
		btnReback.setSize(145, 25);
		btnRemove.setSize(145, 25);
		btnExit.setSize(67, 24);
		lblTitle.setWidth(108);
		lblTitle.setAlignment(Align.center);
		
		btnEnter.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (curName != null) {
					NetworkUtil.sendEnterGame(curName);
				}
			}
			
		});
		btnSelectChr1.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (App.Roles != null && App.Roles.length > 0) {
					curName = App.Roles[0].name;
					App.LastName = App.Roles[0].name;
				}	
			}
			
		});
		btnSelectChr2.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (App.Roles != null && App.Roles.length > 1) {
					curName = App.Roles[1].name;
					App.LastName = App.Roles[1].name;
				}	
			}
			
		});
		
		super.show();
	}
	
	@Override
	public void render(float delta) {

		NetworkUtil.recv(msg -> {
			if (msg.type() == MessageType.ENTER_RESP) {
				var enterResp = (EnterResp) msg;
				if (enterResp.forbidTip != null) {
					JOptionPane.showMessageDialog(null, enterResp.forbidTip);
				} else {
					if (!enterResp.cbi.name.equals(App.LastName)) return false; // ??
					App.Chr = enterResp.cbi;
					App.toGame();
				}
				return true;
			}
			return false;
		});
		
		super.render(delta);
	}
}