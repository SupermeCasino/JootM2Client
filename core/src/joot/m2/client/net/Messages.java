package joot.m2.client.net;

import com.github.jootnet.mir2.core.actor.Action;

import joot.m2.client.actor.Hum;
import joot.m2.client.net.messages.HumActionChange;

/**
 * 消息创建工具类
 */
public final class Messages {
    
    /**
     * 为人物新动作创建消息
     * 
     * @param hum 人物
     * @return 人物动作更新消息
     */
    public static Message humActionChange(Hum hum) {
        var step = 1;
		if (hum.getAction().act == Action.Run) step++;
        var nx = hum.getX();
        var ny = hum.getY();
        switch (hum.getAction().dir) {
            case North:
                ny -= step;
                break;
            case NorthEast:
                ny -= step;
                nx += step;
                break;
            case East:
                nx += step;
                break;
            case SouthEast:
                ny += step;
                nx += step;
                break;
            case South:
                ny += step;
                break;
            case SouthWest:
                ny += step;
                nx -= step;
                break;
            case West:
                nx -= step;
                break;
            case NorthWest:
                ny -= step;
                nx -= step;
                break;

            default:
                break;
        }
        return new HumActionChange(hum.getName(), hum.getX(), hum.getY(), nx, ny, hum.getAction());
    }
}
