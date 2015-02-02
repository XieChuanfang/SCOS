package ustc.edu.sse.esd.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * 导航页activity，由此导航到点菜，查看订单，登录/注册和帮助
 * Copyright: Copyright (c) 2015-2-1 19:23:25
 * Company: 中国科学技术大学   软件学院
 * @author moon
 * @version 1.1
 * 
 */
public class MainScreen extends Activity  {
	private final static int requestCode = 1;   //请求activity返回结果常亮
	private ImageView login_register;
	private RelativeLayout order_layout;
	private RelativeLayout check_order_layout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);       //加载视图
	    
		/*初始化视图*/
		viewInit(); 
		
		/*设置登陆注册点击事件处理函数*/
		login_register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainScreen.this, LoginOrRegister.class);
				startActivityForResult(intent, requestCode);
			}
		});
	}
	
	/**
	 * 初始化视图
 	*/
	private void viewInit() {
		// TODO Auto-generated method stub
		login_register = (ImageView) findViewById(R.id.login_register);
		order_layout = (RelativeLayout) findViewById(R.id.order_layout);
		check_order_layout = (RelativeLayout) findViewById(R.id.check_order_layout);
		
		Intent intent = getIntent();
		String str = intent.getStringExtra("myhint");
		
		if("FromEntry".equals(str)) {
			Toast.makeText(MainScreen.this, str, Toast.LENGTH_SHORT).show();
		} else {
			findViewById(R.id.order_layout).setVisibility(View.INVISIBLE);
			findViewById(R.id.check_order_layout).setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 回调函数
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		String strLogin = data.getStringExtra("login");  //获取返回值
		
		if (requestCode == 1) {        
			if (resultCode == LoginOrRegister.LOGIN_RESULT) {           //登录成功
				
				Toast.makeText(MainScreen.this, strLogin,
						Toast.LENGTH_SHORT).show();
				
				//判断点菜和查看订单导航项是否可见，如果不可见，设置可见
				if(!order_layout.isShown()) {
					order_layout.setVisibility(View.VISIBLE);
				} 
				
				if (!check_order_layout.isShown()) {
					check_order_layout.setVisibility(View.VISIBLE);
				}
				
			} else if (resultCode == LoginOrRegister.RETURN_RESULT) {           //返回
				Toast.makeText(MainScreen.this, strLogin, Toast.LENGTH_SHORT)
						.show();
			}
		}
	}
}
