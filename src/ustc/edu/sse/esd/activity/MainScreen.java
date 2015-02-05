package ustc.edu.sse.esd.activity;

import java.util.ArrayList;
import java.util.HashMap;

import ustc.edu.sse.esd.model.User;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/**
 * 导航页activity，由此导航到点菜，查看订单，登录/注册和帮助
 *  Copyright: Copyright (c) 2015-2-1 19:23:25
 * Company: 中国科学技术大学 软件学院
 * 
 * @author moon：代码编写，star：代码整理
 * @version 2.0
 * 
 */
public class MainScreen extends Activity {
	private GridView gridView; // GridView对象
	private static int index = 0; // 初始化数据源使用的数组下标
	private static boolean isHiden = true; // 是否隐藏点菜和查看订单导航项
	private int[] imgItems = new int[] { R.drawable.order,
			R.drawable.check_order, R.drawable.login_register, R.drawable.help }; // 导航图标
	private String[] txtItems = new String[] { "点菜", "查看订单", "登录/注册", "系统帮助" }; // 导航项文本
	private ArrayList<HashMap<String, Object>> items; // GridView数据源对象
	private User loginUser; // 当前登录用户名
	private final static int myRequestCode = 1; // 请求activity返回结果常量
	private final static int ITEM_NUM = 4; // 导航项个数
	private static final int ORDER_ITEM_POSITION = 0; // 点菜在gridView中的position
	private static final int CKECK_ORDER__ITEM_POSITION = 1; // 查看订单在gridView中的position
	private static final int LOGIN_REGISTER__ITEM_POSITION = 2; // 登录注册在gridView中的position
	private static final int HELP_ITEM_POSITION = 3; // 帮助在gridView中的position
	private static final int HIDE_INDEX = 2; // 如果隐藏导航项中的点菜和查看订单，则数据源从该下表开始加载
	private static final int NOT_HIDE_INDEX = 0; // 如果不隐藏导航项中的点菜和查看订单，则数据源从该下表开始加载

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main); // 加载视图

		gridView = (GridView) findViewById(R.id.grid_view);

		Intent intent = getIntent();
		String str = intent.getStringExtra("hint");

		/* 判断是否应该隐藏点菜和查看订单导航项 */
		if (str.equals("FromEntry")) {
			isHiden = false;
			index = NOT_HIDE_INDEX;
			Toast.makeText(MainScreen.this, str, Toast.LENGTH_SHORT).show();
		} else {
			isHiden = true;
			index = HIDE_INDEX;
		}
		/* 显示Menu */
		displayMenu();
	}

	/**
	 * 导航项点击处理监听对象
	 * 
	 * @author moon
	 * 
	 */
	private class ItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			switch (position) {
			case ORDER_ITEM_POSITION:
				Intent intent1 = new Intent(MainScreen.this, FoodView.class);
				intent1.putExtra("login_user", loginUser);
				startActivity(intent1); // 携带用户信息跳转到FoodView
				break;
			case CKECK_ORDER__ITEM_POSITION:
				Intent intent2 = new Intent(MainScreen.this,
						FoodOrderView.class);
				intent2.putExtra("login_user", loginUser);
				startActivity(intent2); // 携带用户信息跳转到FoodOrderView
				break;
			case LOGIN_REGISTER__ITEM_POSITION:
				Intent intent3 = new Intent(MainScreen.this,
						LoginOrRegister.class);
				startActivityForResult(intent3, myRequestCode);
				break;
			case HELP_ITEM_POSITION:
				break;
			default:
				break;
			}

		}
	}

	/**
	 * 回调函数
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Bundle bundle = data.getExtras();
		String strLogin = bundle.getString("login"); // 获取返回值
		if (requestCode == myRequestCode) {
			if (resultCode == LoginOrRegister.LOGIN_RESULT) { // 登录成功
				loginUser = (User) bundle.getSerializable("login_user"); // 返回用户对象
				if (isHiden) {
					isHiden = false;
					index = NOT_HIDE_INDEX;
					displayMenu(); // 刷新导航项界面
				}
				Toast.makeText(MainScreen.this, strLogin, Toast.LENGTH_SHORT)
						.show();
			} else if (resultCode == LoginOrRegister.REGISTER_RESULT) { // 注册成功返回
				loginUser = (User) bundle.getSerializable("login_user"); // 返回用户对象
				if (isHiden) {
					isHiden = false;
					index = NOT_HIDE_INDEX;
					displayMenu(); // 刷新导航项界面
				}
				Toast.makeText(MainScreen.this, "欢迎您成为SCOS新用户",
						Toast.LENGTH_SHORT).show();
			} else if (resultCode == LoginOrRegister.RETURN_RESULT) { // 返回
				Toast.makeText(MainScreen.this, strLogin, Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	/**
	 * 显示导航项
	 */
	private void displayMenu() {
		// 初始化数据源
		items = new ArrayList<HashMap<String, Object>>();
		for (int i = index; i < ITEM_NUM; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("imgItem", imgItems[i]);
			map.put("txtItem", txtItems[i]);
			items.add(map);
		}

		/* 创建适配器 */
		SimpleAdapter saImageItems = new SimpleAdapter(this, items,
				R.layout.navigation_menu,
				new String[] { "imgItem", "txtItem" }, new int[] {
						R.id.img_item, R.id.txt_item });

		/* 为gridView设置适配器 */
		gridView.setAdapter(saImageItems);
		/* 为gridView导航项设置点击处理事件 */
		gridView.setOnItemClickListener(new ItemClickListener());
	}
}
