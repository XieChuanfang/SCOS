package ustc.edu.sse.esd.activity;

import android.app.Activity;
import android.app.SearchManager.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * scos引导界面，左划后今进入mainScreen主导航界面
 * Copyright: Copyright (c) 2015-2-1 16:47:25
 * Company: 中国科学技术大学   软件学院
 * @author moon
 * @version 1.1
 */
public class SCOSEntry extends Activity implements OnTouchListener, OnGestureListener  {

	private int verticalMinDistance = 20;  
	private int minVelocity         = 0; 
	private GestureDetector mGestureDetector;  
	 
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entry); // 加载配置文件
		
	     mGestureDetector = new GestureDetector((OnGestureListener) this);      //创建手势检测器
	     
	     RelativeLayout entryLayout = (RelativeLayout)findViewById(R.id.entry_layout);    
	     entryLayout.setOnTouchListener(this);      //为当前view设置
	     entryLayout.setLongClickable(true);  
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 手势活动处理事件
	 */
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
	    if (e1.getX() - e2.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {  
	        Intent intent = new Intent(SCOSEntry.this, MainScreen.class); 
	        intent.putExtra("myhint", "FromEntry");
	        startActivity(intent);  
	        Toast.makeText(this, "向左手势", Toast.LENGTH_SHORT).show();  
	        finish();                    //结束scosEntry
	    } else if (e2.getX() - e1.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {  
	    	
	        Toast.makeText(this, "向右手势", Toast.LENGTH_SHORT).show();  
	    }  
		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return mGestureDetector.onTouchEvent(event);    //触屏事件由手势检测器检测处理
	}
}
