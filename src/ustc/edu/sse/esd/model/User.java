package ustc.edu.sse.esd.model;

import java.io.Serializable;

/**
 * 用户类信息
 * Copyright: Copyright (c) 2015-2-3 20:47:25
 * Company: 中国科学技术大学 软件学院
 * 
 * @author moon：代码编写，star：代码整理
 * @version 5.0
 */
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	private String userName;
	private String password;
	private boolean oldUser;

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
