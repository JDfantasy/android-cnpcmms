package com.cnpc.zhibo.app.util.adapter;

import java.util.List;

import com.cnpc.zhibo.app.util.Node;
import com.cnpc.zhibo.app.util.TreeHelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;


public abstract class TreeListViewAdapter<T> extends BaseAdapter
{
	protected Context mContext;
	protected List<Node> mAllNodes;
	protected List<Node> mVisibleNodes;
	protected LayoutInflater mInflater;

	protected ListView mTree;

	/**
	 * ����Node�ĵ���ص�
	 * 
	 * @author zhy
	 * 
	 */
	public interface OnTreeNodeClickListener
	{
		void onClick(Node node, int position);
	}

	private OnTreeNodeClickListener mListener;

	public void setOnTreeNodeClickListener(OnTreeNodeClickListener mListener)
	{
		this.mListener = mListener;
	}

	public TreeListViewAdapter(ListView tree, Context context, List<T> datas,
			int defaultExpandLevel) throws IllegalArgumentException,
			IllegalAccessException
	{
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		changeDatas(datas,defaultExpandLevel);

		mTree = tree;

		mTree.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				expandOrCollapse(position);

				if (mListener != null)
				{
					mListener.onClick(mVisibleNodes.get(position), position);
				}

			}

		});

	}
	/**
	 * ���ڶ�̬��������
	 * @param datas
	 * @param defaultExpandLevel
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void changeDatas(List<T> datas, int defaultExpandLevel) throws IllegalArgumentException, IllegalAccessException {
		mAllNodes = TreeHelper.getSortedNodes(datas, defaultExpandLevel);
		mVisibleNodes = TreeHelper.filterVisibleNodes(mAllNodes);
		this.notifyDataSetChanged();
	}

	/**
	 * �����������չ��
	 * 
	 * @param position
	 */
	private void expandOrCollapse(int position)
	{
		Node n = mVisibleNodes.get(position);
		if (n != null)
		{
			if (n.isLeaf())
				return;
			n.setExpand(!n.isExpand());
			mVisibleNodes = TreeHelper.filterVisibleNodes(mAllNodes);
			notifyDataSetChanged();
		}
	}

	@Override
	public int getCount()
	{
		return mVisibleNodes.size();
	}

	@Override
	public Object getItem(int position)
	{
		return mVisibleNodes.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Node node = mVisibleNodes.get(position);
		convertView = getConvertView(node, position, convertView, parent);
		// �����ڱ߾�
		convertView.setPadding(node.getLevel() * 50, 5, 5, 5);
		return convertView;
	}

	public abstract View getConvertView(Node node, int position,
			View convertView, ViewGroup parent);

}
