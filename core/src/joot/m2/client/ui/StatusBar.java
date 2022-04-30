package joot.m2.client.ui;

import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import joot.m2.client.image.M2Texture;
import joot.m2.client.util.AssetUtil;
import joot.m2.client.util.FontUtil;

/**
 * 
 * 游戏界面下方的状态栏
 * 
 * @author linxing
 *
 */
public final class StatusBar extends WidgetGroup {
	/** 右侧功能区背景图 */
	private Image funAreaBg;
	/** 药品随机卷使用栏背景图 */
	private Image quikAreaBg;
	/** 能量条背景图 */
	private Image powerBg;
	/** 聊天框 */
	private ChatBox chatBox;
	/** 世界聊天开关？ */
	private CheckBox chkMsg1;
	/** 行会聊天开关？ */
	private CheckBox chkMsg2;
	/** 队伍聊天开关？ */
	private CheckBox chkMsg3;
	/** 私聊开关？ */
	private CheckBox chkMsg4;
	/** 地图聊天开关？ */
	private CheckBox chkMsg5;
	/** 小地图开关 */
	private ImageButton btnMMap;
	/** 交易按钮 */
	private ImageButton btnTrade;
	/** 行会按钮 */
	private ImageButton btnGuild;
	/** 队伍按钮 */
	private ImageButton btnTeam;
	/** 好友列表 */
	private ImageButton btnFriend;
	/** 聊天记录 */
	private ImageButton btnTalkHistory;
	/** 排行榜 */
	private ImageButton btnRankList;
	/** 小退 */
	private ImageButton btnLogout;
	/** 大退 */
	private ImageButton btnExit;
	/** 打开人物属性界面 */
	private ImageButton btnHum;
	/** 打开背包 */
	private ImageButton btnBag;
	/** 打开技能界面 */
	private ImageButton btnSkill;
	/** 开关声音 */
	private ImageButton btnSound;
	/** 打开商店 */
	private ImageButton btnShop;

