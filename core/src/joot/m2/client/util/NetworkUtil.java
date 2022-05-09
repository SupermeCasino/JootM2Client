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
import com.github.jootnet.m2.core.net.messages.EnterReq;
import com.github.jootnet.m2.core.net.messages.LoginReq;
import com.github.jootnet.m2.core.net.messages.NewChrReq;
import com.github.jootnet.m2.core.net.messages.NewUserReq;
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
     * 发送人物动作更改到服务器
     * 
     * @param hum 已发生动作更改的人物
     */
    public static void sendHumActionChange(ChrBasicInfo hum) {
		if (ws == null) return;
    	try {
			ws.send(Messages.humActionChange(hum).pack());
		} catch (Exception e) { }
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
    }
    
    private static class WebSocketListenerImpl implements WebSocketListener {

		@Override
		public boolean onOpen(WebSocket webSocket) {
			webSocket.send("Hello wrold!");
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
			return true;
		}

		@Override
		public boolean onMessage(WebSocket webSocket, byte[] packet) {
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
