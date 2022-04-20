package joot.m2.client.util;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

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

	private static WebSocketClient wsc = null;
	/**
	 * 使用服务器URL创建网络交互工具类
	 * 
	 * @param url 服务器路径
	 */
	public static void init(String url) {
		wsc = new WebSocketClientImpl(URI.create(url));
	}
    
    /**
     * 发送人物动作更改到服务器
     * 
     * @param hum 已发生动作更改的人物
     */
    public static void sendHumActionChange(Hum hum) {
    	try {
			wsc.send(Messages.pack(Messages.humActionChange(hum)));
		} catch (Exception e) { }
    }
    
    public static void sendLoginReq(String una, String psw) {
    	try {
			wsc.send(Messages.pack(Messages.loginReq(una, psw)));
		} catch (Exception e) { }
    }
    
    
    
    
    
    private static class WebSocketClientImpl extends WebSocketClient {

		public WebSocketClientImpl(URI serverUri) {
			super(serverUri, new Draft_6455(), null, 300);
			connect();
			var reConnectThread = new ReconnectThread();
			reConnectThread.setName("M2ReconnectThread-" + reConnectThread.getId());
			reConnectThread.start();
		}

		@Override
		public void onOpen(ServerHandshake handshakedata) { }
		
		@Override
		public void onMessage(ByteBuffer bytes) {
			try {
				synchronized (recvMsgList) {
					recvMsgList.add(Messages.unpack(bytes));	
				}
			}catch(Exception ex) { }
		}

		@Override
		public void onMessage(String message) { }

		@Override
		public void onClose(int code, String reason, boolean remote) { }

		@Override
		public void onError(Exception ex) { }
		
		
	    
	    private volatile boolean needReconnect = true;
	    
	    private class ReconnectThread extends Thread {
	    	@Override
	    	public void run() {
	    		while (needReconnect) {
	    			try {
		    			if (!WebSocketClientImpl.this.isOpen()) {
		    				WebSocketClientImpl.this.reconnectBlocking();
		    			}
						Thread.sleep(1000);
	    			} catch(Exception ex) { }
	    		}
	    	}
	    }
    	
    }
}
