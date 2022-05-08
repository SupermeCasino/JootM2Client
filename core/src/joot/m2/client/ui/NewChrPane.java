package joot.m2.client.ui;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.github.jootnet.m2.core.actor.Occupation;

import joot.m2.client.image.M2Texture;
import joot.m2.client.util.AssetUtil;
import joot.m2.client.util.DrawableUtil;
import joot.m2.client.util.FontUtil;

/**
 * 创建角色
 * 
 * @author linxing
 *
 */
public class NewChrPane extends WidgetGroup {
	private Image bg;
	/** 昵称 */
	private TextField txtName;
	/** 选择战士 */
	private ImageButton btnWarrior;
	/** 选择法师 */
	private ImageButton btnMaster;
	/** 选择道士 */
	private ImageButton btnTaoist;
	/** 选择男 */
	private ImageButton btnMan;
	/** 选择女 */
	private ImageButton btnWoman;
	/** 提交 */
	private ImageButton btnCommit;
	/** 叉叉 */
	private ImageButton btnClose;
	/** 角色动画 */
	private Animation<M2Texture> aniChr1;
	private float deltaAniChr1;
	private Image imgChr1;
	private Occupation occu = Occupation.warrior;
	private byte gender = 0;
	
	@FunctionalInterface
	public interface OperationConsumer {
		void op();
	}
	
	/**
	 * 
	 * @param closeConsumer 面板关闭
	 */
	public NewChrPane(OperationConsumer closeConsumer) {
		AssetUtil.<M2Texture>get(texs -> {
			var texIdx = 0;
			addActor(bg = new Image(texs[texIdx++]));
			
			addActor(btnWarrior = new ImageButton(new ImageButtonStyle(null, new TextureRegionDrawable(texs[texIdx++]), null, null, null, null)));
			addActor(btnMaster = new ImageButton(new ImageButtonStyle(null, new TextureRegionDrawable(texs[texIdx++]), null, null, null, null)));
			addActor(btnTaoist = new ImageButton(new ImageButtonStyle(null, new TextureRegionDrawable(texs[texIdx++]), null, null, null, null)));
			addActor(btnMan = new ImageButton(new ImageButtonStyle(null, new TextureRegionDrawable(texs[texIdx++]), null, null, null, null)));
			addActor(btnWoman = new ImageButton(new ImageButtonStyle(null, new TextureRegionDrawable(texs[texIdx++]), null, null, null, null)));
			addActor(btnCommit = new ImageButton(new ImageButtonStyle(null, new TextureRegionDrawable(texs[texIdx++]), null, null, null, null)));
			addActor(btnClose = new ImageButton(new ImageButtonStyle(null, new TextureRegionDrawable(texs[texIdx++]), null, null, null, null)));
		}, "prguse/73", "prguse/74", "prguse/75", "prguse/76", "prguse/77", "prguse/78", "prguse/62", "prguse/64");
		
		addActor(txtName = new TextField("", new TextFieldStyle(FontUtil.Song_12_all_colored,
				Color.WHITE,
				DrawableUtil.Cursor_White,
				DrawableUtil.Bg_LightGray,
				null)));
		txtName.setWidth(133);
		txtName.setMaxLength(10);
		addActor(imgChr1 = new Image());
		imgChr1.setPosition(152, 300);
		imgChr1.setSize(300, 360);
		
		btnWarrior.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				var occuO = occu;
				occu = Occupation.warrior;
				if (occuO != occu)
					select();
			}
			
		});
		btnMaster.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				var occuO = occu;
				occu = Occupation.master;
				if (occuO != occu)
					select();
			}
			
		});
		btnTaoist.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				var occuO = occu;
				occu = Occupation.taoist;
				if (occuO != occu)
					select();
			}
			
		});
		btnMan.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				var genderO = gender;
				gender = 0;
				if (gender != genderO)
					select();
			}
			
		});
		btnWoman.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				var genderO = gender;
				gender = 1;
				if (gender != genderO)
					select();
			}
			
		});
		btnClose.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				setVisible(false);
				if (closeConsumer != null) closeConsumer.op();
			}
			
		});
		
		select();
	}
	
	private boolean lastVisible = true;
	@Override
	public void act(float delta) {
		if (isVisible() && !lastVisible) {
			getStage().setKeyboardFocus(txtName);
		}
		lastVisible = isVisible();

		if (aniChr1 != null) {
			deltaAniChr1 += delta;
			imgChr1.setDrawable(new TextureRegionDrawable(aniChr1.getKeyFrame(deltaAniChr1)));
		}
		
		super.act(delta);
	}
	
	@Override
	public void layout() {
		bg.setPosition(573, 259);
		txtName.setPosition(646, 555);
		btnWarrior.setPosition(621, 483);
		btnMaster.setPosition(666, 483);
		btnTaoist.setPosition(711, 483);
		btnMan.setPosition(666, 410);
		btnWoman.setPosition(711, 410);
		btnCommit.setPosition(675, 283);
		btnClose.setPosition(820, 623);
	}
	

	// 选中状态的动画序列帧
	private static String[][][] selectAniTexs = new String[][][] {
		// 男
		new String[][] {
			// 战
			IntStream.range(40, 56).mapToObj(i -> "chrsel/" + i).collect(Collectors.toList()).toArray(new String[0]),
			IntStream.range(80, 96).mapToObj(i -> "chrsel/" + i).collect(Collectors.toList()).toArray(new String[0]),
			IntStream.range(120, 136).mapToObj(i -> "chrsel/" + i).collect(Collectors.toList()).toArray(new String[0])
		},
		new String[][] {
			IntStream.range(160, 176).mapToObj(i -> "chrsel/" + i).collect(Collectors.toList()).toArray(new String[0]),
			IntStream.range(200, 216).mapToObj(i -> "chrsel/" + i).collect(Collectors.toList()).toArray(new String[0]),
			IntStream.range(240, 256).mapToObj(i -> "chrsel/" + i).collect(Collectors.toList()).toArray(new String[0])
		}
	};
	
	private void select() {
		deltaAniChr1 = 0;
		aniChr1 = null;
		imgChr1.setDrawable(null);

		AssetUtil.<M2Texture>get(texs -> {
			aniChr1 = new Animation<M2Texture>(0.15f, texs);
			aniChr1.setPlayMode(PlayMode.LOOP);
		}, selectAniTexs[gender][occu.ordinal()]);
	}
}
