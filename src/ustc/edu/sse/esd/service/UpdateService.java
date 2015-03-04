package ustc.edu.sse.esd.service;

import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import ustc.edu.sse.esd.activity.MainScreen;
import ustc.edu.sse.esd.activity.R;
import ustc.edu.sse.esd.model.Food;
import ustc.edu.sse.esd.util.GetPostUtil;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import android.widget.RemoteViews;
/**
 * 自动更新服务，模拟服务器更新菜品信息，该服务在接受开机广播后启动
 * 将更新菜品信息已通知形式展现
 * Copyright: Copyright (c) 2015-2-316 20:47:25
 * Company: 中国科学技术大学 软件学院
 * 
 * @author moon：代码编写，star：代码整理
 * @version 5.0
 */
public class UpdateService extends IntentService {
	private NotificationManager notificationManager;            //通知管理器
	private NotificationCompat.Builder mBuilder;                //通知构造器
	private ArrayList<Food> update_hot_food = new ArrayList<Food>();  //模拟更新热菜菜品
	public ButtonBroadcastReceiver bReceiver;                    //广播接收器对象
	private int[] imageId = {R.drawable.hot_food_update, R.drawable.seafood_update};  
	/* 通知栏删除通知按钮点击事件对应的ACTION */
	public final static String ACTION_BUTTON = "es.source.code.service.UpdateService.ACTION_BUTTON_CLICK";
	private final static String TAG = "es.source.code.service.UpdateService";
	/* 通知栏按钮广播 */
	public final static String INTENT_BUTTONID_TAG = "es.source.code.service.UpdateService.SendBroadcast";
	public final static int BUTTON_ID = 1;             //按钮点击 ID
	private final static int NOTIFY_ID = 0;            //通知ID
	private final static int UPDATE_NUM = 2;           //模拟更新菜品数量
	private final static String SPEC = "http://10.0.2.2:8080/SCOSServer/FoodUpdateService";  //菜品更新服务器url 

	/**
	 * 注意需要空构造函数
	 */
	public UpdateService() {
		super(TAG);
	}
	@Override
	public void onCreate() {
		super.onCreate();
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);  //得到通知管理器
		initButtonReceiver();  //定义广播接收器，注册广播接收器
	}
	
	/**
	 * 	运行在worker线程中，每一次只能处理一个Intent，其他intent只有在当前处理完成后才一次处理
	 * 所以，不能处理较耗费时间的任务，处理完成后service自动stop
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		Food newFood = null;
		for(int i = 0; i < UPDATE_NUM; i++) {
			try {
				newFood = loginByGet();                
				update_hot_food.add(newFood);              //模拟更新2种热菜
				showNotify(i+1);   //发出通知
				Thread.sleep(10000);                       //每10s发送新菜品更新申请
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @return 
	 * 解析服务器返回的json数据，创建更新food对象
	 * 
	 */
	private Food loginByGet() {
        Food newFood  = null;  
		String result = GetPostUtil.sendGet(SPEC, null);  //使用get方法从服务器获取json字符串
        try {
        	JSONTokener jsonParser = new JSONTokener(result);  
            JSONObject food = (JSONObject) jsonParser.nextValue();
            Log.e(TAG, food.toString());                   //测试接收到的JSON字符串
			String name = food.getString("name");
	        int price = food.getInt("price"); 
	        String comment = food.getString("comment");
	        int food_class = food.getInt("food_class");
	        int leftNum = food.getInt("left_num"); 
	        int index = food.getInt("imageId");
	        newFood  = new Food();
	        newFood.setName(name);
	        newFood.setPrice(price);
	        newFood.setImageId(imageId[index]);
	        newFood.setLeft_num(leftNum);
	        newFood.setComment(comment);
	        newFood.setOrderedNum(0);
	        newFood.setOrdered(false);
	        newFood.setFoodClass(food_class);
		} catch (JSONException e) {
			e.printStackTrace();
		}  
        return newFood;
	}
	
	/**
	 * 构建通知 ；为通知设置点击监听事件
	 */
	private void showNotify(int i) {
		Intent intent = new Intent(this, MainScreen.class);
		intent.putExtra("hint", "UpdateService");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		RemoteViews view_custom = new RemoteViews(getPackageName(),
				R.layout.view_notificatinon);
		view_custom.setImageViewResource(R.id.custom_icon,         // 设置图标
				R.drawable.ic_launcher);                           
		view_custom.setTextViewText(R.id.tv_custom_title, "SCOS"); // 设置标题
		view_custom.setTextViewText(R.id.tv_custom_content,        // 设置内容
				"新品上架：有" + i + "个菜品更新" );
		view_custom.setOnClickPendingIntent(R.id.view_notification,   // 点击通知后回到FoodDetailed
				contentIntent);
		/* 为通知中的清除按钮注册点击监听事件，在按钮点击后发送删除通知广播 */
		Intent btnIntent = new Intent(ACTION_BUTTON);
		btnIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_ID);
		PendingIntent delIntent = PendingIntent.getBroadcast(this, 1,
				btnIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		view_custom.setOnClickPendingIntent(R.id.imgbtn_clean, delIntent);
		/* 构建通知 */
		mBuilder = new Builder(this);
		mBuilder.setContent(view_custom).setContentIntent(contentIntent)
				.setWhen(System.currentTimeMillis()).setTicker("有新品菜")
				.setSmallIcon(R.drawable.ic_launcher);
		Notification notify = mBuilder.build();         
		notify.contentView = view_custom;                   // 为该通知配置视图
		notificationManager.notify(NOTIFY_ID, notify);      //发出通知
	}
	
	/**
	 *  带按钮的通知栏点击广播接收
	 */
	public void initButtonReceiver() {
		bReceiver = new ButtonBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTION_BUTTON);
		registerReceiver(bReceiver, intentFilter);   //注册一个在主线程运行的广播接收器
	}
	
	/** 
	 * 清除当前创建的通知栏 
	 */
	private void clearNotify(int notifyId) {
		notificationManager.cancelAll();  // 删除你发的所有通知
	}
	
	/**
	 *	定义广播接收器，能够接受按钮点击事件发出的广播
	 */
	public class ButtonBroadcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(action.equals(ACTION_BUTTON)) {
				int buttonId = intent.getIntExtra(INTENT_BUTTONID_TAG, 0);
				switch (buttonId) {
				case BUTTON_ID:
					clearNotify(NOTIFY_ID);   //清除特定的通知
					break;
				default:
					break;
				}
			}
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(bReceiver);        //为updateService注销广播接收器
	}
}
