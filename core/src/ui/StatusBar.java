package ui;

import java.util.stream.IntStream;

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
		while(!AssetUtil.isLoaded("newopui/0")) AssetUtil.update();
		while(!AssetUtil.isLoaded("newopui/1")) AssetUtil.update();
		while(!AssetUtil.isLoaded("newopui/2")) AssetUtil.update();
		while(!AssetUtil.isLoaded("newopui/3")) AssetUtil.update();
		addActor(new Image(AssetUtil.<M2Texture>get("newopui/0"))); // 左侧生命值法力值的背景图
		addActor(funAreaBg = new Image(AssetUtil.<M2Texture>get("newopui/1")));
		addActor(quikAreaBg = new Image(AssetUtil.<M2Texture>get("newopui/2")));
		quikAreaBg.setY(157);
		addActor(powerBg = new Image(AssetUtil.<M2Texture>get("newopui/3")));
		addActor(chatBox = new ChatBox());
		IntStream.range(4, 20).forEach(i -> {
			while(!AssetUtil.isLoaded("prguse/" + i)) AssetUtil.update();
		});
		IntStream.range(128, 140).forEach(i -> {
			while(!AssetUtil.isLoaded("prguse/" + i)) AssetUtil.update();
		});
		IntStream.range(34, 38).forEach(i -> {
			while(!AssetUtil.isLoaded("prguse3/" + i)) AssetUtil.update();
		});
		IntStream.range(280, 290).forEach(i -> {
			while(!AssetUtil.isLoaded("prguse3/" + i)) AssetUtil.update();
		});
		IntStream.range(460, 462).forEach(i -> {
			while(!AssetUtil.isLoaded("prguse3/" + i)) AssetUtil.update();
		});
		while(!AssetUtil.isLoaded("prguse3/297")) AssetUtil.update();
		addActor(chkMsg1 = new CheckBox(null, new CheckBoxStyle(new TextureRegionDrawable(AssetUtil.<M2Texture>get("prguse3/280")),
				new TextureRegionDrawable(AssetUtil.<M2Texture>get("prguse3/281")), FontUtil.Default, null)));
		chkMsg1.setPosition(176, 116);
		addActor(chkMsg2 = new CheckBox(null, new CheckBoxStyle(new TextureRegionDrawable(AssetUtil.<M2Texture>get("prguse3/282")),
				new TextureRegionDrawable(AssetUtil.<M2Texture>get("prguse3/283")), FontUtil.Default, null)));
		chkMsg2.setPosition(176, 96);
		addActor(chkMsg3 = new CheckBox(null, new CheckBoxStyle(new TextureRegionDrawable(AssetUtil.<M2Texture>get("prguse3/284")),
				new TextureRegionDrawable(AssetUtil.<M2Texture>get("prguse3/285")), FontUtil.Default, null)));
		chkMsg3.setPosition(176, 76);
		addActor(chkMsg4 = new CheckBox(null, new CheckBoxStyle(new TextureRegionDrawable(AssetUtil.<M2Texture>get("prguse3/286")),
				new TextureRegionDrawable(AssetUtil.<M2Texture>get("prguse3/287")), FontUtil.Default, null)));
		chkMsg4.setPosition(176, 56);
		addActor(chkMsg5 = new CheckBox(null, new CheckBoxStyle(new TextureRegionDrawable(AssetUtil.<M2Texture>get("prguse3/288")),
				new TextureRegionDrawable(AssetUtil.<M2Texture>get("prguse3/289")), FontUtil.Default, null)));
		chkMsg5.setPosition(176, 36);
		addActor(btnMMap = new ImageButton(new ImageButtonStyle(new TextureRegionDrawable(AssetUtil.<M2Texture>get("prguse/130")),
				new TextureRegionDrawable(AssetUtil.<M2Texture>get("prguse/131")), null, null, null, null)));
		btnMMap.setPosition(210, 134);
		addActor(btnTrade = new ImageButton(new ImageButtonStyle(new TextureRegionDrawable(AssetUtil.<M2Texture>get("prguse/132")),
				new TextureRegionDrawable(AssetUtil.<M2Texture>get("prguse/133")), null, null, null, null)));
		btnTrade.setPosition(240, 134);
		addActor(btnGuild = new ImageButton(new ImageButtonStyle(new TextureRegionDrawable(AssetUtil.<M2Texture>get("prguse/134")),
				new TextureRegionDrawable(AssetUtil.<M2Texture>get("prguse/135")), null, null, null, null)));
		btnGuild.setPosition(270, 134);
		addActor(btnTeam = new ImageButton(new ImageButtonStyle(new TextureRegionDrawable(AssetUtil.<M2Texture>get("prguse/128")),
				new TextureRegionDrawable(AssetUtil.<M2Texture>get("prguse/129")), null, null, null, null)));
		btnTeam.setPosition(300, 134);
		addActor(btnFriend = new ImageButton(new ImageButtonStyle(new TextureRegionDrawable(AssetUtil.<M2Texture>get("prguse3/34")),
				new TextureRegionDrawable(AssetUtil.<M2Texture>get("prguse3/35")), null, null, null, null)));
		btnFriend.setPosition(330, 134);
		addActor(btnTalkHistory = new ImageButton(new ImageButtonStyle(new TextureRegionDrawable(AssetUtil.<M2Texture>get("prguse3/36")),
				new TextureRegionDrawable(AssetUtil.<M2Texture>get("prguse3/37")), null, null, null, null)));
		btnTalkHistory.setPosition(360, 135);
		addActor(btnRankList = new ImageButton(new ImageButtonStyle(new TextureRegionDrawable(AssetUtil.<M2Texture>get("prguse3/460")),
				new TextureRegionDrawable(AssetUtil.<M2Texture>get("prguse3/461")), null, null, null, null)));
		btnRankList.setPosition(390, 134);
		addActor(btnLogout = new ImageButton(new ImageButtonStyle(new TextureRegionDrawable(AssetUtil.<M2Texture>get("prguse/136")),
				new TextureRegionDrawable(AssetUtil.<M2Texture>get("prguse/137")), null, null, null, null)));
		btnLogout.setPosition(754, 134);
		addActor(btnExit = new ImageButton(new ImageButtonStyle(new TextureRegionDrawable(AssetUtil.<M2Texture>get("prguse/138")),
				new TextureRegionDrawable(AssetUtil.<M2Texture>get("prguse/139")), null, null, null, null)));
		btnExit.setPosition(784, 134);
		addActor(btnHum = new ImageButton(new ImageButtonStyle()));
		btnHum.getStyle().over = new TextureRegionDrawable(AssetUtil.<M2Texture>get("prguse/8"));
		btnHum.setSize(24, 24);
		btnHum.setPosition(642, 166);
		addActor(btnBag = new ImageButton(new ImageButtonStyle()));
		btnBag.getStyle().over = new TextureRegionDrawable(AssetUtil.<M2Texture>get("prguse/9"));
		btnBag.setSize(24, 22);
		btnBag.setPosition(682, 188);
		addActor(btnSkill = new ImageButton(new ImageButtonStyle()));
		btnSkill.getStyle().over = new TextureRegionDrawable(AssetUtil.<M2Texture>get("prguse/10"));
		btnSkill.setSize(24, 23);
		btnSkill.setPosition(722, 207);
		addActor(btnSound = new ImageButton(new ImageButtonStyle()));
		btnSound.getStyle().over = new TextureRegionDrawable(AssetUtil.<M2Texture>get("prguse/11"));
		btnSound.setSize(24, 23);
		btnSound.setPosition(762, 217);
		addActor(btnShop = new ImageButton(new ImageButtonStyle()));
		btnShop.getStyle().down = new TextureRegionDrawable(AssetUtil.<M2Texture>get("prguse3/297"));
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
