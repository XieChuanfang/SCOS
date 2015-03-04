package ustc.edu.sse.esd.db;

import java.util.ArrayList;
import ustc.edu.sse.esd.model.Food;
import ustc.edu.sse.esd.model.OrderedFood;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
/**
 * 数据库操作类
 * Copyright: Copyright (c) 2015-3-4 10:47:25
 * Company: 中国科学技术大学 软件学院
 * 
 * @author moon：代码编写，star：代码整理
 * @version 5.0
 */
public class DBService {
	private DBHelper helper;       //数据库操作对象
	private SQLiteDatabase db;     //数据库对象
	public DBService(Context context) {
		helper = new DBHelper(context);
	}
	/**
	 * 将菜品信息插入Food表中
	 */
	public void insertFood(Food food) {
		db = helper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("name", food.getName());
		cv.put("price", food.getPrice());
		cv.put("imageId", food.getImageId());
		cv.put("comment", food.getComment());
		cv.put("classId", food.getFoodClass());
		cv.put("leftNum", food.getLeftNum());
		if(food.isOrdered())
			cv.put("isOrdered", 1);
		else 
			cv.put("isOrdered", 0);
		db.insert("FOOD", null, cv);
	}
	
	/**
	 * 将已点菜品信息插入Order表中
	 */
	public void insertOrder(Food food) {
		db = helper.getWritableDatabase();
		String name = food.getName();
		Cursor cursor = db.rawQuery("select * from ORDERLIST where name = ?", new String[] {name});
		boolean flag = cursor.moveToFirst(); 
		if(!flag) {               //当前菜品不在order表中，则插入表中
			ContentValues cv = new ContentValues();
			cv.put("name", food.getName());
			cv.put("orderedNum", 1);
			cv.put("isSubmitted", 0);
			db.insert("ORDERLIST", null, cv);
		} else {                                   //当前菜品已在order表中，则更新已订数量
			int orderedNum = cursor.getInt(cursor.getColumnIndex("orderedNum"));
			ContentValues cv = new ContentValues();
			cv.put("orderedNum",  ++orderedNum);
			db.update("ORDERLIST", cv, "name = ?", new String[] {name});
		}
	}
	
	/**
	 * 清空数据库表FOOD
	 */
	public void clearFood() {
		db = helper.getWritableDatabase();
		db.execSQL("delete from FOOD");
	}
	/**
	 * 清空数据库表ORDERLIST
	 */
	public void clearOrderList() {
		db = helper.getWritableDatabase();
		if(db != null) {
			db.execSQL("delete from ORDERLIST");
		}
	}
	/**
	 * 清空数据库表SUBMITLIST
	 */
	public void clearSubmitList() {
		db = helper.getWritableDatabase();
		if(db != null) {
			db.execSQL("delete from SUBMITLIST");
		}
	}
	/**
	 * 从数据库中加载当前类别的菜品数据到集合中
	 */
	public void select(int i, ArrayList<Food> fList) {
		db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from FOOD where classId = ?", new String[] {i+""}); 
		while (cursor.moveToNext()) {
			Food f = new Food();
			String name = cursor.getString(cursor.getColumnIndex("name"));
			int price = cursor.getInt(cursor.getColumnIndex("price"));
			int imageId = cursor.getInt(cursor.getColumnIndex("imageId"));
			String comment = cursor.getString(cursor.getColumnIndex("comment"));
			int leftNum = cursor.getInt(cursor.getColumnIndex("leftNum"));
			int classId = cursor.getInt(cursor.getColumnIndex("classId"));
			int isOrdered = cursor.getInt(cursor.getColumnIndex("isOrdered"));
			f.setName(name);
			f.setPrice(price);
			f.setComment(comment);
			f.setLeft_num(leftNum);
			f.setImageId(imageId);      
			f.setFoodClass(classId);
			if(isOrdered == 0)
				f.setOrdered(false);
			else 
				f.setOrdered(true);
			fList.add(f);
		}
	}
	
	/**
	 * 点击点菜按钮后更新数据库
	 * @param name：菜品名
	 */
	public void update(Food food) {
		db = helper.getWritableDatabase();
		String name = food.getName();	
		ContentValues cv = new ContentValues();
		cv.put("isOrdered", 1);
		db.update("FOOD", cv, "name = ?", new String[] {name});  
	}
	
	/**
	 * 获取已点菜品集合：作为未下单菜数据源
	 * @param myOrderedFood:已点菜品对象
	 */
	public void getOrderedList(OrderedFood myOrderedFood) {
		db = helper.getWritableDatabase();
		Cursor cursor1 = db.rawQuery("select * from ORDERLIST where isSubmitted = ?", new String[] {"0"});
		int count = 0;
		int totalCost = 0;
		while(cursor1.moveToNext()) {
			Food f = new Food();
			String name = cursor1.getString(cursor1.getColumnIndex("name"));
			int orderedNum = cursor1.getInt(cursor1.getColumnIndex("orderedNum"));
			
			Cursor cursor2 = db.rawQuery("select * from FOOD where name = ?", new String[] {name});  //联结FOOD表获取price和comment信息
			int price = 0;
			String comment = null;
			if(cursor2.moveToFirst()) {
			     price = cursor2.getInt(cursor2.getColumnIndex("price"));
				 comment = cursor2.getString(cursor2.getColumnIndex("comment"));
			}
			count = count + orderedNum;               //获取订单中菜品数量
			totalCost = totalCost + orderedNum * price;  //获取订单总价
			f.setName(name);
			f.setPrice(price);
			f.setComment(comment);
			f.setOrderedNum(orderedNum);
			myOrderedFood.getOrderedList().add(f);
		}
		myOrderedFood.setCount(count);
		myOrderedFood.setTotalCost(totalCost);
	}
	
