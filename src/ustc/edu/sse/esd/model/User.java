package ustc.edu.sse.esd.model;

import java.io.Serializable;

/**
 * 用户类信息
 * 实现Serializable才能用Intent传递
 * Copyright: Copyright (c) 2015-2-3 20:47:25
 * Company: 中国科学技术大学 软件学院
 * @author moon：代码编写，star：代码整理
 * @version 2.0
 */
public class User implements Serializable {
	private String userName; // 用户名
	private String password; // 密码
	private boolean oldUser; // 是否为老用户

	/**
	 * 有参构造函数
	 * @param userName 用户名
	 * @param password 密码
	 * @param oldUser 是否为老用户
	 */
	public User(String userName, String password, boolean oldUser) {
		super();
		this.userName = userName;
		this.password = password;
		this.oldUser = oldUser;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isOldUser() {
		return oldUser;
	}

	public void setOldUser(boolean oldUser) {
		this.oldUser = oldUser;
	}
}
