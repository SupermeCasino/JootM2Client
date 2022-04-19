package joot.m2.client.net.messages;

import joot.m2.client.net.Message;
import joot.m2.client.net.MessageType;

public class LoginResp implements Message {
	
	/**
	 * 角色
	 */
	public static class Role {
		public int type; // 0:战士 1:法师 2:道士 3:刺客
		public int level; // 等级 1-1000
		public int status; // 状态 0:激活
		public String name; // 昵称
	}

	@Override
	public MessageType type() {
		return MessageType.LOGIN_RESP;
	}

	private int code;
	private String serverTip;
	private Role[] roles;
	
	public LoginResp(int code, String serverTip, Role[] roles) {
		this.code = code;
		this.serverTip = serverTip;
		this.roles = roles;
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
	
	public Role[] roles() {
		return this.roles;
	}
}