	/**
	 * 已下单菜退点按钮更新数据源
	 * 如果已点菜品数量大于1，则更新菜品数量
	 * 如果已点菜品数量等于1，则删除该菜品
	 */
	public void deleteOrderFood(Food food) {
		db = helper.getWritableDatabase();
		String name = food.getName();
		Log.e("delete name ", name);
		Cursor cursor = db.rawQuery("select * from ORDERLIST where name = ?", new String[] {name});
		boolean flag = cursor.moveToFirst(); 
		if(flag) { 
			int orderedNum = cursor.getInt(cursor.getColumnIndex("orderedNum"));
			if(orderedNum > 1) {                     
				ContentValues cv = new ContentValues();
				cv.put("orderedNum", --orderedNum);
				db.update("ORDERLIST", cv, "name = ?", new String[] {name});
			} else {
				db.delete("ORDERLIST",  "name = ?", new String[] {name});
			}
		}
	}
	
	/**
	 * 获取已下单菜品集合对象：作为已下单菜数据源
	 * @param myOrderedFood:已下单菜品对象
	 */
	public void getSubmittedList(OrderedFood mySubmittedList) {
		db = helper.getWritableDatabase();
		Cursor cursor1 =  db.rawQuery("select * from SUBMITLIST", null);
		int count = 0;
		int totalCost = 0;
		while(cursor1.moveToNext()) {
			Food f = new Food();
			String name = cursor1.getString(cursor1.getColumnIndex("name"));
			int orderedNum = cursor1.getInt(cursor1.getColumnIndex("orderedNum"));
			Cursor cursor2 = db.rawQuery("select * from FOOD where name = ?", new String[] {name});  //联结FOOD表获取price和comment信息
			int price = 0;
			String comment = null;
			if(cursor2.moveToFirst()) {
			     price = cursor2.getInt(cursor2.getColumnIndex("price"));
				 comment = cursor2.getString(cursor2.getColumnIndex("comment"));
			}
			count = count + orderedNum;               //获取订单中菜品数量
			totalCost = totalCost + orderedNum * price;  //获取订单总价
			f.setName(name);
			f.setPrice(price);
			f.setComment(comment);
			f.setOrderedNum(orderedNum);
			mySubmittedList.getOrderedList().add(f);
		}
		mySubmittedList.setCount(count);
		mySubmittedList.setTotalCost(totalCost);
	}
	/**
	 * 将表ORDERLIST中的菜品设置为已提交
	 */
	public void updateOrderList() {
		db = helper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("isSubmitted", 1);
		db.update("ORDERLIST", cv, null, null);
	}
	
	/**
	 * 提交订单处理函数：将表ORDERLIST中的数据插入到SUBMITLIST表中
	 */
	public void insertSubmitList() {
		db = helper.getWritableDatabase();
		Cursor cursor1 = db.rawQuery("select * from ORDERLIST", null);
		while(cursor1.moveToNext()) {
			String name = cursor1.getString(cursor1.getColumnIndex("name"));
			int orderedNum1 = cursor1.getInt(cursor1.getColumnIndex("orderedNum"));
			Cursor cursor2 = db.rawQuery("select * from SUBMITLIST where name = ?", new String[] {name});
			if(cursor2.moveToFirst()) {
				int orderedNum2 = cursor2.getInt(cursor2.getColumnIndex("orderedNum"));
				ContentValues cv = new ContentValues();
				cv.put("orderedNum", orderedNum2 + orderedNum1);
				db.update("SUBMITLIST", cv, "name = ?", new String[] {name});   //SUBMITLIST中有该菜品，则更新该菜品数量
			}  else {
				ContentValues cv = new ContentValues();
				cv.put("name", name);
				cv.put("orderedNum", orderedNum1);
				db.insert("SUBMITLIST", null, cv);           //SUBMITLIST中没有该菜品，则插入该菜品
			}
		}
	}
	/**
	 * 检查该菜品是否已点
	 */
	public boolean checkIsOrdered(Food f) {
		String name = f.getName();
		db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select isOrdered from FOOD where name = ?", new String[] {name});
		int isOrdered = 0;
		if(cursor.moveToFirst()) {
			isOrdered = cursor.getInt(cursor.getColumnIndex("isOrdered"));
		}
		if(isOrdered == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	/*
	 * 更新菜品库存数量:菜品数量减1
	 */
	public void updateLeftNum() {
		db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select name, leftNum from FOOD", null);
		while(cursor.moveToNext()) {
			int leftNum = cursor.getInt(cursor.getColumnIndex("leftNum"));
			String name = cursor.getString(cursor.getColumnIndex("name"));
			ContentValues cv = new ContentValues();
			cv.put("leftNum", --leftNum);
			db.update("FOOD", cv, "name = ?", new String[] {name});
		}
	}
}
	
