package ustc.edu.sse.esd.service;

import java.util.ArrayList;

import ustc.edu.sse.esd.activity.FoodDetailed;
import ustc.edu.sse.esd.activity.R;
import ustc.edu.sse.esd.model.Food;
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
import android.widget.RemoteViews;
/**
 * 自动更新服务，模拟服务器更新菜品信息，该服务在接受开机广播后启动
 * 将更新菜品信息已通知形式展现
 * Copyright: Copyright (c) 2015-2-316 20:47:25
 * Company: 中国科学技术大学 软件学院
 * 
 * @author moon：代码编写，star：代码整理
 * @version 3.0
 */
public class UpdateService extends IntentService {
	private NotificationManager notificationManager;   //通知管理器
	private NotificationCompat.Builder mBuilder;       //通知构造器
	private ArrayList<Food> update_hot_food = new ArrayList<Food>();  //模拟更新热菜菜品
	private final static int NOTIFY_ID = 0;            //通知ID
	public ButtonBroadcastReceiver bReceiver;   //广播接收器对象
	/** 通知栏按钮点击事件对应的ACTION */
	public final static String ACTION_BUTTON = "es.source.code.service.UpdateService.ACTION_BUTTON_CLICK";
	private final static String TAG = "es.source.code.service.UpdateService";
	/** 通知栏按钮广播 */
	public final static String INTENT_BUTTONID_TAG = "es.source.code.service.UpdateService.SendBroadcast";
	/**按钮点击 ID */
	public final static int BUTTON_ID = 1;
	
	/**
	 * 注意需要空构造函数
	 */
	public UpdateService() {
		super(TAG);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		/*得到通知管理器*/
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE); 
		initButtonReceiver();  //定义广播接收器，注册广播接收器
	}
	/**
	 * 	运行在worker线程中，每一次只能处理一个Intent，其他intent只有在当前处理完成后才一次处理
	 * 所以，不能处理较耗费时间的任务，处理完成后service自动stop
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		initUpdateFood(); 
		for(int i = 0; i < update_hot_food.size(); i++) {
			try {
				showNotify(update_hot_food, update_hot_food.get(i), i);   //发出通知
				Thread.sleep(10000);                                     //每10s发送新菜品更新申请
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 初始化新菜品信息
	 */
	private void initUpdateFood() {
		Food f1 = new Food();
		f1.setName("炒虾仁");
		f1.setFoodClass(2);
		f1.setImageId(R.drawable.hot_food_update);
		f1.setPrice(20);
		f1.setComment("很香哦");
		f1.setOrdered(false);
		f1.setOrderedNum(0);
		update_hot_food.add(f1);
		Food f2 = new Food();
		f2.setName("清蒸鲈鱼");
		f2.setFoodClass(2);
		f2.setImageId(R.drawable.seafood_update);
		f2.setPrice(35);
		f2.setComment("很鲜");
		f2.setOrdered(false);
		f2.setOrderedNum(0);
		update_hot_food.add(f2);
	}
	/**
	 * 构建通知
	 */
	private void showNotify(ArrayList<Food> fList, Food newFood, int position) {
		Intent intent = new Intent(this, FoodDetailed.class);  
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("food_list", fList);
		intent.putExtra("position", position); 
		
		/*点击通知后回到FoodDetailed主界面*/
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		RemoteViews view_custom = new RemoteViews(getPackageName(), R.layout.view_notificatinon);  
		view_custom.setImageViewResource(R.id.custom_icon, R.drawable.ic_launcher);   //设置图标
		view_custom.setTextViewText(R.id.tv_custom_title, "SCOS");                     //设置标题
		view_custom.setTextViewText(R.id.tv_custom_content, "新品上架： " + newFood.getName() + "  " + newFood.getPrice()
				+ "元/份   " + "热菜");
		view_custom.setOnClickPendingIntent(R.id.view_notification, contentIntent);  
		
		/*为清除按钮注册点击监听事件，在按钮点击后发送该PendIntent,清除通知*/
		Intent buttonIntent = new Intent(ACTION_BUTTON);
		buttonIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_ID);
		PendingIntent deleteIntent = PendingIntent.getBroadcast(this, 1, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		view_custom.setOnClickPendingIntent(R.id.imgbtn_clean, deleteIntent); 
		
		/*构建通知*/
		mBuilder = new Builder(this);
		mBuilder.setContent(view_custom)
		.setContentIntent(contentIntent)
		.setWhen(System.currentTimeMillis())    
		.setTicker("有新品菜") 
		.setSmallIcon(R.drawable.ic_launcher);	
		Notification notify = mBuilder.build();   
		notify.contentView = view_custom;        //为该通知配置视图
		notificationManager.notify(NOTIFY_ID, notify); 
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
	
	@Override
	public void onDestroy() {
		unregisterReceiver(bReceiver);   //为updateService注销广播接收器
		super.onDestroy();
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
					clearNotify(NOTIFY_ID);  //清除特定的通知
					break;
				default:
					break;
				}
			}
		}
	}
}
