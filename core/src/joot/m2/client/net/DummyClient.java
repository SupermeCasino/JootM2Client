package joot.m2.client.net;

import java.util.ArrayList;
import java.util.List;

import joot.m2.client.actor.Hum;

/**
 * 一个假的网络客户端
 */
public final class DummyClient {
    private List<Message> recvMsgList = new ArrayList<>();

    /**
     * 断开与服务器的连接
     */
    public void close() {
        recvMsgList.clear();
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
        recvMsgList_.addAll(recvMsgList);
        recvMsgList.clear();
        return recvMsgList_;
    }
    
    /**
     * 发送人物动作更改到服务器
     * 
     * @param hum 已发生动作更改的人物
     */
    public void sendHumActionChange(Hum hum) {
        // 这里直接从发送队列（无）送入接收队列，表示动作确认
        recvMsgList.add(Messages.humActionChange(hum));
    }
}
