package joot.m2.client.net.messages;

import com.github.jootnet.mir2.core.actor.HumActionInfo;

import joot.m2.client.net.Message;
import joot.m2.client.net.MessageType;

/**
 * 告知玩家动作更改的消息
 */
public final class HumActionChange implements Message {

    @Override
    public MessageType type() {
        return MessageType.HUM_ACTION_CHANGE;
    }
    
    private String name;
    private int x;
    private int y;
    private int nextX;
    private int nextY;
    private HumActionInfo action;

    public HumActionChange(String name, int x, int y, int nextX, int nextY, HumActionInfo action) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.nextX = nextX;
        this.nextY = nextY;
        this.action = action;
    }

    /**
     * 获取动作发生的玩家名称
     * 
     * @return 玩家名称
     */
    public String name() {
        return name;
    }

    /**
     * 获取动作发生时身处的横坐标
     * 
     * @return 玩家（地图）横坐标
     */
    public int x() {
        return x;
    }

    /**
     * 获取动作发生时身处的纵坐标
     * 
     * @return 玩家（地图）纵坐标
     */
    public int y() {
        return y;
    }

	/**
	 * 获取角色当前动作完成之后应该到达的位置
	 * 
	 * @return 目标位置横坐标
	 */
	public int nextX() {
		return nextX;
	}

	/**
	 * 获取角色当前动作完成之后应该到达的位置
	 * 
	 * @return 目标位置纵坐标
	 */
	public int nextY() {
		return nextY;
	}

    /**
     * 获取玩家动作
     * 
     * @return 玩家最新动作
     */
    public HumActionInfo action() {
        return action;
    }
}
