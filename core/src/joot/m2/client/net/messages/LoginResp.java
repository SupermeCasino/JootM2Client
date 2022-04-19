package joot.m2.client.net.messages;

import joot.m2.client.net.Message;
import joot.m2.client.net.MessageType;

public class LoginResp implements Message {

	@Override
	public MessageType type() {
		return MessageType.LOGIN_RESP;
	}

	private int code;
	private String serverTip;
	
	public LoginResp(int code, String serverTip) {
		this.code = code;
		this.serverTip = serverTip;
	}
	
	/**
	 * 登陆相应错误码
	 * <br>
	 * 0 登陆成功
	 * 1 用户名或密码错误
	 * 2 用户不存在
	 * 
	 * @return 错误码
	 */
	public int code() {
		return this.code;
	}
	
	public String serverTip() {
		return this.serverTip;
	}
}
