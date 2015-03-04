package ustc.edu.sse.esd.br;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * 开机广播接收器，然后启动updateService服务
 * Copyright: Copyright (c) 2015-2-316 20:47:25
 * Company: 中国科学技术大学 软件学院
 * 
 * @author moon：代码编写，star：代码整理
 * @version 5.0
 */
public class DeviceStartedListener extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		   Intent in = new Intent();  
	       in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		   in.setAction("android.intent.action.update_service");
	       context.startService(in); 
	}
}
