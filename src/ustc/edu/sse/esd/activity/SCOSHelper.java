package ustc.edu.sse.esd.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import es.source.code.mail.MailSenderInfo;
import es.source.code.mail.SimpleMailSender;
/**
 * 帮助导航项类
* Copyright: Copyright (c) 2015-2-5 20:47:25
* Company: 中国科学技术大学 软件学院
* 
* @author moon：代码编写，star：代码整理
* @version 2.0
*/
public class SCOSHelper extends Activity {
	//帮助导航项图标
	private int[] drawables = { R.drawable.user_agreement,                              
			R.drawable.about_system, R.drawable.man_help, R.drawable.sms_help,
			R.drawable.mail_help };                                              
	//帮助导航项文本说明
	private String[] names = new String[] { "用户使用协议", "关于系统", "电话人工帮助", "短信帮助", 
			"邮件帮助" };
	private LayoutInflater inflater; //布局文件加载器
	private GridView gridView; //GridView对象
	//自定义ACTION常数，作为广播的Intent Filter识别常数
	private final static String SMS_SEND_ACTIOIN = "SMS_SEND_ACTIOIN";  
	private final static String SMS_DELIVERED_ACTION = "SMS_DELIVERED_ACTION";
	private final static int PROTOCOL_INDEX = 0; //用户使用协议
	private final static int ABOUT_SYSTEM_INDEX = 1; //关于系统 
	private final static int PHONE_HELP_INDEX = 2; //电话人工帮助
	private final static int SMS_HELP_INDEX = 3; //短信帮助
	private final static int MAIL_HELP_INDEX = 4; //邮件帮助
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		inflater = LayoutInflater.from(this);
		gridView = (GridView) findViewById(R.id.gridview); 
		
		displayMenu();   //适配gridView，加载视图
	}
	
	/**
	 * 显示导航项
	 */
	private void displayMenu() {
		// 初始化数据源
		ArrayList<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < drawables.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("imgItem", drawables[i]);
			map.put("txtItem", names[i]);
			items.add(map);
		}

		/* 创建适配器 */
		SimpleAdapter adapter = new SimpleAdapter(this, items,
				R.layout.help_item,
				new String[] { "imgItem", "txtItem" }, new int[] {
						R.id.img_item, R.id.txt_item });

		/* 为gridView设置适配器 */
		gridView.setAdapter(adapter);
		/* 为gridView导航项设置点击处理事件 */
		gridView.setOnItemClickListener(new ItemClickListener());
	}

	/**
	 * 导航项点击处理监听对象 
	 */
	private class ItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
		switch (position) {
				case PROTOCOL_INDEX:
					break;
				case ABOUT_SYSTEM_INDEX:
					break;
				case PHONE_HELP_INDEX://电话人工帮助 
					phoneCall(); 
					break;
				case SMS_HELP_INDEX://发送短息服务
					String phoneNumber = "5554"; 
					String message = "test scos helper";                
					sendSMS(phoneNumber, message); 
					Toast.makeText(SCOSHelper.this, "求助短信发送成功",
							Toast.LENGTH_SHORT).show();
					break;
				case MAIL_HELP_INDEX: //创建线程发送邮件
					new Thread(new MailSender()).start();    
					break;
				default:
					break;
			}
		}
		
		/**
		 * 拨打电话
		 */
		private void phoneCall() {
			Uri uri = Uri.parse("tel:" + "5554");
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_DIAL);
			intent.setData(uri);
			startActivity(intent);
		}

		/**
		 * 发送短信
		 */
		private void sendSMS(String phoneNumber, String message) {
			SmsManager sms = SmsManager.getDefault(); 	
			/*创建发送intent*/
			Intent sentIntent = new Intent(SMS_SEND_ACTIOIN); 
			PendingIntent sentPI = PendingIntent.getBroadcast(SCOSHelper.this, 0, sentIntent, 0); 
			/*创建提交intent*/
			Intent deliverIntent = new Intent(SMS_DELIVERED_ACTION);  
			PendingIntent deliverPI = PendingIntent.getBroadcast(SCOSHelper.this, 0,  deliverIntent, 0); 
			
			//如果短信内容超过70个字符 将这条短信拆成多条短信发送出去  
			if (message.length() > 70) {  
			    ArrayList<String> msgs = sms.divideMessage(message);  
			    for (String msg : msgs) {  
			    	sms.sendTextMessage(phoneNumber, null, msg, sentPI, deliverPI);  
			    }  
			} else {  
			    sms.sendTextMessage(phoneNumber, null, message, sentPI, deliverPI);  
			}  
		}
	}
	
	/**
	 * 使用Handler用来更新UI线程
	 */
	Handler h = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				Toast.makeText(SCOSHelper.this, "求助邮件发送成功", Toast.LENGTH_SHORT)
						.show();
			}
		}
	};
	
	/**
	 * 使用java mail发送邮件：需要使用三个库：activition.jar,additional.jar和mail.jar
	 * @author moon
	 */
	private class MailSender extends Thread {

		@Override
		public void run() {
			MailSenderInfo mailInfo = new MailSenderInfo(); //设置邮件格式
		    mailInfo.setMailServerHost("stmp.163.com");     
			mailInfo.setMailServerPort("25");	  
			mailInfo.setValidate(true);
			mailInfo.setUserName("#########@163.com");	 	
			mailInfo.setPassword("#############"); //您的邮箱密码
			mailInfo.setFromAddress("#########@163.com");      
			mailInfo.setToAddress("#########@163.com"); 
			mailInfo.setSubject("设置邮箱标题");       
			mailInfo.setContent("设置邮箱内容");
			SimpleMailSender sms = new SimpleMailSender(); //发送邮件
			/*发送文体格式 */
			if(sms.sendTextMail(mailInfo)) {
				Message msg = h.obtainMessage();
				msg.what = 1;
				h.sendMessage(msg);
			}
		}
	}
}
