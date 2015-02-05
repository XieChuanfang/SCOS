package ustc.edu.sse.esd.model;

import java.io.Serializable;

/**
 * food类定义
 * 实现Serializable类，才可以使用Intent传递
 * Copyright: Copyright (c) 2015-2-3 19:47:25
 * Company: 中国科学技术大学 软件学院
 * @author moon：代码编写，star：代码整理
 * @version 2.0
 */
public class Food implements Serializable {
	public final static int foodCount = 20; 
	private String name; // 菜名
	private int price; // 价格
	private String comment; // 备注
	private int imageId; // 图片ID
	private boolean isOrdered; // 是否订购
	private int num; 

	public Food() {
		super();
	}

	/**
	 * 有参构造方法
	 * @param name 名
	 * @param price 价格
	 * @param comment 备注
	 * @param imageId 图片Id
	 * @param isOrdered 是否已定
	 */
	public Food(String name, int price, String comment, int imageId,
			boolean isOrdered) {
		super();
		this.name = name;
		this.price = price;
		this.comment = comment;
		this.imageId = imageId;
		this.isOrdered = isOrdered;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public boolean isOrdered() {
		return isOrdered;
	}

	public void setOrdered(boolean isOrdered) {
		this.isOrdered = isOrdered;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
}
