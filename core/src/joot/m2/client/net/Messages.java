package joot.m2.client.net;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import com.github.jootnet.mir2.core.actor.Action;
import com.github.jootnet.mir2.core.actor.Direction;
import com.github.jootnet.mir2.core.actor.HumActionInfo;

import joot.m2.client.actor.Hum;
import joot.m2.client.net.messages.HumActionChange;
import joot.m2.client.net.messages.LoginReq;
import joot.m2.client.net.messages.LoginResp;

/**
 * 消息工具类
 */
public final class Messages {
	
	/**
	 * 将消息打包到数据缓冲区
	 * 
	 * @param message 消息
	 * @param buffer 缓冲区
	 * @throws IOException 
	 */
	public static byte[] pack(Message message) throws IOException {
		var stream = new ByteArrayOutputStream();
		var buffer = new DataOutputStream(stream);
		// 0.类型
		buffer.writeInt(message.type().id());
		
		switch (message.type()) {
		
		case HUM_ACTION_CHANGE: {
			var humActionChange = (HumActionChange) message;
			// 1.人物姓名
			byte[] nameBytes = humActionChange.name().getBytes(StandardCharsets.UTF_8);
			buffer.writeByte((byte) nameBytes.length);
			buffer.write(nameBytes);
			// 2.当前坐标以及动作完成后的坐标
			buffer.writeShort((short) humActionChange.x());
			buffer.writeShort((short) humActionChange.y());
			buffer.writeShort((short) humActionChange.nextX());
			buffer.writeShort((short) humActionChange.nextY());
			// 3.动作
			pack(humActionChange.action(), buffer);
			break;
		}
		
		case LOGIN_REQ: {
			var loginReq = (LoginReq) message;
			// 1.用户名
			byte[] unaBytes = loginReq.una().getBytes(StandardCharsets.UTF_8);
			buffer.writeByte((byte) unaBytes.length);
			buffer.write(unaBytes);
			// 2.密码
			byte[] pswBytes = loginReq.psw().getBytes(StandardCharsets.UTF_8);
			buffer.writeByte((byte) pswBytes.length);
			buffer.write(pswBytes);
			break;
		}
		
		
		default:
			return null;
		}

		buffer.flush();
		return stream.toByteArray();
	}
	
	/**
	 * 从缓冲区中解析数据包
	 * 
	 * @param buffer 缓冲区
	 * @return 解析的数据包或null
	 * @throws BufferUnderflowException 缓冲区数据不足
	 */
	public static Message unpack(ByteBuffer buffer) throws BufferUnderflowException {
		MessageType type = null;
		
		var typeId = buffer.getInt();
		for (var msgType : MessageType.values()) {
			if (msgType.id() == typeId) {
				type = msgType;
				break;
			}
		}
		
		if (type == null) return null;
		
		switch (type) {
		
		case HUM_ACTION_CHANGE: {
			var nameBytesLen = buffer.get();
			var nameBytes = new byte[nameBytesLen];
			buffer.get(nameBytes);
			short x = buffer.getShort();
			short y = buffer.getShort();
			short nx = buffer.getShort();
			short ny = buffer.getShort();
			var humActionInfo = new HumActionInfo();
			unpack(humActionInfo, buffer);
			return new HumActionChange(new String(nameBytes, StandardCharsets.UTF_8), x, y, nx, ny, humActionInfo);
		}
		
		case LOGIN_RESP: {
			var code = buffer.getInt();
			var tipBytesLen = buffer.get();
			String serverTip = null;
			if (tipBytesLen > 0) {
				byte[] tipBytes = new byte[tipBytesLen];
				buffer.get(tipBytes);
				serverTip = new String(tipBytes, StandardCharsets.UTF_8);
			}
			int roleCount = buffer.get();
			var roles = new LoginResp.Role[roleCount];
			for (var i = 0; i < roleCount; ++i) {
				roles[i] = new LoginResp.Role();
				roles[i].type = buffer.getInt();
				roles[i].level = buffer.getInt();
				roles[i].status = buffer.getInt();
				var nameBytesLen = buffer.get();
				byte[] nameBytes = new byte[nameBytesLen];
				buffer.get(nameBytes);
				roles[i].name = new String(nameBytes, StandardCharsets.UTF_8);
			}
			return new LoginResp(code, serverTip, roles);
		}
		
		default:
			break;
		}
		
		return null;
	}
    
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
    
    public static Message loginReq(String una, String psw) {
    	return new LoginReq(una, psw);
    }
    
    private static void pack(HumActionInfo info, DataOutput buffer) throws IOException {
    	buffer.writeByte((byte) info.act.ordinal());
    	buffer.writeByte((byte) info.dir.ordinal());
    	buffer.writeShort(info.frameIdx);
    	buffer.writeShort(info.frameCount);
    	buffer.writeShort(info.duration);
    }
    private static void unpack(HumActionInfo info, ByteBuffer buffer) {
    	byte actOrdinal = buffer.get();
    	for (var act : Action.values()) {
    		if (act.ordinal() == actOrdinal) {
    			info.act = act;
    			break;
    		}
    	}
    	byte dirOrdinal = buffer.get();
    	for (var dir : Direction.values()) {
    		if (dir.ordinal() == dirOrdinal) {
    			info.dir = dir;
    			break;
    		}
    	}
    	info.frameIdx = buffer.getShort();
    	info.frameCount = buffer.getShort();
    	info.duration = buffer.getShort();
    }
}
