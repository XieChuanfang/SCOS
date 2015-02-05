package ustc.edu.sse.esd.fragment;

import ustc.edu.sse.esd.activity.FoodOrderView;
import ustc.edu.sse.esd.activity.R;
import ustc.edu.sse.esd.adapter.OrderListAdapter;
import ustc.edu.sse.esd.model.Food;
import ustc.edu.sse.esd.model.OrderedFood;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
/**
 *未下单菜的fragment：加载相应未下单菜品的ListView
 * Copyright: Copyright (c) 2015-2-3 12:47:25
 * Company: 中国科学技术大学 软件学院
 * 
 * @author moon：代码编写，star：代码整理
 * @version 2.0
 */
public class OrderedFrag extends Fragment implements OnClickListener {
	private ListView listView; // ListView对象
	private TextView textView; // 显示未下单菜品信息
	private Button button; // 提交订单按钮对象
	private OrderListAdapter myOrderListAdapter; // listView适配器
	private Context mContext; // FoodOrderView上下文
	private OrderedFood myOrderedFood; // 已点且未下单菜品集合对象

	public OrderedFrag(Context mContext) {
		super();
		this.mContext = mContext;
		this.myOrderedFood = FoodOrderView.myOrderedFood;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/**
	 * 为fragment加载视图：一个ListView和，一个TextView和一个Button
	 */
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		View mMainView = inflater.inflate(R.layout.food_ordered_list,
				container, false); // 加载布局文件：显示未下单菜品信息
		listView = (ListView) mMainView.findViewById(R.id.order_listview);
		textView = (TextView) mMainView.findViewById(R.id.txt_ordered_sum_info);
		button = (Button) mMainView.findViewById(R.id.btn_submit);
		button.setOnClickListener(this); // 为提交订单按钮设置监听器
		// 创建ListView适配器
		myOrderListAdapter = new OrderListAdapter(myOrderedFood, mContext,
				textView);
		/* 为ViewPager设置适配器 */
		listView.setAdapter(myOrderListAdapter);
		textView.setText("菜品总数：" + myOrderedFood.getCount() + "份" + "      "
				+ "订单总价: " + myOrderedFood.getTotalCost() + "元");

		return mMainView;
	}

	/**
	 * 设置提交订单监听事件：将未下单菜变为已下单菜，同时清空未下单菜
	 */
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_submit) {
			int count1 = myOrderedFood.getCount();
			int totalCost1 = myOrderedFood.getTotalCost();
			OrderedFood submittedFood = FoodOrderView.mySubmittedFood;
			/* 将未下单菜品集合拷贝到已下单菜品集合中 */
			for (int i = 0; i < count1; i++) {
				Food f = myOrderedFood.getOrderedList().get(i);
				submittedFood.getOrderedList().add(f);
			}
			int count2 = submittedFood.getCount();
			int totalCost2 = submittedFood.getTotalCost();
			submittedFood.setCount(count1 + count2);
			submittedFood.setTotalCost(totalCost1 + totalCost2);

			/* 将未下单菜品集合清空 */
			myOrderedFood.getOrderedList().clear();
			myOrderedFood.setCount(0);
			myOrderedFood.setTotalCost(0);
			// 更新整个ViewPager
			FoodOrderView a = (FoodOrderView) getActivity();
			/* 刷新FoodOrderView视图:重新加载ViewPager的两个fragment，执行更新操作 */
			a.getViewPagerAdapter().reLoad();
		}
	}
}
