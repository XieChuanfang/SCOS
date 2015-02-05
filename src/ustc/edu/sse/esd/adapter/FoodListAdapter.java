package ustc.edu.sse.esd.adapter;

import java.util.ArrayList;

import ustc.edu.sse.esd.activity.FoodOrderView;
import ustc.edu.sse.esd.activity.R;
import ustc.edu.sse.esd.model.Food;
import ustc.edu.sse.esd.model.OrderedFood;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 自定义FoodListAdapter：用于将Food数据信息适配到ListView中去
 * Copyright: Copyright (c) 2015-2-4 10:47:25
 * Company: 中国科学技术大学 软件学院
 * 
 * @author moon：代码编写，star：代码整理
 * @version 2.0
 */
public class FoodListAdapter extends BaseAdapter {
	private Holder holder; // 消息传递机制
	private Context mContext; // 上下文
	private LayoutInflater inflater; // 填充器
	private ArrayList<Food> fList; // 菜品集合
	private Food food; // 菜品
	private OrderedFood myOrderedFood; // 已点菜品集合对象，即一个订单

	public FoodListAdapter(Context mContext, ArrayList<Food> fList) {
		super();
		this.mContext = mContext; // 获取当前的上下文
		this.fList = fList;
		this.inflater = LayoutInflater.from(mContext); // 获取布局文件加载器
		this.myOrderedFood = FoodOrderView.myOrderedFood;
	}

	/**
	 * 获取数据集合大小
	 */
	@Override
	public int getCount() {
		return fList.size();
	}

	/**
	 * 获取当前位置的对象
	 */
	@Override
	public Object getItem(int position) {
		return fList.get(position);
	}

	/**
	 * 获取在列表中与指定索引对应的行id
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 获取一个在数据集中指定索引的视图来显示数据
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		food = fList.get(position); // 从数据源中取出food对象
		if (convertView == null) {
			holder = new Holder(); // 创建视图项对象
			convertView = inflater.inflate(R.layout.food_list_item, null); // 为ListView加载视图项
			holder.image = (ImageView) convertView.findViewById(R.id.food_img);
			holder.name = (TextView) convertView.findViewById(R.id.txt_name);
			holder.price = (TextView) convertView.findViewById(R.id.txt_price);
			holder.button = (Button) convertView.findViewById(R.id.btn_order);
			// 将ListView视图项内组件对象保存到缓存中，并将其设置为listView视图项的Tag，以后就可以不用重新创建组件对象了
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		// 进行数据设置
		holder.name.setText(food.getName());
		holder.price.setText(food.getPrice() + "元/份");
		holder.button.setText(R.string.txt_order);
		holder.image.setImageResource(food.getImageId());
		final Button button = holder.button;

		/**
		 * 为点菜按钮设置监听点击事件，将相应food对象加到已点菜品集合中,并设置myOrderedFood对象属性
		 */
		holder.button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Food food = fList.get(position); // 重新获取点击位置的food对象，
				food.setOrdered(true); // 将该菜品的点菜状态设为已点
				button.setText(R.string.btn_cancel_order); // 修改按钮为“退点”
				myOrderedFood.getOrderedList().add(food); // 将该food实例加入“已点菜品”集合
				int tempCount = myOrderedFood.getCount();
				myOrderedFood.setCount(++tempCount); // 修改数量
				int tempTotalCost = myOrderedFood.getTotalCost();
				myOrderedFood.setTotalCost(tempTotalCost + food.getPrice()); // 修改总价
				Toast.makeText(mContext, "点菜成功！", Toast.LENGTH_SHORT).show();
			}
		});

		return convertView;
	}

	/**
	 * ListView Tag标记
	 * 
	 * @author moon
	 * 
	 */
	protected class Holder {
		ImageView image;
		TextView name;
		TextView price;
		Button button;
	}

}
