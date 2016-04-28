package com.cnpc.zhibo.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import com.cnpc.zhibo.app.util.Myutil;
import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.widget.EaseConversationList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * 维修端消息通知，此界面展示与维修员联系过的会话列表
 * 
 * @author xicunyou
 *
 */
public class Maintain_home_newsNotificationActivity extends MyActivity implements OnClickListener {

	private ImageView back;// 返回
	private InputMethodManager inputMethodManager;
	private EaseConversationList conversationListView;
	private EditText query;
	private ImageButton clearSearch;
	private FrameLayout errorItemContainer;
	protected List<EMConversation> conversationList = new ArrayList<EMConversation>();
	protected boolean isConflict;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maintain_home_news_notification);
		setViews();
		set_title_text("消息会话");
	}

	// 控件初始化
	private void setViews() {
		back = (ImageView) findViewById(R.id.imageView_myacitity_zuo);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(this);
		initView();
		setUpView();
	}

	private void initView() {
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		// 会话列表控件
		conversationListView = (EaseConversationList) findViewById(R.id.list);
		// 搜索框
		query = (EditText) findViewById(R.id.query);
		// 搜索框中清除button
		clearSearch = (ImageButton) findViewById(R.id.search_clear);
		errorItemContainer = (FrameLayout) findViewById(R.id.fl_error_item);
	}

	private void setUpView() {
		conversationList.addAll(loadConversationList());
		conversationListView.init(conversationList);
		conversationListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				EMConversation conversation = conversationListView.getItem(position);
				startActivity(new Intent(Maintain_home_newsNotificationActivity.this, ChatActivity.class)
						.putExtra(EaseConstant.EXTRA_USER_ID, conversation.getUserName()));
				Myutil.set_activity_open(Maintain_home_newsNotificationActivity.this);
			}

		});
		conversationListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
				AlertDialog.Builder builder = new Builder(Maintain_home_newsNotificationActivity.this);
				builder.setMessage("确认选择删除该会话吗?");
				builder.setTitle("提示");
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 删除聊天记录
						dialog.dismiss();
						EMChatManager.getInstance().clearConversation(conversationList.get(position).getUserName());
						refresh();
					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).create().show();

				return true;
			}
		});

		EMChatManager.getInstance().addConnectionListener(connectionListener);

		query.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				conversationListView.filter(s);
				if (s.length() > 0) {
					clearSearch.setVisibility(View.VISIBLE);
				} else {
					clearSearch.setVisibility(View.INVISIBLE);
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void afterTextChanged(Editable s) {
			}
		});
		clearSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				query.getText().clear();
				hideSoftKeyboard();
			}
		});

		conversationListView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				hideSoftKeyboard();
				return false;
			}
		});
	}

	protected EMConnectionListener connectionListener = new EMConnectionListener() {
		@Override
		public void onDisconnected(int error) {
			if (error == EMError.USER_REMOVED || error == EMError.CONNECTION_CONFLICT) {
				isConflict = true;
			} else {
				handler.sendEmptyMessage(0);
			}
		}

		@Override
		public void onConnected() {
			handler.sendEmptyMessage(1);
		}
	};

	/**
	 * 获取会话列表
	 * 
	 * @param context
	 * @return +
	 */
	private List<EMConversation> loadConversationList() {
		// 获取所有会话，包括陌生人
		Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
		// 过滤掉messages size为0的conversation
		/**
		 * 如果在排序过程中有新消息收到，lastMsgTime会发生变化 影响排序过程，Collection.sort会产生异常
		 * 保证Conversation在Sort过程中最后一条消息的时间不变 避免并发问题
		 */
		List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
		EMConversation admin = null;
		synchronized (conversations) {
			for (EMConversation conversation : conversations.values()) {
				if (conversation.getAllMessages().size() != 0) {
					// 过滤掉admin用户发来的消息
					if (!conversation.getUserName().equals("admin")) {
						sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(),
								conversation));
					} else {
						admin = conversation;
					}
				}
			}
		}
		try {
			// Internal is TimSort algorithm, has bug
			sortConversationByLastChatTime(sortList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<EMConversation> list = new ArrayList<EMConversation>();
		for (Pair<Long, EMConversation> sortItem : sortList) {
			list.add(sortItem.second);
		}
		if (admin != null)
			list.add(0, admin);
		return list;
	}

	/**
	 * 根据最后一条消息的时间排序
	 * 
	 * @param usernames
	 */
	private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
		Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
			@Override
			public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

				if (con1.first == con2.first) {
					return 0;
				} else if (con2.first > con1.first) {
					return 1;
				} else {
					return -1;
				}
			}

		});
	}

	protected Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				onConnectionDisconnected();
				break;
			case 1:
				onConnectionConnected();
				break;

			default:
				break;
			}
		}
	};

	/**
	 * 连接到服务器
	 */
	protected void onConnectionConnected() {
		errorItemContainer.setVisibility(View.GONE);
	}

	/**
	 * 连接断开
	 */
	protected void onConnectionDisconnected() {
		errorItemContainer.setVisibility(View.VISIBLE);
	}

	// 监听
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 返回
		case R.id.imageView_myacitity_zuo:
			finish();
			Myutil.set_activity_close(this);// 设置切换界面的效果
			break;

		default:
			break;
		}

	}

	private void hideSoftKeyboard() {
		if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		refresh();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EMChatManager.getInstance().removeConnectionListener(connectionListener);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (isConflict) {
			outState.putBoolean("isConflict", true);
		}
	}

	/**
	 * 刷新页面
	 */
	public void refresh() {
		conversationList.clear();
		conversationList.addAll(loadConversationList());
		conversationListView.refresh();
	}

}
