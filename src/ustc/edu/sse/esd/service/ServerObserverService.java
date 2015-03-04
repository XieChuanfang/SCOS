package ustc.edu.sse.esd.service;

import java.util.List;
import ustc.edu.sse.esd.db.DBService;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
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
 * @version 5.0
 */
public class ServerObserverService extends Service {
	private Messenger sm = null;                       //server进程messenger
	private Messenger cm = null;                       //client进程messenger
	private boolean isUpdate = false;                  //是否需要更新

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

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 1) {
				cm = msg.replyTo;
				isUpdate = true;
				ServiceThread st = new ServiceThread();  
				st.start();                     //启动线程完成数据库菜品库存信息的更新
			} else if(msg.what == 0) {
				isUpdate = false;
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
				DBService ds = new DBService(ServerObserverService.this); 
				ds.updateLeftNum();                 //更新菜品库存量
				try {
					/*判断scos是否还在运行，如果是，则发送库存更新消息到SCOS*/
					if(isRunning(getApplicationContext(), "ustc.edu.sse.esd.activity")) {  
						cm.send(m);     //发送消息
					}
					Thread.sleep(5000);  //休眠5s
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
	}
}
