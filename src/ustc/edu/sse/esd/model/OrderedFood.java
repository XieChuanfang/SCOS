package ustc.edu.sse.esd.model;

import java.util.ArrayList;

/**
 * 已点菜品集合对象
 * Copyright: Copyright (c) 2015-2-4 21:47:25
 * Company: 中国科学技术大学 软件学院
 * 
 * @author moon：代码编写，star：代码整理
 * @version 5.0
 */
public class OrderedFood {
	ArrayList<Food> orderedList = new ArrayList<Food>(); // 已点菜集合
	int totalCost; // 已点菜总价
	int count; // 已点菜总数

	public int getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(int totalCost) {
		this.totalCost = totalCost;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public ArrayList<Food> getOrderedList() {
		return orderedList;
	}

	public void setOrderedList(ArrayList<Food> orderedList) {
		this.orderedList = orderedList;
	}
}
