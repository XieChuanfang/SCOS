package ustc.edu.sse.esd.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ustc.edu.sse.esd.activity.FoodOrderView;
import ustc.edu.sse.esd.activity.R;
import ustc.edu.sse.esd.model.Food;
import ustc.edu.sse.esd.model.OrderedFood;
import ustc.edu.sse.esd.model.User;
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
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 已下单菜fragment：加载相应已下单菜品的ListView
 * Copyright: Copyright (c) 2015-2-3 14：27::25
 * Company: 中国科学技术大学 软件学院
 * 
 * @author moon：代码编写，star：代码整理
 * @version 2.0
 */
public class SubmittedOrderedFrag extends Fragment implements OnClickListener {
	private OrderedFood mySubmittedFood; // 已点但已下单菜品集合
	private Context mContext; // FoodOrderView上下文
	private ListView listView; // ListView对象
	private TextView textView; // 显示未下单菜品信息
	private Button button; // 结账按钮对象
	private User loginUser; // 登陆用户信息

	public SubmittedOrderedFrag(Context mContext, User loginUser) {
		super();
		this.mContext = mContext;
		this.mySubmittedFood = FoodOrderView.mySubmittedFood;
		this.loginUser = loginUser;
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

		View mMainView = inflater.inflate(R.layout.food_submitted_list,
				container, false); // 加载布局文件：显示未下单菜品信息
		listView = (ListView) mMainView.findViewById(R.id.submit_listview);
		textView = (TextView) mMainView
				.findViewById(R.id.txt_submitted_sum_info);
		button = (Button) mMainView.findViewById(R.id.btn_pay);
		button.setOnClickListener(this); // 为结账按钮设置监听器
		// 创建适配器
		SimpleAdapter adapter = new SimpleAdapter(mContext, initData(),
				R.layout.food_submitted_item, new String[] { "name", "price",
						"number", "comment" }, new int[] { R.id.txt_name,
						R.id.txt_price, R.id.txt_number, R.id.txt_comment });

		listView.setAdapter(adapter); // 为ListView设置适配器
		textView.setText("菜品总数：" + mySubmittedFood.getCount() + "份" + "      "
				+ "订单总价: " + mySubmittedFood.getTotalCost() + "元");
		return mMainView;
	}

	/**
	 * 初始化数据源
	 * 
	 * @return List<HashMap<String, Object>> 提交的订单菜品
	 */
	private List<HashMap<String, Object>> initData() {
		HashMap<String, Object> item;
		ArrayList<HashMap<String, Object>> mListItem;
		mListItem = new ArrayList<HashMap<String, Object>>();
		int length = mySubmittedFood.getCount();
		for (int i = 0; i < length; i++) {
			Food food = mySubmittedFood.getOrderedList().get(i);
			item = new HashMap<String, Object>();
			item.put("name", food.getName());
			item.put("price", food.getPrice() + "元/份");
			item.put("number", food.getNum() + "份");
			item.put("comment", food.getComment());
			mListItem.add(item);
		}

		return mListItem;
	}

	/**
	 * 结账按钮点击事件:主要完成，清空已下单菜集合，完成结账
	 */
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_pay) {
			if (loginUser != null) {
				if (loginUser.isOldUser()) {
					Toast.makeText(mContext, "您好，老顾客，本次你可享受7 折优惠",
							Toast.LENGTH_SHORT).show(); // 老顾客
				} else {
					Toast.makeText(mContext, "结账完成", Toast.LENGTH_SHORT).show(); // 新顾客
				}
				// 结账完成后清空已下单菜
				mySubmittedFood.getOrderedList().clear();
				mySubmittedFood.setCount(0);
				mySubmittedFood.setTotalCost(0);

				button.setText("已结账");
			} else {
				Toast.makeText(mContext, "抱歉，您未登录，请登录", Toast.LENGTH_SHORT)
						.show(); // 未登录
			}
		}
	}
}
