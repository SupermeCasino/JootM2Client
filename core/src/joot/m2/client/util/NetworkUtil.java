package joot.m2.client.util;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.WebSockets;
import com.github.jootnet.m2.core.actor.ChrBasicInfo;
import com.github.jootnet.m2.core.actor.Occupation;
import com.github.jootnet.m2.core.net.Message;
import com.github.jootnet.m2.core.net.MessageType;
import com.github.jootnet.m2.core.net.Messages;
import com.github.jootnet.m2.core.net.messages.DeleteChrReq;
import com.github.jootnet.m2.core.net.messages.EnterReq;
import com.github.jootnet.m2.core.net.messages.KickedOut;
import com.github.jootnet.m2.core.net.messages.LoginReq;
import com.github.jootnet.m2.core.net.messages.LogoutReq;
import com.github.jootnet.m2.core.net.messages.ModifyPswReq;
import com.github.jootnet.m2.core.net.messages.NewChrReq;
import com.github.jootnet.m2.core.net.messages.NewUserReq;
import com.github.jootnet.m2.core.net.messages.OutReq;
import com.github.jootnet.m2.core.net.messages.SysInfo;

import joot.m2.client.App;

/**
 * 网络交互工具类
 * 
 * @author linxing
 *
 */
public final class NetworkUtil {

    /** 接受到的数据 */
	private static List<Message> recvMsgList = new ArrayList<>();
	/** 启用保活的标志 */
	private static boolean keepAliveFlag;
	/** 上次接收到数据的时间 */
	private static long lastRecvTime;
	/** 上一次发送数据的时间 */
	private static long lastSendTime;
    
    @FunctionalInterface
    public interface MessageConsumer {
    	boolean recv(Message msg);
    }
    
    /**
     * 接受并处理消息
     * <br>
     * 如果不是自己能处理的消息，返回false
     * 
     * @param consumer 消息消费者
     */
    public static void recv(MessageConsumer consumer) {
		synchronized (recvMsgList) {
	    	var recvMsgList_ = new ArrayList<Message>();
			recvMsgList_.addAll(recvMsgList);
			recvMsgList.clear();
			for (var msg : recvMsgList_) {
				if (!consumer.recv(msg)) recvMsgList.add(msg);
			}
		}
		if (keepAliveFlag && System.currentTimeMillis() - lastSendTime > 15 * 1000) {
			if (ws != null) {
				try {
					ws.send("PING");
				} catch (Exception ex) { }
			}
		}
		if (System.currentTimeMillis() - lastRecvTime > 60 * 1000) {
			// 一分钟超时
			if (ws != null) {
				ws = null;
				DialogUtil.alert(null, "与服务器的连接断开...", () -> {
					Gdx.app.exit();
				});
			}
		}
    }

    private static String wsUrl = null;
	private static WebSocket ws = null;
	/**
	 * 使用服务器URL创建网络交互工具类
	 * 
	 * @param url 服务器路径
	 */
	public static void init(String url) {
		wsUrl = url;
	}
	/**
	 * 启动网络交互
	 */
	public static void start() {
		ws = WebSockets.newSocket(wsUrl);
		ws.setSendGracefully(true);
		ws.addListener(new WebSocketListenerImpl());
		ws.setUseTcpNoDelay(true);
		try {
			ws.connect();
		} catch (Exception ex) { }
	}
	
	/**
	 * 停止网络交互
	 */
	public static void shutdown() {
		if (ws == null) return;
		ws.close();
		ws = null;
	}
	
	/**
	 * 是否启用保活
	 * <br>
	 * 一般进入游戏画面之后，开启保活，会在闲时与服务器产生交互避免被服务器断开
	 * 
	 * @param flag 是否启用保活
	 */
	public static void keepAlive(boolean flag) {
		keepAliveFlag = flag;
	}
    
    /**
     * 发送人物动作更改到服务器
     * 
     * @param hum 已发生动作更改的人物
     */
    public static void sendHumActionChange(ChrBasicInfo hum) {
		if (ws == null) return;
    	try {
			ws.send(Messages.humActionChange(hum).pack());
		} catch (Exception e) { }
		lastSendTime = System.currentTimeMillis();
    }
    
    /**
     * 发送登陆
     * 
     * @param una 账号
     * @param psw 密码
     */
    public static void sendLoginReq(String una, String psw) {
		if (ws == null) return;
    	try {
			ws.send(new LoginReq(una, Base64.getEncoder().encodeToString(MessageDigest.getInstance("MD5").digest(psw.getBytes()))).pack());
		} catch (Exception e) { }
		lastSendTime = System.currentTimeMillis();
    }
    
