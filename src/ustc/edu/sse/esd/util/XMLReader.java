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
 * @version 3.0
 */
public class XMLReader {
	public static Context context = null;
	/*菜品ID*/
	private final static int COOL_FOOD_ID = 1;
	private final static int HOT_FOOD_ID = 2;
	private final static int SEAFOOD_ID = 3;
	private final static int DRINKS_ID = 4;
	/**
	 * 从xml读取数据，创建food对象，再添加到菜品集合中
	 */
	public static void getDataFromXML(List<Food> coolFood, List<Food> hotFood, List<Food> seaFood, List<Food> drinks) {
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
			/*根据类别将菜添加到相应菜品集合中*/
			switch (food_class[i]) {
			case COOL_FOOD_ID:  
				f.setFoodClass(COOL_FOOD_ID);
				coolFood.add(f);
				break;
			case HOT_FOOD_ID:
				f.setFoodClass(HOT_FOOD_ID);
				hotFood.add(f);
				break;
			case SEAFOOD_ID:
				f.setFoodClass(SEAFOOD_ID);
				seaFood.add(f);
				break;
			case DRINKS_ID:
				f.setFoodClass(DRINKS_ID);
				drinks.add(f);
				break;
			default:
				break;
			}
		}
	}
}