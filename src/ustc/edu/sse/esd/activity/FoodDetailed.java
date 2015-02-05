package ustc.edu.sse.esd.activity;

import java.util.ArrayList;
import ustc.edu.sse.esd.adapter.MyPagerAdapter;
import ustc.edu.sse.esd.model.Food;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 菜品详细信息，使用组件ViewPager视图显示
 * Copyright: Copyright (c) 2015-2-4 15:47:25
 * Company: 中国科学技术大学 软件学院
 * 
 * @author moon：代码编写，star：代码整理
 * @version 2.0
 */
public class FoodDetailed extends Activity {
	private ImageView imageView;
	private TextView nameView;
	private TextView priceView;
	private Button button;
	private EditText commentEdit;
	private ViewPager mPager;
	private ArrayList<View> views;  // 视图项集合，每一个View用来加载一种菜品的详细信息
	private int itemPosition = 0;    //用来表示最初显示菜信息的集合的下表
	private ArrayList<Food> fList;  // 当前菜品类菜的信息集合

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.food_view); // 加载布局文件，只有一个ViewPager组件
		mPager = (ViewPager) findViewById(R.id.pager);
		LayoutInflater mInflater = getLayoutInflater(); // 得到当前上下文的的布局加载器
		views = new ArrayList<View>();

		itemPosition = getIntent().getIntExtra("position", 0); // 获得要查看信息的菜品下标地址
		fList = (ArrayList<Food>) getIntent().getSerializableExtra("food_list");
		for (int i = 0; i < fList.size(); i++) {
			View view = mInflater.inflate(R.layout.food_detailed, null); // 加载布局文件，显示菜信息的详细内容
			imageView = (ImageView) view.findViewById(R.id.image);
			nameView = (TextView) view.findViewById(R.id.name);
			priceView = (TextView) view.findViewById(R.id.price);
			button = (Button) view.findViewById(R.id.button);
			commentEdit = (EditText) view.findViewById(R.id.comment);
			/* 设置菜名详细信息 */
			Food f = fList.get(i);
			imageView.setImageResource(f.getImageId());
			nameView.setText(f.getName());
			priceView.setText(f.getPrice() + "元/份");
			commentEdit.setText(f.getComment());

			/* 如果菜已点，设置按钮为退点，否则，设为点菜 */
			if (f.isOrdered()) {
				button.setText(R.string.btn_cancel_order);
			} else {
				button.setText(R.string.txt_order);
			}

			views.add(view); // 将当前View加到集合中
		}

		/* 为ViewPager设置适配器 */
		mPager.setAdapter(new MyPagerAdapter(views));
		/* 显示当前位置菜品信息 */
		mPager.setCurrentItem(itemPosition);
	}
}
