package joot.m2.client.util;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.WebSockets;

import joot.m2.client.actor.Hum;
import joot.m2.client.net.Message;
import joot.m2.client.net.Messages;

/**
 * 网络交互工具类
 * 
 * @author linxing
 *
 */
public final class NetworkUtil {

    /** 接受到的数据 */
	private static List<Message> recvMsgList = new ArrayList<>();
	
    /**
     * 获取服务端发送过来的所有消息
     * <br>
     * 在上一次渲染循环中
     * 
     * @return 服务器发送过来的所有消息
     */
    public static List<Message> getRecvMsgList() {
        var recvMsgList_ = new ArrayList<Message>();
		synchronized (recvMsgList) {
			recvMsgList_.addAll(recvMsgList);
		}
        recvMsgList.clear();
        return recvMsgList_;
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
    public static void sendHumActionChange(Hum hum) {
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
