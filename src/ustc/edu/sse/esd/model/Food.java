package ustc.edu.sse.esd.model;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * food类定义,实现serializable和Parcelable接口
 * 注：Parcel是通过IBinder发送消息的容器，主要用于高性能的IPC传输，不合适将里面的数据放到持久存储器中存储
 * Copyright: Copyright (c) 2015-2-3 19:47:25
 * Company: 中国科学技术大学 软件学院
 * 
 * @author moon：代码编写，star：代码整理
 * @version 2.0
 */
public class Food implements Serializable, Parcelable {
	public final static int COOL_FOOD_ID = 1;               
	public final static int HOT_FOOD_ID = 2;
	public final static int SEAFOOD_ID = 3;
	public final static int DRINKS_ID = 4;
	private String name;                            //菜名
	private int price;                              //价格
	private String comment;                         //备注
	private int imageId;                            //图片
	private boolean isOrdered;                      //是否已点
	private int orderedNum;                         //已点菜品数量
	private int leftNum;                            //库存量
	private int foodClass;                          //类别

	public Food() {
		super();
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

	public int getFoodClass() {
		return foodClass;
	}
	public void setFoodClass(int foodClass) {
		this.foodClass = foodClass;
	}
	public boolean isOrdered() {
		return isOrdered;
	}
	public void setOrdered(boolean isOrdered) {
		this.isOrdered = isOrdered;
	}
	public int getLeftNum() {
		return leftNum;
	}
	public void setLeft_num(int leftNum) {
		this.leftNum = leftNum;
	}
	public int getOrderedNum() {
		return orderedNum;
	}
	public void setOrderedNum(int orderedNum) {
		this.orderedNum = orderedNum;
	}
	
	public void writeToParcel(Parcel out, int flags) {
    	out.writeString(name);
    	out.writeInt(price);
    	out.writeString(comment);
    	out.writeInt(imageId);
    	out.writeInt(isOrdered ? 1: 0);
    	out.writeInt(leftNum);
    	out.writeInt(orderedNum);
    	out.writeInt(foodClass);
    }
	
    public static final Parcelable.Creator<Food> CREATOR = new Parcelable.Creator<Food>() {
    	
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        public Food[] newArray(int size) {
            return new Food[size];
        }
    };
    
    private Food(Parcel in) {
        this.name = in.readString();
        this.price = in.readInt();
        this.comment = in.readString();
        this.imageId = in.readInt();
        this.isOrdered = (in.readInt() == 1 ? true: false);
        this.leftNum = in.readInt();
        this.orderedNum = in.readInt();
        this.foodClass = in.readInt();
    }

	@Override
	public int describeContents() {
		return 0;
	}

}
