package com.cnpc.zhibo.app.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础节点类
 * 用于树状显示
 * @author xicunyou
 */
public class Node {
	
	public Node() {
	}
	
	public Node(int id, int pId, String name) {
		super();
		this.id = id;
		this.pId = pId;
		this.name = name;
	}
	private int id;
	//根节点的pid为0 
	private int pId = 0;
	//显示名称
	private String name;
	//树的层级
	private int level;
	//是否为展开
	private boolean isExpand = false;
	//图标
	private int icon;
	private Node parent;
	private List<Node> children = new ArrayList<Node>();
	//类型
	private String type ;
	//存储实体再点击时容易判断
	private Object bean;
	public Object getBean() {
		return bean;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setBean(Object bean) {
		this.bean = bean;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getpId() {
		return pId;
	}
	public void setpId(int pId) {
		this.pId = pId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public int getIcon() {
		return icon;
	}
	public void setIcon(int icon) {
		this.icon = icon;
	}
	public Node getParent() {
		return parent;
	}
	public void setParent(Node parent) {
		this.parent = parent;
	}
	public List<Node> getChildren() {
		return children;
	}
	public void setChildren(List<Node> children) {
		this.children = children;
	}
	
	/**
	 * 判断是否是根节点
	 * @return
	 */
	public boolean isRoot(){
		return parent == null;
	}
	/**
	 * 判断父节点是否是展开
	 * @return
	 */
	public boolean isParentExpand(){
		if(parent==null)
			return false;
		return parent.isExpand();
	}
	/**
	 * 是否是叶节点
	 * @return
	 */
	public boolean isLeaf(){
		return children.size()==0;
	}
	/**
	 *  得到当前节点的层级
	 * @return
	 */
	public int getLevel() {
		return parent == null?0:parent.getLevel()+1;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
	public boolean isExpand() {
		return isExpand;
	}
	/**
	 *  关闭根节点所有子节点也默认关闭
	 * @param isExpand
	 */
	public void setExpand(boolean isExpand) {
		this.isExpand = isExpand;
		if(!isExpand){
			for(Node node:children){
				node.setExpand(false);
			}
		}
	}
	
}
