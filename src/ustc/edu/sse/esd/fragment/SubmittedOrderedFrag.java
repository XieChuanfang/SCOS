package ustc.edu.sse.esd.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import ustc.edu.sse.esd.activity.R;
import ustc.edu.sse.esd.db.DBService;
import ustc.edu.sse.esd.model.Food;
import ustc.edu.sse.esd.model.OrderedFood;
import ustc.edu.sse.esd.model.User;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
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
 * 已下单菜fragment：加载相应已下单菜品的ListView Copyright: Copyright (c) 2015-2-3 14：27::25
 * Company: 中国科学技术大学 软件学院
 * 
 * @author moon：代码编写，star：代码整理
 * @version 5.0
 */
public class SubmittedOrderedFrag extends Fragment implements OnClickListener {
	private OrderedFood mySubmittedFood; // 已点但已下单菜品集合
	private Context mContext; // FoodOrderView上下文
	private ListView listView; // ListView对象
	private TextView textView; // 显示未下单菜品信息
	private Button button; // 结账按钮对象
	private User loginUser; // 登陆用户信息
	private ProgressDialog dialog; // 对话框

	public SubmittedOrderedFrag(Context mContext, User loginUser) {
		super();
		this.mContext = mContext;
		this.mySubmittedFood = new OrderedFood();
		this.loginUser = loginUser;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DBService ds = new DBService(mContext);
		ds.getSubmittedList(mySubmittedFood);    //从数据库中获取已提交订单菜品对象

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
		dialog = new ProgressDialog(mContext);
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
	 * @return
	 */
	private List<HashMap<String, Object>> initData() {
		HashMap<String, Object> item = null;
		ArrayList<HashMap<String, Object>> mListItem = null;
		mListItem = new ArrayList<HashMap<String, Object>>();
		int length = mySubmittedFood.getOrderedList().size();
		for (int i = 0; i < length; i++) {
			Food food = mySubmittedFood.getOrderedList().get(i);
			item = new HashMap<String, Object>();
			item.put("name", food.getName());
			item.put("price", food.getPrice() + "元/份");
			item.put("number", food.getOrderedNum() + "份");
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
				UpdatePdTask uptask = new UpdatePdTask(); // 创建模拟结账任务
				uptask.execute(0);      // 开始任务:参数进度初始值
			} else {
				Toast.makeText(mContext, "抱歉，您未登录，请登录", Toast.LENGTH_SHORT)
						.show();                                   // 未登录
			}
		}
	}
	/**
	 * 使用AsyncTask模拟结账功能
	 * 
	 * @author moon
	 * 
	 */
	private class UpdatePdTask extends AsyncTask<Integer, Integer, String> {
		/**
		 * 后台线程更新进度值，调用publishProgress将进度值反映到UI线程中
		 * 进度条的目前精度，每个1s增加1，初始为0
		 */
		@Override
		protected String doInBackground(Integer... params) {
			do {
				params[0] += 1; 
				try {
					publishProgress(params[0]);  //UI线程更新进度
					Thread.sleep(1000);
					if (dialog.getProgress() >= 6)    //完成结账，退出后台线程
						break;
					if (isCancelled())     //取消任务即取消结账，退出后台线程
						break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			} while (dialog.getProgress() <= 6);

			return null;
		}
		/*
		 * 在UI线程中执行，doInBackground返回时候触发 这里的result是doInBackgroud的返回值
		 */
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			button.setVisibility(View.INVISIBLE);   //结账按钮设为不可见
		}
		/*
		 * 这个函数在doInBackground调用publishProgress时候触发，在UI线程中执行，虽然调用时只有一个参数;
		 * 但是这里取到的是一个数组，所以要用progress[0]来取值,第N个参数要用progress[n]来取值
		 */
		@Override
		protected void onProgressUpdate(Integer... values) {
			dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); // 设置进度条风格，风格为圆形，旋转的
			dialog.setTitle("提示");
			dialog.setMessage("正在结账。。。");
			dialog.setIcon(android.R.drawable.ic_dialog_alert);
			dialog.setMax(6);       // 设置ProgressDialog的最大进度
			dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
					new ProgressDialog.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							float totalCost = mySubmittedFood.getTotalCost();
							if (loginUser.isOldUser()) {           // 老顾客，打7折
								totalCost = (int) (mySubmittedFood
										.getTotalCost() * 0.7); 
							}
							// Toast信息：每消费10元增加1积分
							String hint = "本次付款金额"		
									+ totalCost + "元," + "增加" + totalCost
									/ 10
									+ "个积分";
							Toast.makeText(mContext, hint, Toast.LENGTH_SHORT)
									.show();
							
							// 结账完成后清空已下单菜
							mySubmittedFood.getOrderedList().clear();
							mySubmittedFood.setCount(0);
							mySubmittedFood.setTotalCost(0);
							DBService ds = new DBService(mContext);
							ds.clearSubmitList();   //清空数据库表SUBMITLIST
						}

					});
			dialog.setCancelable(true); // 设置ProgressDialog 按退回按键不可以取消
			dialog.show();     // 显示对话框
			dialog.setProgress(values[0]);
		}
	}
}
