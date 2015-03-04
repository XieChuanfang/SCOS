package ustc.edu.sse.esd.util;

import java.util.List;
import ustc.edu.sse.esd.activity.R;
import ustc.edu.sse.esd.model.Food;
import android.content.Context;
import android.content.res.TypedArray;
/**
 * 从xml读取资源文件
 * Copyright: Copyright (c) 2015-2-16 21.30:25
 * Company: 中国科学技术大学 软件学院
 * 
 * @author moon：代码编写，star：代码整理
 * @version 5.0
 */
public class XMLReader {
	public static Context context = null;

	/**
	 * 从xml读取数据，创建food对象，再添加到菜品集合中
	 */
	public static void getDataFromXML(List<Food> fList) {
		// 从XML文件中装载数组资源
		String[] names = context.getResources().getStringArray(R.array.food_name);
		int[] price = context.getResources().getIntArray(R.array.food_price);
		String[] comment = context.getResources().getStringArray(R.array.food_comment);
		int[] left_num = context.getResources().getIntArray(R.array.food_left_num);
		int[] food_class = context.getResources().getIntArray(R.array.food_class);
		TypedArray typedArray = context.getResources().obtainTypedArray(R.array.food_img); // 获取混杂类型数组
		int length = typedArray.length();
		int[] images = new int[length];
		for (int i = 0; i < length; i++) {
			images[i] = typedArray.getResourceId(i, 0); // 将混杂类型数组转变为图片资源id数组
		}
		typedArray.recycle();
		
		for (int i = 0; i < food_class.length; i++) {
			Food f = new Food();
			f.setName(names[i]);
			f.setPrice(price[i]);
			f.setComment(comment[i]);
			f.setLeft_num(left_num[i]);
			f.setImageId(images[i]);      
			f.setOrderedNum(0);
			f.setOrdered(false);
			f.setFoodClass(food_class[i]);
			fList.add(f);
		}
	}
}