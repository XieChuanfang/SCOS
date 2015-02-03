package ustc.edu.sse.esd.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * 完成登录和注册功能，完成后返回到MainScreen主导航界面
 * Copyright: Copyright (c) 2015-2-1 20:30:12
 * Company: 中国科学技术大学   软件学院
 * @author moon：代码编写，star：代码整理
 * @version 1.2
 */
public class LoginOrRegister extends Activity {
	public final static int LOGIN_RESULT=2; // 登录结果  
	public final static int RETURN_RESULT=3; // 注册结果 
	private Button btn_login; // 登录按钮
	private Button btn_return; // 注册按钮
	private TextView edt_login; // 用户名输入框
	private TextView edt_password; // 密码输入框

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		// 初始化视图组件
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_return = (Button) findViewById(R.id.btn_return);
		edt_login = (TextView) findViewById(R.id.edt_login);
		edt_password = (TextView) findViewById(R.id.edt_password);
		
		/*登录按钮设置监听事件*/
		btn_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 获取输入的数据
				String name = edt_login.getText().toString();
				String password = edt_password.getText().toString();
				// 验证输入是否符合规则
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
			public void onClick(View v) { // 跳转回主功能界面
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
	 * @return boolean 是否验证成功
	 */
	private boolean checkValid(String name, String password) {
		boolean isValid = false; // 标记是否验证通过
		String regex = "^[\\w]{6,}+"; // 正则表达式
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
