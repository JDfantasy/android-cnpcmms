package com.cnpc.zhibo.app.entity;

/**
 *供应商选择部门的列表的实体类
 */
public class Supplier_notice_contacts {
	public String department_name;
	public boolean ched=false;

	public Supplier_notice_contacts() {
		super();
	}

	public Supplier_notice_contacts(String department_name) {
		super();
		this.department_name = department_name;
	}

}
