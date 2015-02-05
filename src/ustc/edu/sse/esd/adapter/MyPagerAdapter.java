package ustc.edu.sse.esd.adapter;

import java.util.ArrayList;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * 用于将菜品详细信息适配到ViewPager
 * Copyright: Copyright (c) 2015-2-3 9:47:25
 * Company: 中国科学技术大学 软件学院
 * 
 * @author moon：代码编写，star：代码整理
 * @version 2.0
 */
public class MyPagerAdapter extends PagerAdapter {
	ArrayList<View> views = new ArrayList<View>(); // 菜品详细信息视图集合

	public MyPagerAdapter(ArrayList<View> views) {
		super();
		this.views = views;
	}

	@Override
	public int getCount() {
		return views.size();
	}

	/**
	 * 销毁其他视图
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(views.get(position));
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	/**
	 * 将相应位置的view视图添加到ViewPager中去
	 */
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = views.get(position);
		container.addView(view);
		return view;
	}
}
