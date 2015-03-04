package ustc.edu.sse.esd.db;

import ustc.edu.sse.esd.activity.R;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * 数据库操作管理类
 * Copyright: Copyright (c) 2015-3-3 9:47:25
 * Company: 中国科学技术大学 软件学院
 * 
 * @author moon：代码编写，star：代码整理
 * @version 5.0
 */
public class DBHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "scos_db";
	private static final int VERSION = 1;
	private Context mContext;

	public DBHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
		mContext = context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		 String createFood = mContext.getResources()
					.getString(R.string.createFood).toString();
		 String createOrder = mContext.getResources()
				 .getString(R.string.createOrder).toString();
		 String createSubmittedOrder = mContext.getResources()
				 .getString(R.string.createSubmittedOrder).toString();
		 db.execSQL(createFood);               //创建food表
		 db.execSQL(createOrder);              //创建order表
		 db.execSQL(createSubmittedOrder);     //创建SUBMITTEDORDER表
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
}
