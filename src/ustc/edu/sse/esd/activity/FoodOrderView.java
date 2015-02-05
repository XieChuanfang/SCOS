package ustc.edu.sse.esd.activity;

import java.util.ArrayList;
import java.util.List;
import ustc.edu.sse.esd.adapter.ViewPagerAdapter;
import ustc.edu.sse.esd.fragment.OrderedFrag;
import ustc.edu.sse.esd.fragment.SubmittedOrderedFrag;
import ustc.edu.sse.esd.model.OnReloadListener;
import ustc.edu.sse.esd.model.OrderedFood;
import ustc.edu.sse.esd.model.User;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

/**
 * 一个ViewPager加载两个Fragment，分别是FoodOrderedFragment和FoodSubmitedFragment，
 * 分别展示未下单菜和下单菜两个视图
 * Copyright: Copyright (c) 2015-2-4 16:47:25
 * Company: 中国科学技术大学 软件学院
 * 
 * @author moon：代码编写，star：代码整理
 * @version 2.0
 */
public class FoodOrderView extends FragmentActivity implements TabListener {
	private List<Fragment> fragmentList; // fragment集合
	private final static String[] tabStr = new String[] { "未下单菜", "已下单菜" }; // tab文本数组
	private ViewPager mViewPager; // ViewPager对象
	private ViewPagerAdapter mViewPagerAdapter; // ViewPager适配器对象
	private User loginUser; // 当前登录用户
	public final static OrderedFood myOrderedFood = new OrderedFood(); // 已点菜品对象,表示一个未下的订单
	public final static OrderedFood mySubmittedFood = new OrderedFood(); // 已点菜品对象，表示一个已下的订单

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.food_order_view); // 加载布局文件
		mViewPager = (ViewPager) findViewById(R.id.food_order_pager); // 创建ViewPager对象

		loginUser = (User) getIntent().getSerializableExtra("login_user"); // 获得当前用户对象
		int position = getIntent().getIntExtra("position", 0); // 得到显示Fragment的Position
		/* 初始化fragment集合 */
		fragmentList = new ArrayList<Fragment>();
		fragmentList.add(new OrderedFrag(FoodOrderView.this));
		fragmentList
				.add(new SubmittedOrderedFrag(FoodOrderView.this, loginUser));

		setUpActionBar();
		setUpViewPager();
		setUpTabs();

		mViewPager.setCurrentItem(position); // 初始化当前显示的Fragment View
	}

	/**
	 * 设置actionBar属性
	 */
	private void setUpActionBar() {
		final android.app.ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
	}

	/**
	 * 为ViewPager设置监听事件
	 */
	private void setUpViewPager() {
		// 创建ViewPagerAdapter实例
		mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),
				fragmentList, FoodOrderView.this, tabStr);

		/* Activity在设置适配器时，先为适配器实现添加一个回调函数, 在回调方法中调用setPagerItems方法重新设置数据 */
		mViewPagerAdapter.setOnReloadListener(new OnReloadListener() {

			@Override
			public void onReload() {
				fragmentList = null;
				List<Fragment> list = new ArrayList<Fragment>();
				list.add(new OrderedFrag(FoodOrderView.this));
				list.add(new SubmittedOrderedFrag(FoodOrderView.this, loginUser));
				mViewPagerAdapter.setPagerItems(list); // 调用setPagerItems方法重新加载fragment
			}
		});

		mViewPager.setAdapter(mViewPagerAdapter); // 设置适配器
		setViewPagerAdapter(mViewPagerAdapter);
		/* 为ViewPager页面切换设置监听事件 */
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						final android.app.ActionBar actionBar = getActionBar();
						actionBar.setSelectedNavigationItem(position);
					}
				});
	}

	/**
	 * 为actionBar添加tab
	 */
	private void setUpTabs() {
		android.app.ActionBar actionBar = getActionBar();
		for (int i = 0; i < mViewPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
					.setText(mViewPagerAdapter.getPageTitle(i))
					.setTabListener((TabListener) this));
		}
	}

	public ViewPagerAdapter getViewPagerAdapter() {
		return mViewPagerAdapter;
	}

	public void setViewPagerAdapter(ViewPagerAdapter mViewPagerAdapter) {
		this.mViewPagerAdapter = mViewPagerAdapter;
	}

	/**
	 * tab点击事件处理函数：tab点击后相应position的fragment也需要切换
	 */
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		if (mViewPager != null)
			mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}
}
