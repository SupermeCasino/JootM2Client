package joot.m2.client.net;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import joot.m2.client.actor.Hum;

/**
 * 一个假的网络客户端
 */
public final class WebSocketClient extends org.java_websocket.client.WebSocketClient {
	
    public WebSocketClient(URI serverUri) {
		super(serverUri, new Draft_6455(), null, 300);
		connect();
		var reConnectThread = new ReconnectThread();
		reConnectThread.setName("M2ReconnectThread-" + reConnectThread.getId());
		reConnectThread.start();
	}

    /** 接受到的数据 */
	private List<Message> recvMsgList = new ArrayList<>();

	
	@Override
	protected void finalize() throws Throwable {
        recvMsgList.clear();
        needReconnect = false;
    	super.close();
	}
	
    /**
     * 获取服务端发送过来的所有消息
     * <br>
     * 在上一次渲染循环中
     * 
     * @return 服务器发送过来的所有消息
     */
    public List<Message> getRecvMsgList() {
        var recvMsgList_ = new ArrayList<Message>();
		synchronized (recvMsgList) {
			recvMsgList_.addAll(recvMsgList);
		}
        recvMsgList.clear();
        return recvMsgList_;
    }
    
    /**
     * 发送人物动作更改到服务器
     * 
     * @param hum 已发生动作更改的人物
     */
    public void sendHumActionChange(Hum hum) {
    	try {
			send(Messages.pack(Messages.humActionChange(hum)));
		} catch (Exception e) { }
    }
    
    
    
    private volatile boolean needReconnect = true;
    
    private class ReconnectThread extends Thread {
    	@Override
    	public void run() {
    		while (needReconnect) {
    			try {
	    			if (!WebSocketClient.this.isOpen()) {
	    				WebSocketClient.this.reconnectBlocking();
	    			}
					Thread.sleep(1000);
    			} catch(Exception ex) { }
    		}
    			System.out.println();
    	}
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
}
