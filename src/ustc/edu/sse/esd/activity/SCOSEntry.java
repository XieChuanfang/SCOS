package ustc.edu.sse.esd.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
/**
 * scos引导界面，左划后今进入mainScreen主导航界面 
 * Copyright: Copyright (c) 2015-2-3 20：7:25
 * Company: 中国科学技术大学 软件学院
 * 
 * @author moon：代码编写，star：代码整理
 * @version 2.0
 */

public class SCOSEntry extends Activity implements OnTouchListener,
		OnGestureListener {
	private int verticalMinDistance = 20; // 左滑的最短距离
	private int minVelocity = 0; // 最小速度
	private GestureDetector mGestureDetector; // 手势监听者

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entry); // 加载配置文件

		mGestureDetector = new GestureDetector(this, this); // 创建手势检测器

		RelativeLayout entryLayout = (RelativeLayout) findViewById(R.id.entry_layout);
		entryLayout.setOnTouchListener(this); // 为当前view设置
		entryLayout.setLongClickable(true);
	}

	/**
	 * 手势活动处理事件
	 */
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// 向左滑动判断
		if (e1.getX() - e2.getX() > verticalMinDistance
				&& Math.abs(velocityX) > minVelocity) {
			Intent intent = new Intent(SCOSEntry.this, MainScreen.class);
			intent.putExtra("hint", "FromEntry");
			startActivity(intent);
			finish(); // 销毁scosEntry
		}

		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// 触屏事件由手势检测器检测处理
		return mGestureDetector.onTouchEvent(event);
	}
	

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}
}