    /**
     * 发送创建用户
     * @param una
     * @param psw
     * @param name
     * @param q1
     * @param a1
     * @param q2
     * @param a2
     * @param tel
     * @param iPhone
     * @param mail
     */
    public static void sendNewUser(String una, String psw, String name, String q1, String a1, String q2, String a2, String tel,
			String iPhone, String mail) {
		if (ws == null) return;
    	try {
    		ws.send(new NewUserReq(una, Base64.getEncoder().encodeToString(MessageDigest.getInstance("MD5").digest(psw.getBytes()))
    				, name, q1, a1, q2, a2, tel, iPhone,mail).pack());
		} catch (Exception e) { }
		lastSendTime = System.currentTimeMillis();
    }
    
    /**
     * 发送修改密码
     * 
     * @param una 用户名
     * @param oldPsw 旧密码
     * @param newPsw 新密码
     */
    public static void sendModifyPsw(String una, String oldPsw, String newPsw) {
		if (ws == null) return;
    	try {
    		ws.send(new ModifyPswReq(una, Base64.getEncoder().encodeToString(MessageDigest.getInstance("MD5").digest(oldPsw.getBytes()))
    				, Base64.getEncoder().encodeToString(MessageDigest.getInstance("MD5").digest(newPsw.getBytes()))).pack());
    	} catch (Exception e) { }
		lastSendTime = System.currentTimeMillis();
    }
    
    /**
     * 发送删除角色
     * 
     * @param nama 角色名称
     */
    public static void sendDelteChr(String nama) {
		if (ws == null) return;
    	try {
    		ws.send(new DeleteChrReq(nama).pack());
    	} catch (Exception e) { }
		lastSendTime = System.currentTimeMillis();
    }
    
    /**
     * 发送创建角色
     * @param name 昵称
     * @param occupation 职业
     * @param gender 性别
     */
    public static void sendNewChr(String name, Occupation occupation, byte gender) {
		if (ws == null) return;
    	try {
    		ws.send(new NewChrReq(name, occupation, gender).pack());
    	} catch (Exception e) { }
		lastSendTime = System.currentTimeMillis();
    }
    
    /**
     * 发送进入游戏
     * 
     * @param chrName 选择的角色昵称
     */
    public static void sendEnterGame(String chrName) {
		if (ws == null) return;
    	try {
			ws.send(new EnterReq(chrName).pack());
		} catch (Exception e) { }
		lastSendTime = System.currentTimeMillis();
    }
    
    /**
     * 发送登出
     */
    public static void sendLogout() {
		if (ws == null) return;
    	try {
			ws.send(new LogoutReq().pack());
		} catch (Exception e) { }
		lastSendTime = System.currentTimeMillis();
    }
    
    /**
     * 发送离开游戏世界
     */
    public static void sendOut() {
		if (ws == null) return;
    	try {
			ws.send(new OutReq().pack());
		} catch (Exception e) { }
		lastSendTime = System.currentTimeMillis();
    }
    
    private static class WebSocketListenerImpl implements WebSocketListener {

		@Override
		public boolean onOpen(WebSocket webSocket) {
			webSocket.send("Hello wrold!");
			lastRecvTime = System.currentTimeMillis();
			lastSendTime = System.currentTimeMillis();
			return true;
		}

		@Override
		public boolean onClose(WebSocket webSocket, int closeCode, String reason) {
			if (ws != null) {
				ws = null;
				DialogUtil.alert(null, "与服务器的连接断开...", () -> {
					Gdx.app.exit();
				});
			}
			return true;
		}

		@Override
		public boolean onMessage(WebSocket webSocket, String packet) {
			lastRecvTime = System.currentTimeMillis();
			return true;
		}

		@Override
		public boolean onMessage(WebSocket webSocket, byte[] packet) {
			if (ws == null) return true;
			lastRecvTime = System.currentTimeMillis();
			try {
				synchronized (recvMsgList) {
					var msg = Message.unpack(ByteBuffer.wrap(packet));
					if (msg.type() == MessageType.SYS_INFO) {
						var sysInfo = (SysInfo) msg;
						App.timeDiff = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) - sysInfo.time;
						App.MapNames = new HashMap<>();
						App.MapMMaps = new HashMap<>();
						for (var i = 0; i < sysInfo.mapCount; ++i) {
							App.MapNames.put(sysInfo.mapNos[i], sysInfo.mapNames[i]);
							App.MapMMaps.put(sysInfo.mapNos[i], sysInfo.mapMMaps[i]);
						}
						return true;
					} else if (msg.type() == MessageType.KICKED_OUT) {
						var kickedOut = (KickedOut) msg;
						var tip = (String) null;
						if (kickedOut.reason == 1) {
							tip = "账号在其他地方登陆";
						}
						if (kickedOut.serverTip != null) {
							tip = kickedOut.serverTip;
						}
						ws = null;
						DialogUtil.alert(null, tip, () -> {
							Gdx.app.exit();
						});
						return true;
					}
					recvMsgList.add(msg);
				}
			}catch(Exception ex) { }
			return true;
		}

		@Override
		public boolean onError(WebSocket webSocket, Throwable error) {
			if (ws != null) {
				ws = null;
				DialogUtil.alert(null, "与服务器的连接断开...", () -> {
					Gdx.app.exit();
				});
			}
			return true;
		}
    	
    }
}
