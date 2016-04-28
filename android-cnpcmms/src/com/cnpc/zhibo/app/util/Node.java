package com.cnpc.zhibo.app.util;

import java.util.ArrayList;
import java.util.List;

/**
 * �����ڵ���
 * ������״��ʾ
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
	//���ڵ��pidΪ0 
	private int pId = 0;
	//��ʾ����
	private String name;
	//���Ĳ㼶
	private int level;
	//�Ƿ�Ϊչ��
	private boolean isExpand = false;
	//ͼ��
	private int icon;
	private Node parent;
	private List<Node> children = new ArrayList<Node>();
	//����
	private String type ;
	//�洢ʵ���ٵ��ʱ�����ж�
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
	 * �ж��Ƿ��Ǹ��ڵ�
	 * @return
	 */
	public boolean isRoot(){
		return parent == null;
	}
	/**
	 * �жϸ��ڵ��Ƿ���չ��
	 * @return
	 */
	public boolean isParentExpand(){
		if(parent==null)
			return false;
		return parent.isExpand();
	}
	/**
	 * �Ƿ���Ҷ�ڵ�
	 * @return
	 */
	public boolean isLeaf(){
		return children.size()==0;
	}
	/**
	 *  �õ���ǰ�ڵ�Ĳ㼶
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
	 *  �رո��ڵ������ӽڵ�ҲĬ�Ϲر�
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
