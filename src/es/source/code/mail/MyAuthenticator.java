package es.source.code.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
//注意引入是mail包中的Authenticator
/**
 *   验证类
 * @author moon
 *
 */
public class MyAuthenticator extends Authenticator{
	String userName = null;
	String password = null;
	 
	public MyAuthenticator(){
	}
	
	public MyAuthenticator(String username, String password) { 
		this.userName = username; 
		this.password = password; 
	} 
	
	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(userName, password);
	}
}
 