	public StatusBar() {
		AssetUtil.<M2Texture>get(texs -> {
			int texIdx = 0;
			addActor(new Image(texs[texIdx++])); // 左侧生命值法力值的背景图
			addActor(funAreaBg = new Image(texs[texIdx++]));
			addActor(quikAreaBg = new Image(texs[texIdx++]));
			addActor(powerBg = new Image(texs[texIdx++]));
			addActor(chatBox = new ChatBox());
			addActor(chkMsg1 = new CheckBox(null, new CheckBoxStyle(new TextureRegionDrawable(texs[texIdx++]),
					new TextureRegionDrawable(texs[texIdx++]), FontUtil.Default, null)));
			addActor(chkMsg2 = new CheckBox(null, new CheckBoxStyle(new TextureRegionDrawable(texs[texIdx++]),
					new TextureRegionDrawable(texs[texIdx++]), FontUtil.Default, null)));
			addActor(chkMsg3 = new CheckBox(null, new CheckBoxStyle(new TextureRegionDrawable(texs[texIdx++]),
					new TextureRegionDrawable(texs[texIdx++]), FontUtil.Default, null)));
			addActor(chkMsg4 = new CheckBox(null, new CheckBoxStyle(new TextureRegionDrawable(texs[texIdx++]),
					new TextureRegionDrawable(texs[texIdx++]), FontUtil.Default, null)));
			addActor(chkMsg5 = new CheckBox(null, new CheckBoxStyle(new TextureRegionDrawable(texs[texIdx++]),
					new TextureRegionDrawable(texs[texIdx++]), FontUtil.Default, null)));
			addActor(btnMMap = new ImageButton(new ImageButtonStyle(new TextureRegionDrawable(texs[texIdx++]),
					new TextureRegionDrawable(texs[texIdx++]), null, null, null, null)));
			addActor(btnTrade = new ImageButton(new ImageButtonStyle(new TextureRegionDrawable(texs[texIdx++]),
					new TextureRegionDrawable(texs[texIdx++]), null, null, null, null)));
			addActor(btnGuild = new ImageButton(new ImageButtonStyle(new TextureRegionDrawable(texs[texIdx++]),
					new TextureRegionDrawable(texs[texIdx++]), null, null, null, null)));
			
			addActor(btnTeam = new ImageButton(new ImageButtonStyle(new TextureRegionDrawable(texs[texIdx++]),
					new TextureRegionDrawable(texs[texIdx++]), null, null, null, null)));
			addActor(btnFriend = new ImageButton(new ImageButtonStyle(new TextureRegionDrawable(texs[texIdx++]),
					new TextureRegionDrawable(texs[texIdx++]), null, null, null, null)));
			addActor(btnTalkHistory = new ImageButton(new ImageButtonStyle(new TextureRegionDrawable(texs[texIdx++]),
					new TextureRegionDrawable(texs[texIdx++]), null, null, null, null)));
			addActor(btnRankList = new ImageButton(new ImageButtonStyle(new TextureRegionDrawable(texs[texIdx++]),
					new TextureRegionDrawable(texs[texIdx++]), null, null, null, null)));
			addActor(btnLogout = new ImageButton(new ImageButtonStyle(new TextureRegionDrawable(texs[texIdx++]),
					new TextureRegionDrawable(texs[texIdx++]), null, null, null, null)));
			addActor(btnExit = new ImageButton(new ImageButtonStyle(new TextureRegionDrawable(texs[texIdx++]),
					new TextureRegionDrawable(texs[texIdx++]), null, null, null, null)));
			addActor(btnHum = new ImageButton(new ImageButtonStyle()));
			btnHum.getStyle().over = new TextureRegionDrawable(texs[texIdx++]);
			addActor(btnBag = new ImageButton(new ImageButtonStyle()));
			btnBag.getStyle().over = new TextureRegionDrawable(texs[texIdx++]);
			addActor(btnSkill = new ImageButton(new ImageButtonStyle()));
			btnSkill.getStyle().over = new TextureRegionDrawable(texs[texIdx++]);
			addActor(btnSound = new ImageButton(new ImageButtonStyle()));
			btnSound.getStyle().over = new TextureRegionDrawable(texs[texIdx++]);
			addActor(btnShop = new ImageButton(new ImageButtonStyle()));
			btnShop.getStyle().down = new TextureRegionDrawable(texs[texIdx++]);
		}
				, "newopui/0"
				, "newopui/1"
				, "newopui/2"
				, "newopui/3"
				, "prguse3/280"
				, "prguse3/281"
				, "prguse3/282"
				, "prguse3/283"
				, "prguse3/284"
				, "prguse3/285"
				, "prguse3/286"
				, "prguse3/287"
				, "prguse3/288"
				, "prguse3/289"
				, "prguse/130"
				, "prguse/131"
				, "prguse/132"
				, "prguse/133"
				, "prguse/134"
				, "prguse/135"
				, "prguse/128"
				, "prguse/129"
				, "prguse3/34"
				, "prguse3/35"
				, "prguse3/36"
				, "prguse3/37"
				, "prguse3/460"
				, "prguse3/461"
				, "prguse/136"
				, "prguse/137"
				, "prguse/138"
				, "prguse/139"
				, "prguse/8"
				, "prguse/9"
				, "prguse/10"
				, "prguse/11"
				, "prguse3/297");
		
		quikAreaBg.setY(157);
		chkMsg1.setPosition(176, 116);
		chkMsg2.setPosition(176, 96);
		chkMsg3.setPosition(176, 76);
		chkMsg4.setPosition(176, 56);
		chkMsg5.setPosition(176, 36);
		btnMMap.setPosition(210, 134);
		btnTrade.setPosition(240, 134);
		btnGuild.setPosition(270, 134);
		btnTeam.setPosition(300, 134);
		btnFriend.setPosition(330, 134);
		btnTalkHistory.setPosition(360, 135);
		btnRankList.setPosition(390, 134);
		btnLogout.setPosition(754, 134);
		btnExit.setPosition(784, 134);
		btnHum.setSize(24, 24);
		btnHum.setPosition(642, 166);
		btnBag.setSize(24, 22);
		btnBag.setPosition(682, 188);
		btnSkill.setSize(24, 23);
		btnSkill.setPosition(722, 207);
		btnSound.setSize(24, 23);
		btnSound.setPosition(762, 217);
		btnShop.setSize(28, 28);
		btnShop.setPosition(750, 19);
	}
	
	/*@Override
	public Actor hit(float x, float y, boolean touchable) {
		// TODO 把镂空的地方返回false，鼠标点选之后可以走跑
		return super.hit(x, y, touchable);
	}*/
	
	/**
	 * 将焦点给到输入框
	 */
	public void focusInput() {
		getStage().setKeyboardFocus(chatBox.txtChat);
	}

	@Override
	public void layout() {
		funAreaBg.setX(getWidth() - funAreaBg.getWidth());
		quikAreaBg.setX((getWidth() - quikAreaBg.getWidth()) / 2);
		powerBg.setX(getWidth() - funAreaBg.getWidth() + 6);
		chatBox.setBounds(194, 0, getWidth() - 388, 157);
		btnHum.setX(getWidth() - 157);
		btnBag.setX(getWidth() - 118);
		btnSkill.setX(getWidth() - 78);
		btnSound.setX(getWidth() - 36);
		btnShop.setX(getWidth() - 47);
	}
}
