package joot.m2.client.util;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.WebSockets;
import com.github.jootnet.m2.core.actor.ChrBasicInfo;
import com.github.jootnet.m2.core.net.Message;
import com.github.jootnet.m2.core.net.Messages;

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

	private static WebSocket ws = null;
	/**
	 * 使用服务器URL创建网络交互工具类
	 * 
	 * @param url 服务器路径
	 */
	public static void init(String url) {
		ws = WebSockets.newSocket(url);
		ws.setSendGracefully(true);
		ws.addListener(new WebSocketListenerImpl());
		ws.setUseTcpNoDelay(true);
		ws.connect();
		var reConnectThread = new ReconnectThread();
		reConnectThread.setName("M2ReconnectThread-" + reConnectThread.getId());
		reConnectThread.start();
	}
	
	/**
	 * 停止网络交互
	 */
	public static void shutdown() {
		ws.close();
		needReconnect = false;
		try {
			Thread.sleep(2000);
			ws.close();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    /**
     * 发送人物动作更改到服务器
     * 
     * @param hum 已发生动作更改的人物
     */
    public static void sendHumActionChange(ChrBasicInfo hum) {
    	try {
			ws.send(Messages.pack(Messages.humActionChange(hum)));
		} catch (Exception e) { }
    }
    
    /**
     * 发送登陆
     * 
     * @param una 账号
     * @param psw 密码
     */
    public static void sendLoginReq(String una, String psw) {
    	try {
			ws.send(Messages.pack(Messages.loginReq(una, psw)));
		} catch (Exception e) { }
    }
    
    /**
     * 发送进入游戏
     * 
     * @param chrName 选择的角色昵称
     */
    public static void sendEnterGame(String chrName) {
    	try {
			ws.send(Messages.pack(Messages.selectChr(chrName)));
		} catch (Exception e) { }
    }
    
    private static class WebSocketListenerImpl implements WebSocketListener {

		@Override
		public boolean onOpen(WebSocket webSocket) {
			return true;
		}

		@Override
		public boolean onClose(WebSocket webSocket, int closeCode, String reason) {
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
					recvMsgList.add(Messages.unpack(ByteBuffer.wrap(packet)));
				}
			}catch(Exception ex) { }
			return true;
		}

		@Override
		public boolean onError(WebSocket webSocket, Throwable error) {
			return true;
		}
    	
    }
	
	private static volatile boolean needReconnect = true;
    
    private static class ReconnectThread extends Thread {
    	@Override
    	public void run() {
    		while (needReconnect) {
    			try {
	    			if (!ws.isOpen()) {
	    				ws.connect();
	    			}
					Thread.sleep(1000);
    			} catch(Exception ex) { }
    		}
    	}
    }
}
