package ustc.edu.sse.esd.adapter;

import java.util.List;
import ustc.edu.sse.esd.model.OnReloadListener;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 自定义ViewPager的适配器,继承自FragmentPagerAdapter,用于将fragment适配到ViewPager
 * Copyright: Copyright (c) 2015-2-4 11:47:25
 * Company: 中国科学技术大学 软件学院
 * 
 * @author moon：代码编写，star：代码整理
 * @version 5.0
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
	private static final int TAB_COOL_FOOD_INDEX = 0;
	private static final int TAB_HOT_FOOD_INDEX = 1;
	private static final int TAB_SEAFOOD_INDEX = 2;
	private static final int TAB_DRINKS_INDEX = 3;

	private String[] tabStr; // tab文本数组
	private List<Fragment> fgs = null; // ViewPager数据源：fragment集合
	private FragmentManager mFragmentManager; // fragment管理器
	private OnReloadListener mListener; // 当数据发生改变时的回调接口
	private int mChildCount = 0;

	/* 适配器构造函数 */
	public ViewPagerAdapter(FragmentManager fm, List<Fragment> fgs,
			Context context, String[] tabStr) {
		super(fm);
		this.fgs = fgs;
		mFragmentManager = fm;
		this.tabStr = tabStr;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case TAB_COOL_FOOD_INDEX:
			return fgs.get(TAB_COOL_FOOD_INDEX);
		case TAB_HOT_FOOD_INDEX:
			return fgs.get(TAB_HOT_FOOD_INDEX);
		case TAB_SEAFOOD_INDEX:
			return fgs.get(TAB_SEAFOOD_INDEX);
		case TAB_DRINKS_INDEX:
			return fgs.get(TAB_DRINKS_INDEX);
		default:
			break;
		}

		return null;
	}

	@Override
	public int getCount() {
		return fgs.size();
	}

	/**
	 * 获取tab的文本
	 */
	@Override
	public CharSequence getPageTitle(int position) {
		String tabLabel = null;
		switch (position) {
		case TAB_COOL_FOOD_INDEX:
			tabLabel = tabStr[TAB_COOL_FOOD_INDEX];
			break;
		case TAB_HOT_FOOD_INDEX:
			tabLabel = tabStr[TAB_HOT_FOOD_INDEX];
			break;
		case TAB_SEAFOOD_INDEX:
			tabLabel = tabStr[TAB_SEAFOOD_INDEX];
			break;
		case TAB_DRINKS_INDEX:
			tabLabel = tabStr[TAB_DRINKS_INDEX];
			break;
		default:
			break;
		}
		return tabLabel;
	}

	@Override
	public int getItemPosition(Object object) {
		if (mChildCount > 0) {
			mChildCount--;
			return POSITION_NONE;
		}
		return super.getItemPosition(object);
	}

	@Override
	public void notifyDataSetChanged() {
		mChildCount = getCount();
		super.notifyDataSetChanged();
	}

	/**
	 * 设置监听器
	 */
	public void setOnReloadListener(OnReloadListener listener) {
		this.mListener = listener;
	}

	/**
	 * 重新设置页面内容
	 */
	public void setPagerItems(List<Fragment> items) {
		if (items != null) {
			for (int i = 0; i < fgs.size(); i++) {
				mFragmentManager.beginTransaction().remove(fgs.get(i)).commit();
			}

			fgs = items;
		}
	}

	/*
	 * 当页面数据发生改变的时候可以调用此方法，重新载入数据，具体载入信息由回调函数实现
	 */
	public void reLoad() {
		if (mListener != null) {
			mListener.onReload();
		}

		this.notifyDataSetChanged(); // 不可少，通知系统数据改变
	}
}
