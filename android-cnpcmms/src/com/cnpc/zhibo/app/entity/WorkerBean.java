package com.cnpc.zhibo.app.entity;

import com.cnpc.zhibo.app.util.annotation.TreeNodeId;
import com.cnpc.zhibo.app.util.annotation.TreeNodeLabel;
import com.cnpc.zhibo.app.util.annotation.TreeNodePid;
import com.cnpc.zhibo.app.util.annotation.TreeNodeType;

/**
 * ά�޹�ʵ��
 * ������ʾά�޹��б��е��ն�ʵ��
 * @author xicunyou
 *
 */
public class WorkerBean 
{
	@TreeNodeId
	private int  	id;
	@TreeNodePid
	private int 	parent;
	@TreeNodeLabel
	private String name;
	@TreeNodeType
	private String type;
	private WorkerBean children;
	private boolean selected = false;
	
	public WorkerBean() {
	}
	
	public WorkerBean(int id, int parent, String name) {
		super();
		this.id = id;
		this.parent = parent;
		this.name = name;
	}


	
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getParent() {
		return parent;
	}


	public void setParent(int parent) {
		this.parent = parent;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public WorkerBean getChildren() {
		return children;
	}


	public void setChildren(WorkerBean children) {
		this.children = children;
	}
	
}
