package com.cnpc.zhibo.app.entity;

//վ����Ҫ���޵���Ŀ��ʵ����
import android.R.string;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * ���ʵ���������ʵ��������ʵ����ʱӦע�⼸������ 1����ȡ���ݺ�д�����ݵķ���ʱ�����ݵ�˳��Ҫ����һ�� 2�����������������������д
 * 3������İ���Ҫ������ȷ�����ܵ���
 * */
public class Centre_Repairs_item implements Parcelable {
	public String id;// ��Ŀ��id
	public String name;// ��Ŀ������
	public String order;
	public int ckid;
	public boolean ck = false;// �ж���Ŀ�Ƿ�ѡ��

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
	// ���л�
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(this.id);
		dest.writeString(this.name);
		dest.writeString(this.order);

	}

	// �����л�
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
