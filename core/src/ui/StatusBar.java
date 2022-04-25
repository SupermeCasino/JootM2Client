package ui;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;

import joot.m2.client.image.M2Texture;
import joot.m2.client.util.AssetUtil;

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
	}
}
