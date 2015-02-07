package ustc.edu.sse.esd.model;

import java.io.Serializable;

/**
 * food类定义
 * Copyright: Copyright (c) 2015-2-3 19:47:25
 * Company: 中国科学技术大学 软件学院
 * 
 * @author moon：代码编写，star：代码整理
 * @version 2.0
 */
public class Food implements Serializable {
	public final static int foodCount = 20;
	private String name;
	private int price;
	private String comment;
	private int imageId;
	private boolean isOrdered;
	private int num;

	public Food() {
		super();
	}

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
