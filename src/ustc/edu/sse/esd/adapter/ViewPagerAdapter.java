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
 * @version 2.0
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
	// Tab标签索引
	private static final int TAB_INDEX_ONE = 0;
	private static final int TAB_INDEX_TWO = 1;
	private static final int TAB_INDEX_THREE = 2;
	private static final int TAB_INDEX_FOUR = 3;

	private String[] tabStr; // tab文本数组
	private List<Fragment> fgs = null; // ViewPager数据源：fragment集合
	private FragmentManager mFragmentManager; // fragment管理器
	private Context context; // FoodView的上下文
	private OnReloadListener mListener; // 当数据发生改变时的回调接口
	private int mChildCount = 0;

	/* 适配器构造函数 */
	public ViewPagerAdapter(FragmentManager fm, List<Fragment> fgs,
			Context context, String[] tabStr) {
		super(fm);
		this.fgs = fgs;
		mFragmentManager = fm;
		this.context = context;
		this.tabStr = tabStr;
	}

	/**
	 * 获取不同索引的Fragment
	 */
	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case TAB_INDEX_ONE:
			return fgs.get(TAB_INDEX_ONE);
		case TAB_INDEX_TWO:
			return fgs.get(TAB_INDEX_TWO);
		case TAB_INDEX_THREE:
			return fgs.get(TAB_INDEX_THREE);
		case TAB_INDEX_FOUR:
			return fgs.get(TAB_INDEX_FOUR);
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
		case TAB_INDEX_ONE:
			tabLabel = tabStr[TAB_INDEX_ONE];
			break;
		case TAB_INDEX_TWO:
			tabLabel = tabStr[TAB_INDEX_TWO];
			break;
		case TAB_INDEX_THREE:
			tabLabel = tabStr[TAB_INDEX_THREE];
			break;
		case TAB_INDEX_FOUR:
			tabLabel = tabStr[TAB_INDEX_FOUR];
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
