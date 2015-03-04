package ustc.edu.sse.esd.adapter;

import java.util.ArrayList;
import ustc.edu.sse.esd.activity.R;
import ustc.edu.sse.esd.db.DBService;
import ustc.edu.sse.esd.model.Food;
import ustc.edu.sse.esd.model.OrderedFood;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 未下单菜和已下单菜Fragment中ListView适配器
 * Copyright: Copyright (c) 2015-2-4 9:47:25
 * Company: 中国科学技术大学 软件学院
 * 
 * @author moon：代码编写，star：代码整理
 * @version 5.0
 */
public class OrderListAdapter extends BaseAdapter {
	private OrderedFood myOrderedFood; // 已点菜品集合对象
	private ArrayList<Food> fList; // 已点菜品集合
	private LayoutInflater inflater; // 布局文件加载器
	private Context mContext; // 当前ListView上下文
	private TextView textView; // textView显示总价，总数量等信息

	public OrderListAdapter(OrderedFood myOrderedFood, Context mContext,
			TextView textView) {
		super();
		this.mContext = mContext;
		this.inflater = LayoutInflater.from(mContext);  // 获取布局文件加载器;
		this.myOrderedFood = myOrderedFood;             //获取已点菜品对象
		this.fList = myOrderedFood.getOrderedList();    //获取已点菜品集合
		this.textView = textView;
	}

	@Override
	public int getCount() {
		return fList.size();
	}

	@Override
	public Object getItem(int position) {
		return fList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 为ListView加载视图项并为退点按钮设置事件处理函数
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Food orderFood = fList.get(position); // 获取food对象
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = inflater.inflate(R.layout.food_ordered_item, null);
			holder.name = (TextView) convertView.findViewById(R.id.txt_name);
			holder.number = (TextView) convertView
					.findViewById(R.id.txt_number);
			holder.price = (TextView) convertView.findViewById(R.id.txt_price);
			holder.comment = (TextView) convertView
					.findViewById(R.id.txt_comment);
			holder.button = (Button) convertView.findViewById(R.id.btn_cancel);
			convertView.setTag(holder);

		} else {
			holder = (Holder) convertView.getTag();
		}

		// 进行数据设置
		holder.name.setText(orderFood.getName());
		holder.price.setText(orderFood.getPrice() + "元/份");
		holder.number.setText(orderFood.getOrderedNum() + "份");
		holder.comment.setText(orderFood.getComment());
		/*
		 * 为退点button设置监听事件； 主要完成两件事：
		 * 1，在orderedList中将退点的food对象删除并修改orderedList对象的属性 2, 更新ListView
		 */
		holder.button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Food cancelFood = fList.get(position);     // 获取当前删除的food对象
				DBService ds = new DBService(mContext);
				ds.deleteOrderFood(cancelFood);            //更新ORDERLIST表
				myOrderedFood.setCount(0);                 //清空myOrderedFood对象
				myOrderedFood.setTotalCost(0);
				myOrderedFood.getOrderedList().clear();
				ds.getOrderedList(myOrderedFood);         //重新从数据库中获取已点菜品对象
				// 通知系统更新ListView
				OrderListAdapter.this.notifyDataSetChanged();
				// 更新textView上的统计信息
				textView.setText("菜品总数：" + myOrderedFood.getCount() + "份" + "      "
						+ "订单总价: " + myOrderedFood.getTotalCost() + "元");
				Toast.makeText(mContext, "退点成功！", Toast.LENGTH_SHORT).show();
			}
		});
		return convertView;
	}

	/**
	 * listView Tag标志
	 * 
	 * @author moon
	 * 
	 */
	protected class Holder {
		TextView comment;
		TextView number;
		TextView name;
		TextView price;
		Button button;
	}
}
