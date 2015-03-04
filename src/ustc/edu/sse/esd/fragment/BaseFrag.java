package ustc.edu.sse.esd.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import ustc.edu.sse.esd.activity.FoodDetailed;
import ustc.edu.sse.esd.activity.R;
import ustc.edu.sse.esd.adapter.FoodListAdapter;
import ustc.edu.sse.esd.db.DBService;
import ustc.edu.sse.esd.model.Food;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * 菜品的fragment：加载相应菜品的ListView
 * Copyright: Copyright (c) 2015-2-4 10:47:25
 * Company: 中国科学技术大学 软件学院
 * 
 * @author moon：代码编写，star：代码整理
 * @version 5.0
 */
public class BaseFrag extends Fragment {
	private Context mContext;        // FoodView上下文
	private ArrayList<Food> fList;   // 相应菜品的Food类集合
	private FoodListAdapter adapter;  
	private int classId;             //当前fragment加载的菜品类别ID

	/* 构造fragment */
	public BaseFrag(int classId, Context context) {
		this.mContext = context;
		this.classId = classId;
		fList = new ArrayList<Food>();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DBService ds = new DBService(mContext);
		ds.select(classId, fList);         //从数据库表FOOD中加载当前菜品类别的菜品数据
	}

	/**
	 * 为当前fragment创建和加载视图View
	 */
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View mMainView = inflater.inflate(R.layout.food_list, container, false); // 用布局加载器加载VIEW
		ListView listView = (ListView) mMainView.findViewById(R.id.food_list); // 创建ListView对象

		// 初始化适配器，将已得到的food集合数据和当前上下文出传入适配器中
		adapter = new FoodListAdapter(mContext, fList);
		listView.setAdapter(adapter);
		// 为ListView设置监听器:点击ListView其他区域跳转到下品详细信息视图中
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(), FoodDetailed.class);
				intent.putExtra("food_list", (Serializable) fList);
				intent.putExtra("position", position);
				startActivity(intent);  
			}
		});

		return mMainView;
	}
}
