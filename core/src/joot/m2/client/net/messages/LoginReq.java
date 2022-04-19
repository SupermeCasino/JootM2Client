package joot.m2.client.net.messages;

import joot.m2.client.net.Message;
import joot.m2.client.net.MessageType;

/**
 * 登陆请求
 * 
 * @author linxing
 *
 */
public final class LoginReq implements Message {

	@Override
	public MessageType type() {
		return MessageType.LOGIN_REQ;
	}
	
	private String una;
	private String psw;
	
	public LoginReq(String una, String psw) {
		this.una = una;
		this.psw = psw;
	}
	
	public String una() {
		return this.una;
	}
	
	public String psw() {
		return this.psw;
	}

}
