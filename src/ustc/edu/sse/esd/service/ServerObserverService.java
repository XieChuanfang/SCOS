package ustc.edu.sse.esd.service;

import java.util.ArrayList;
import java.util.List;

import ustc.edu.sse.esd.model.Food;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
/**
 * 接受服务端传回的库存信息，并将库存信息反馈到UI中
 * Copyright: Copyright (c) 2015-2-10 19:47:25
 * Company: 中国科学技术大学 软件学院
 * 
 * @author moon：代码编写，star：代码整理
 * @version 3.0
 */
public class ServerObserverService extends Service {
	private Messenger sm = null;                       //server进程messenger
	private Messenger cm = null;                       //client进程messenger
	private boolean isUpdate = false;                  //是否需要更新
	private ArrayList<Food> cool_food = new ArrayList<Food>();
	private ArrayList<Food> hot_food = new ArrayList<Food>();
	private ArrayList<Food> seafood = new ArrayList<Food>();
	private ArrayList<Food> drinks = new ArrayList<Food>();
	
	/**
	 * 返回用于该Messenger用来与Handler沟通的IBinder对象
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return sm.getBinder(); 
	}

	@Override
	public void onCreate() {
		super.onCreate();
		sm = new Messenger(cMessageHandler);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		sm = null;
	}
	/**
	 * 本进程handler
	 */
	Handler cMessageHandler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 1) {
				Bundle data = msg.getData();
				/*这个类我是通过将其放入Bundle中传递的，所以在从Bundle中取的时候需要先设置ClassLoader，用于从parcel重新构造出来*/
				data.setClassLoader(Food.class.getClassLoader());  
				ArrayList list = data.getParcelableArrayList("update_food_request");
				cool_food = (ArrayList<Food>) list.get(0);   //强转成你自己定义的list，这样list2就是你传过来的那个list了。
				hot_food = (ArrayList<Food>) list.get(1);
				seafood = (ArrayList<Food>) list.get(2);
				drinks = (ArrayList<Food>) list.get(3);
				cm = msg.replyTo;
				isUpdate = true;
				ServiceThread st = new ServiceThread();  
				st.start();  //启动线程完成库存的查询
			} else if(msg.what == 0) {
				isUpdate = false;
				System.out.println("update Thread stoped!");
			}
		}
	};
	
	/**
	 * 线程用来更新库存信息
	 * @author moon
	 *
	 */
	class ServiceThread extends Thread {
		@Override
		public void run() {
			while(isUpdate) {
				Message m = Message.obtain();   //从消息池中得到消息实例，避免每次创建新消息对象
				m.what = 10;
				Bundle data = new Bundle();
				/*更新菜品库存量*/
				updateInfo(cool_food); 
				updateInfo(hot_food); 
				updateInfo(seafood);  
				updateInfo(drinks); 
				ArrayList list = new ArrayList();
				list.add(cool_food);
				list.add(hot_food);
				list.add(seafood);
				list.add(drinks);
				data.putParcelableArrayList("update_food_finished", list);
				m.setData(data);
				try {
					/*判断scos是否还在运行，如果是，则发送库存更新消息到SCOS*/
					if(isRunning(getApplicationContext(), "ustc.edu.sse.esd.activity")) {  
						cm.send(m);     //发送消息
					}
					Thread.sleep(300);  //休眠300ms
				} catch (RemoteException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		/**
		 * 判断scos进程是否还在运行
		 */
		private boolean isRunning(Context applicationContext, String string) {
			ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
			List<ActivityManager.RunningAppProcessInfo> processList = activityManager.getRunningAppProcesses();
			for (ActivityManager.RunningAppProcessInfo process : processList) {
				if (process.processName.contains(string)) {
					return true;
				}
			}
			return false;
		}
		/**
		 * 模拟服务器更新菜品库存量：这里将每种菜品库存量减1
		 */
		private void updateInfo(ArrayList<Food> foodTypeList) {
			Food temp = null;
			int leftNum;
			int length = foodTypeList.size();
			for(int i=0; i < length; i++) {
				temp = foodTypeList.get(i);
				leftNum = temp.getLeftNum();
				temp.setLeft_num(--leftNum); 
			}
		}
	}
}
