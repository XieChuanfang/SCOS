package ustc.edu.sse.esd.activity;

import android.app.Activity;
import android.os.Bundle;

/**
 * 
 * SCOSEntry Activity.
 * 加载一个logo图片
 * Copyright: Copyright (c) 2015-1-31 下午4:23:25
 * <p>
 * Company: 中国科学技术大学   软件学院
 *
 * 
 * @author moon
 * @version 1.0.0
 */
public class SCOSEntry extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entry); // 加载配置文件
	}
}
