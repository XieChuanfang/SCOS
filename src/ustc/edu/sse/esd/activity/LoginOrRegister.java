package ustc.edu.sse.esd.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * 完成登陆和注册功能，完成后返回到MainScreen主导航界面
 * Copyright: Copyright (c) 2015-2-1 20:30:12
 * Company: 中国科学技术大学   软件学院
 * @author moon
 * @version 1.1
 */
public class LoginOrRegister extends Activity {
	public final static int LOGIN_RESULT=2;   
	public final static int RETURN_RESULT=3;
	private Button btn_login;
	private Button btn_return;
	private TextView edt_login;
	private TextView edt_password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_return = (Button) findViewById(R.id.btn_return);
		edt_login = (TextView) findViewById(R.id.edt_login);
		edt_password = (TextView) findViewById(R.id.edt_password);
		
		/*登录按钮设置监听事件*/
		btn_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String name = edt_login.getText().toString();
				String password = edt_password.getText().toString();
				
				boolean isValid = checkValid(name, password);
				
				if(isValid) {
					Intent intent = new Intent(LoginOrRegister.this, MainScreen.class);
					intent.putExtra("login", "LoginSuccess");
					setResult(LOGIN_RESULT, intent);
					finish();
				}
			}

		});
		
		/*返回按钮设置监听事件*/
		btn_return.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginOrRegister.this, MainScreen.class);
				intent.putExtra("login", "Return");
				setResult(RETURN_RESULT, intent);
				finish();
			}
		});
	}
	
	/**
	 * 判断用户名和密码是否符合规则
	 * @param name:用户名
	 * @param password:密码
	 * @return 
	 */
	private boolean checkValid(String name, String password) {
		boolean isValid = false;
		String regex = "^[\\w]{6,}+";
		if(name.matches(regex) && password.matches(regex))
			isValid = true;
		else {
			if(!name.matches(regex)) {
				edt_login.setError("输入用户名不符合规则!");
			}
			if(!password.matches(regex)) {
				edt_password.setError("输入密码不符合规则!");
			}
		}
		return isValid;
	}
}
