package ustc.edu.sse.esd.activity;

import java.util.ArrayList;
import ustc.edu.sse.esd.activity.R;
import ustc.edu.sse.esd.adapter.ViewPagerAdapter;
import ustc.edu.sse.esd.db.DBService;
import ustc.edu.sse.esd.fragment.BaseFrag;
import ustc.edu.sse.esd.model.Food;
import ustc.edu.sse.esd.model.OnReloadListener;
import ustc.edu.sse.esd.model.User;
import ustc.edu.sse.esd.util.XMLReader;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
/**
 * 用一个ViewPager显示四大菜品信息，每种菜品使用fragment加载到ViewPager
 * Copyright: Copyright (c) 2015-2-3 10:47:25
 * Company: 中国科学技术大学 软件学院
 * 
 * @author moon：代码编写，star：代码整理
 * @version 5.0
 */
public class FoodView extends FragmentActivity implements TabListener {
	private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>(); // fragment集合，用來填充ViewPager
	private ArrayList<Food> fList = new ArrayList<Food>();                // 菜品集合
	private final static String[] tabStr = new String[] { "冷菜", "热菜", "海鲜", "酒水" }; // tab文本数组
	private ViewPager mViewPager;               // ViewPager对象
	private ViewPagerAdapter mViewPagerAdapter; // ViewPager适配器
	private User loginUser;                     // 登录用户信息
	private final static int ORDERED_TAB_POSITION = 0; // 未下单菜tab位置
	private final static int CHECKORDER_TAB_POSITION = 1; // 已下单菜tab位置
	public final static int COOL_FOOD_ID = 1;       //菜品ID
	public final static int HOT_FOOD_ID = 2;
	public final static int SEAFOOD_ID = 3;
	public final static int DRINKS_ID = 4;
	private DBService ds;                   //数据库操作对象
	/**
	 * 本地进程handler
	 */
	Handler h = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 10 ) {
				mViewPagerAdapter.reLoad();     //重新装载ViewPager,更新UI
			} 
		}
	};
	private Messenger cm = new Messenger(h);         //本进程Messenger
	private Messenger sm = null;                     //server进程messenger
	private boolean isBound = false;                 //是否绑定服务
	ServiceConnection conn = new ServiceConnection() {       //ServiceConnection对象监听应用服务的状态
		@Override
		public void onServiceDisconnected(ComponentName name) {
			sm = null;
			isBound = false;
		}
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			sm = new Messenger(service);    //service来自于ServerObserverService的getBinder返回的IBinder
			isBound = true;	
		}
	};
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.food_view);
		/* 从xml文件读取数据,初始化菜品集合 */
		XMLReader.context = this;
		XMLReader.getDataFromXML(fList);
		ds = new DBService(this);
		initDB(fList);          //初始化数据库表FOOD      
		/* 获取用户信息 */
		loginUser = (User) getIntent().getSerializableExtra("login_user");
		/* 初始化fragment数组 */
		fragmentList.add(new BaseFrag(COOL_FOOD_ID, FoodView.this));
		fragmentList.add(new BaseFrag(HOT_FOOD_ID, FoodView.this));
		fragmentList.add(new BaseFrag(SEAFOOD_ID, FoodView.this));
		fragmentList.add(new BaseFrag(DRINKS_ID, FoodView.this));
		setUpActionBar();         /* 设置actionBar属性 */
		setUpViewPager();         /* 设置ViewPager */
		setUpTabs();              /* 设置tab */
	}
	
	/**
	 * 将菜品信息插入数据库
	 */
	private void initDB(ArrayList<Food> fList) {
		for(int i = 0; i < fList.size(); i++)
			ds.insertFood(fList.get(i));
	}
	
	@Override
	protected void onStart() {
		super.onStart();
        Intent intent = new Intent(FoodView.this, ustc.edu.sse.esd.service.ServerObserverService.class);
        if(!bindService(intent, conn, Context.BIND_AUTO_CREATE)) {                              //启动Service
        	Toast.makeText(this, "抱歉！服务器连接出现故障，请稍后再更新！", Toast.LENGTH_SHORT).show();
        }
	}
	/**
	 * 加载菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater(); // 获取菜单布局加载器  
		inflater.inflate(R.layout.menus, menu); // 加载菜单
		return super.onCreateOptionsMenu(menu);
	}
	/**
	 * 菜单选择处理函数
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		super.onMenuItemSelected(featureId, item);
		String menuStr = (String) item.getTitle();
		if (menuStr.equals("已点菜品")) {
			Intent intent = new Intent(FoodView.this, FoodOrderView.class);
			intent.putExtra("position", ORDERED_TAB_POSITION);
			intent.putExtra("Entry", "FoodView");
			intent.putExtra("login_user", loginUser);
			startActivity(intent);           // 进入未下单菜视图：将用户信息和菜单位置传到FoodOrderView
		} else if (menuStr.equals("查看订单")) {
			Intent intent = new Intent(FoodView.this, FoodOrderView.class);
			intent.putExtra("position", CHECKORDER_TAB_POSITION);
			intent.putExtra("login_user", loginUser);
			intent.putExtra("Entry", "FoodView");
			startActivity(intent);             // 进入已下单菜：将用户信息和菜单位置传到FoodOrderView
		} else if(menuStr.equals("启动实时更新")) {
			if(isBound) {
				Message m = Message.obtain();
				m.what = 1;
				m.replyTo = cm;              //传递本进程Messenger对象到server
				try {
					sm.send(m);              //发送消息到server
				} catch (RemoteException e) {
					sm = null;
				}
				item.setTitle("停止实时更新");
			}
		} else if(menuStr.equals("停止实时更新")) {
			Message m = Message.obtain();
			m.what = 0;
			try {
				sm.send(m);   //发送消息
			} catch (RemoteException e) {
				e.printStackTrace();
			}  
			item.setTitle("启动实时更新");
		}
		return true;
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		if(isBound) {
			unbindService(conn);              //取消绑定，停止服务
			isBound = false;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ds.clearFood();                        //清空数据库表FOOD
	}
	/**
	 * 设置actionBar属性
	 */
	private void setUpActionBar() {
		android.app.ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
	}
	/**
	 * 为ViewPager设置适配器
	 */
	private void setUpViewPager() {
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),
				fragmentList, FoodView.this, tabStr);
		mViewPager.setAdapter(mViewPagerAdapter);
		/* Activity在设置适配器时，先为适配器实现添加一个回调函数, 在回调方法中调用setPagerItems方法重新设置数据*/
		mViewPagerAdapter.setOnReloadListener(new OnReloadListener() {
			@Override
			public void onReload() {
				fragmentList = null;
				ArrayList<Fragment> list = new ArrayList<Fragment>();
				list.add(new BaseFrag(COOL_FOOD_ID, FoodView.this));
				list.add(new BaseFrag(HOT_FOOD_ID, FoodView.this));
				list.add(new BaseFrag(SEAFOOD_ID, FoodView.this));
				list.add(new BaseFrag(DRINKS_ID, FoodView.this));
				mViewPagerAdapter.setPagerItems(list); // 调用setPagerItems方法重新设定数据
			}
		});
		/* 为ViewPager设置页面切换监听器：左右滑动ViewPager切换fragment，则相应position的Tab也需要切换*/
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						android.app.ActionBar actionBar = getActionBar();
						actionBar.setSelectedNavigationItem(position); // 选中当前fragment位置的tab
					}
				});
	}
	/**
	 * 向actionBar中添加tab，并为每个tab设置文本和监听器
	 */
	private void setUpTabs() {
		android.app.ActionBar actionBar = getActionBar();
		for (int i = 0; i < mViewPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
					.setText(mViewPagerAdapter.getPageTitle(i))
					.setTabListener((TabListener) this));
		}
	}
	/**
	 * tab点击事件处理函数：tab点击后相应position的fragment也需要切换
	 */
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		if (mViewPager != null)
			mViewPager.setCurrentItem(tab.getPosition()); // 显示当前tab位置的fragment
	}
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}
}
