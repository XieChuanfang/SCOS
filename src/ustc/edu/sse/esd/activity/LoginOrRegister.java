package ustc.edu.sse.esd.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import ustc.edu.sse.esd.model.User;
import ustc.edu.sse.esd.util.GetPostUtil;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 完成登录和注册功能，完成后返回到MainScreen主导航界面 
 * Copyright: Copyright (c) 2015-2-1 20:30:12
 * Company: 中国科学技术大学 软件学院
 * 
 * @author moon：代码编写，star：代码整理
 * @version 5.0
 */
public class LoginOrRegister extends Activity implements OnClickListener {
	public final static int LOGIN_RESULT = 2; // 登陆成功返回常量
	public final static int RETURN_RESULT = 3; // 返回成功返回常量
	public final static int REGISTER_RESULT = 4; // 注册成功返回常量
	private TextView edt_login; // 用户名输入框
	private TextView edt_password; // 密码输入框
	private Button btn_login; // 登陆按钮
	private Button btn_return; // 返回按钮
	private Button btn_register; // 注册按钮
	private User loginUser; // 当前登陆用户信息
	private SharedPreferences mySharedPreferences;  //用户配置信息操作对象
	 /*记住，在模拟器上用10.0.2.2访问你的电脑本机,如果是真机，则需要真实IP地址*/
	private final static String SPEC = "http://10.0.2.2:8080/SCOSServer/LoginValidator"; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 去除标题栏
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		// 初始化视图组件
		edt_login = (TextView) findViewById(R.id.edt_login);
		edt_password = (TextView) findViewById(R.id.edt_password);
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_return = (Button) findViewById(R.id.btn_return);
		btn_register = (Button) findViewById(R.id.btn_register);
		/*从文件名user中返回用户信息到实例mySharedPreferences*/
		mySharedPreferences= getSharedPreferences("user", Activity.MODE_PRIVATE); 
		String name = mySharedPreferences.getString("userName", "");   
		/*如果userName为空,隐藏登录按钮；否则设置默认用户名和隐藏注册提示信息*/
		if(name.isEmpty()) {       				  
			btn_login.setVisibility(View.INVISIBLE);   
		}else {                                  
			edt_login.setText(name);   
			btn_register.setVisibility(View.INVISIBLE);    
		}
		/* 设置点击监听事件 */
		btn_login.setOnClickListener(this);
		btn_return.setOnClickListener(this);
		btn_register.setOnClickListener(this);
	}
	/**
	 * 按钮点击事件处理
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login: 
			onLogin();
			break;
		case R.id.btn_return:      
			onReturn();
			break;
		case R.id.btn_register:    
			onRegister();
			break;
		}
	}
	/**
	 * 登陆按钮点击处理事件
	 */
	private void onLogin() {
		// 获取输入的数据
		final String name = edt_login.getText().toString();
		final String password = edt_password.getText().toString();
		/* 检查信息是否符合规则 */
		boolean isValid = checkValid(name, password);

		if (isValid) {
			/*用子线程连接网络，如果在UI线程连接网络会出现NetworkOnMainThreadException*/
			new Thread(new Runnable() {

				@Override
				public void run() {
					String params = null;
					try {
						params = "name=" + URLEncoder.encode(name, "UTF-8")
								+ "&password=" + URLEncoder.encode(password, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					final String result = GetPostUtil.sendPost(SPEC, params);   //用post方式访问网络获取返回结果
					if(result.equals("1")) {                //验证成功
						/*将用户信息填入user.xml文件*/
						SharedPreferences.Editor editor = mySharedPreferences.edit(); 
						editor.putString("userName", name); 
						editor.putInt("loginState", 1); 
						editor.commit(); 
						/*创建用户对象：老用户*/
						loginUser = new User(name, password, true);
						Intent intent = new Intent();
						Bundle bundle = new Bundle();
						bundle.putSerializable("login_user", loginUser);
						bundle.putString("login", "LoginSuccess");
						intent.putExtras(bundle);                         // 将封装好的数据对象寄存到Intent
						LoginOrRegister.this.setResult(LOGIN_RESULT, intent);   // 设置登录成功返回结果
						LoginOrRegister.this.finish();                   // 销毁LoginOrRegister
					} else if(result.equals("0")) {                               //验证失败
						  /*通过runOnUiThread方法修改主线程控件内容,如果当前线程是UI线程，将马上执行，否则，该完成的动作将post到UI线程的事件队列中*/
		                LoginOrRegister.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(LoginOrRegister.this, "登录失败！", Toast.LENGTH_SHORT).show();
							}
						});
					} else {   
						 LoginOrRegister.this.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(LoginOrRegister.this, "登录失败！请检查服务器是否启动！", Toast.LENGTH_SHORT).show();
								}
							});
					}
				}
			}).start(); 
		}
	}
	/**
	 * 返回按钮点击处理事件
	 */
	private void onReturn() {
		String name = mySharedPreferences.getString("userName", "");
		/*如返回，且用户曾经登陆过，修改用户登录状态，改为失败*/
		if(!name.isEmpty()) {
			SharedPreferences.Editor editor = mySharedPreferences.edit();
			editor.putInt("loginState", 0); 
			editor.commit(); 
		}
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("login", "Return");
		intent.putExtras(bundle); // 将封装好的数据对象寄存到Intent
		setResult(RETURN_RESULT, intent); // 设置返回成功返回结果
		finish(); // 销毁当前activity
	}
	/**
	 * 注册按钮点击处理事件
	 */
	private void onRegister() {
		// 获取输入的数据
		String name = edt_login.getText().toString();
		String password = edt_password.getText().toString();
		/* 检查信息是否符合规则 */
		boolean isValid = checkValid(name, password);
		if (isValid) {
			//将用户信息写入配置文件user.xml
			SharedPreferences.Editor editor = mySharedPreferences.edit(); 
			editor.putString("userName", name); 
			editor.putInt("loginState", 1); 
			editor.commit();     
			/*创建用户对象：新用户*/
			loginUser = new User(name, password, false); 
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putSerializable("login_user", loginUser);
			bundle.putString("login", "RegisterSuccess");
			intent.putExtras(bundle); // 将封装好的数据对象寄存到Intent
			setResult(REGISTER_RESULT, intent); // 设置注册成功返回结果
			finish(); // 销毁当前activity
		}
	}
	/**
	 * 判断用户名和密码是否符合规则
	 */
	private boolean checkValid(String name, String password) {
		boolean isValid = false;
		String regex = "^[\\w]{6,}+"; // 用户名和密码的正则表达式
		if (name.matches(regex) && password.matches(regex))
			isValid = true;
		else {
			if (!name.matches(regex)) {
				edt_login.setError("输入用户名不符合规则!");
			}
			if (!password.matches(regex)) {
				edt_password.setError("输入密码不符合规则!");
			}
		}
		return isValid;
	}
}
