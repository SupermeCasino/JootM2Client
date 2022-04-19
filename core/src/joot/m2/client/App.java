package joot.m2.client;

import com.badlogic.gdx.Game;

import joot.m2.client.scene.ChrSelScene;
import joot.m2.client.scene.GameScene;
import joot.m2.client.scene.LoginScene;
import joot.m2.client.util.AssetUtil;
import joot.m2.client.util.NetworkUtil;

/**
 * 游戏入口
 * 
 * @author linxing
 *
 */
public class App extends Game {
	/**
	 * 游戏场景管理器
	 */
	public static App SceneManager = null;
	/**
	 * 君の名は。
	 */
	public static String MyName = null;
	
	// 登陆场景
	private LoginScene loginScene;
	// 角色选择场景
	private ChrSelScene chrSelScene;
	// 游戏场景
	private GameScene gameScene;

	@Override
	public void create() {
		//Assets.init("D:\\Program Files (x86)\\盛大网络\\热血传奇");
		//Assets.init("D:\\Program Files\\ShengquGames\\Legend of mir");
		//Assets.init("/Users/linxing/m2/盛大网络/热血传奇");
		AssetUtil.init("/Users/linxing/m2/ShengquGames/Legend of mir");
		NetworkUtil.init("ws://127.0.0.1:55842/m2");
		
		SceneManager = this;
		loginScene = new LoginScene();
		chrSelScene = new ChrSelScene();
		gameScene = new GameScene();
		setScreen(loginScene);
	}

	/**
	 * 去到登陆界面
	 */
	public void toLogin() {
		setScreen(loginScene);
	}
	
	/**
	 * 去到角色选择场景
	 */
	public void toChrSel() {
		setScreen(chrSelScene);
	}
	
	/**
	 * 去到游戏场景
	 */
	public void toGame() {
		setScreen(gameScene);
	}
}
