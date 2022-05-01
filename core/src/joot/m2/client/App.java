package joot.m2.client;

import com.badlogic.gdx.Game;
import com.github.jootnet.m2.core.actor.ChrBasicInfo;
import com.github.jootnet.m2.core.net.messages.LoginResp;

import joot.m2.client.scene.ChrSelScene;
import joot.m2.client.scene.GameScene;
import joot.m2.client.scene.LoginScene;

/**
 * 游戏入口
 * 
 * @author linxing
 *
 */
public class App extends Game {
	private static App me = null;
	/** 账号已有的角色列表 */
	public static LoginResp.Role[] Roles;
	/** 上次登录的昵称 */
	public static String LastName;
	/** 当前进入游戏的角色 */
	public static ChrBasicInfo Chr;
	/**
	 * 是否平滑移动
	 */
	public static boolean SmoothMoving = false;
	
	// 登陆场景
	private LoginScene loginScene;
	// 角色选择场景
	private ChrSelScene chrSelScene;
	// 游戏场景
	private GameScene gameScene;

	public App() {
		me = this;
	}
	
	@Override
	public void create() {
		loginScene = new LoginScene();
		chrSelScene = new ChrSelScene();
		gameScene = new GameScene();
		setScreen(loginScene);
	}

	/**
	 * 去到登陆界面
	 */
	public static void toLogin() {
		me.setScreen(me.loginScene);
	}
	
	/**
	 * 去到角色选择场景
	 */
	public static void toChrSel() {
		me.setScreen(me.chrSelScene);
	}
	
	/**
	 * 去到游戏场景
	 */
	public static void toGame() {
		me.setScreen(me.gameScene);
	}
}
