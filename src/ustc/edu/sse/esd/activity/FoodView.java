package ustc.edu.sse.esd.activity;

import java.util.ArrayList;
import java.util.List;
import ustc.edu.sse.esd.adapter.ViewPagerAdapter;
import ustc.edu.sse.esd.fragment.BaseFrag;
import ustc.edu.sse.esd.model.Food;
import ustc.edu.sse.esd.model.OnReloadListener;
import ustc.edu.sse.esd.model.User;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * 用一个ViewPager显示四大菜品信息，每种菜品使用fragment加载到ViewPager
 * Copyright: Copyright (c) 2015-2-3 10:47:25
 * Company: 中国科学技术大学 软件学院
 * 
 * @author moon：代码编写，star：代码整理
 * @version 2.0
 */
public class FoodView extends FragmentActivity implements TabListener {
	private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>(); // fragment集合，用來填充ViewPager
	private ArrayList<Food> cool_food = new ArrayList<Food>(); // 冷菜集合
	private ArrayList<Food> hot_food = new ArrayList<Food>(); // 热菜集合
	private ArrayList<Food> seafood = new ArrayList<Food>(); // 海鲜集合
	private ArrayList<Food> drinks = new ArrayList<Food>(); // 酒水集合
	private final static String[] tabStr = new String[] { "冷菜", "热菜", "海鲜",
			"酒水" }; // tab文本数组
	private ViewPager mViewPager; // ViewPager对象
	private ViewPagerAdapter mViewPagerAdapter; // ViewPager适配器
	private User loginUser; // 登录用户信息
	private final static int ORDERED_TAB_POSITION = 0; // 未下单菜tab位置
	private final static int CHECKORDER_TAB_POSITION = 1; // 已下单菜tab位置
	private final static int CALL_FOR_SERVICE_TAB_POSITION_ = 2; // 呼叫单菜tab位置

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.food_view);
		/* 从xml文件读取数据,初始化菜品集合 */
		getDataFromXML(cool_food, R.array.cool_food_name,
				R.array.cool_food_price, R.array.cool_food_comment,
				R.array.cool_food_img);
		getDataFromXML(hot_food, R.array.hot_food_name, R.array.hot_food_price,
				R.array.hot_food_comment, R.array.hot_food_img);
		getDataFromXML(seafood, R.array.seafood_name, R.array.seafood_price,
				R.array.seafood_comment, R.array.seafood_img);
		getDataFromXML(drinks, R.array.drinks_name, R.array.drinks_price,
				R.array.drinks_comment, R.array.drinks_img);
		/* 获取用户信息 */
		loginUser = (User) getIntent().getSerializableExtra("login_user");
		/* 初始化fragment数组 */
		fragmentList.add(new BaseFrag(cool_food, FoodView.this));
		fragmentList.add(new BaseFrag(hot_food, FoodView.this));
		fragmentList.add(new BaseFrag(seafood, FoodView.this));
		fragmentList.add(new BaseFrag(drinks, FoodView.this));
		setUpActionBar(); /* 设置actionBar属性 */
		setUpViewPager(); /* 设置ViewPager */
		setUpTabs(); /* 设置tab */
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
			intent.putExtra("login_user", loginUser);
			startActivity(intent); // 进入未下单菜视图：将用户信息和菜单位置传到FoodOrderView
		} else if (menuStr.equals("查看订单")) {
			Intent intent = new Intent(FoodView.this, FoodOrderView.class);
			intent.putExtra("position", CHECKORDER_TAB_POSITION);
			intent.putExtra("login_user", loginUser);
			startActivity(intent); // 进入已下单菜：将用户信息和菜单位置传到FoodOrderView
		}
		return true;
	}

	/**
	 * 设置actionBar
	 */
	private void setUpActionBar() {
		android.app.ActionBar actionBar = getActionBar();
		// 设置actionBar基本格式
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
		// 初始化适配器
		mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),
				fragmentList, FoodView.this, tabStr);
		// setViewPagerAdapter(mViewPagerAdapter);

		mViewPager.setAdapter(mViewPagerAdapter);

		// Activity在设置适配器时，先为适配器实现添加一个回调函数, 在回调方法中调用setPagerItems方法重新设置数据
		mViewPagerAdapter.setOnReloadListener(new OnReloadListener() {

			@Override
			public void onReload() {
				fragmentList = null;
				ArrayList<BaseFrag> list = new ArrayList<BaseFrag>();
				list.add(new BaseFrag(cool_food, FoodView.this));
				list.add(new BaseFrag(hot_food, FoodView.this));
				list.add(new BaseFrag(seafood, FoodView.this));
				list.add(new BaseFrag(drinks, FoodView.this));
				mViewPagerAdapter.setPagerItems(fragmentList); // 调用setPagerItems方法重新设定数据
			}
		});
		// 为ViewPager设置页面切换监听器：左右滑动ViewPager切换fragment，则相应position的Tab也需要切换
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
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
	 * 从xml读取数据，创建food对象，再添加到菜品集合中
	 */
	private void getDataFromXML(List<Food> foodType, int nameId, int priceId,
			int commentId, int imageId) {
		// 数据数组，存放中间数据
		String[] names;
		int[] price;
		String[] comment;
		int[] images;
		// 从XML文件中装载数组资源
		names = this.getResources().getStringArray(nameId);
		price = this.getResources().getIntArray(priceId);
		comment = this.getResources().getStringArray(commentId);
		TypedArray typedArray = this.getResources().obtainTypedArray(imageId); // 获取混杂类型数组
		int length = typedArray.length();
		images = new int[length];
		for (int i = 0; i < length; i++) {
			images[i] = typedArray.getResourceId(i, 0); // 将混杂类型数组转变为图片资源id数组
		}
		typedArray.recycle();

		for (int i = 0; i < names.length; i++) {
			Food f = new Food(names[i], price[i], comment[i], images[i], false); // 创建food对象
			f.setNum(1);
			foodType.add(f); // 将food对象加载到相应菜品集合中
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
