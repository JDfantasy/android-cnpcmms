package com.cnpc.zhibo.app.entity;

//站长端要保修的项目的实体类
import android.R.string;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * 这个实体类进行了实例化，在实例化时应注意几个事项 1、读取数据和写入数据的方法时，数据的顺序要保持一致 2、反序列这个方法不能忘记写
 * 3、导入的包名要导入正确，不能导错
 * */
public class Centre_Repairs_item implements Parcelable {
	public String id;// 项目的id
	public String name;// 项目的名称
	public String order;
	public int ckid;
	public boolean ck = false;// 判断项目是否选中

	public Centre_Repairs_item(Parcel p) {
		this.id = p.readString();
		this.name = p.readString();
		this.order = p.readString();

	}

	public Centre_Repairs_item(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Centre_Repairs_item(int ckid, String name) {
		super();
		this.ckid = ckid;
		this.name = name;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	// 序列化
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(this.id);
		dest.writeString(this.name);
		dest.writeString(this.order);

	}

	// 反序列化
	public static final Creator<Centre_Repairs_item> CREATOR = new Parcelable.Creator<Centre_Repairs_item>() {

		@Override
		public Centre_Repairs_item createFromParcel(Parcel arg0) {

			return new Centre_Repairs_item(arg0);
		}

		@Override
		public Centre_Repairs_item[] newArray(int arg0) {

			return new Centre_Repairs_item[arg0];
		}
	};

}
