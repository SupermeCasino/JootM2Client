package joot.m2.client.net;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.java_websocket.handshake.ServerHandshake;

import joot.m2.client.actor.Hum;

/**
 * 一个假的网络客户端
 */
public final class WebSocketClient extends org.java_websocket.client.WebSocketClient {
	
    public WebSocketClient(URI serverUri) {
		super(serverUri);
		connect();
	}

	private List<Message> recvMsgList = new ArrayList<>();

    /**
     * 断开与服务器的连接
     */
    public void close() {
        recvMsgList.clear();
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
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onMessage(ByteBuffer bytes) {
		try {
			synchronized (recvMsgList) {
				recvMsgList.add(Messages.unpack(bytes));	
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void onMessage(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {

	}

	@Override
	public void onError(Exception ex) {
		ex.printStackTrace();
	}
}
