package com.cnpc.zhibo.app;

import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.easeui.ui.EaseBaseActivity;
//import com.easemob.easeui.ui.EaseChatFragment;
import com.easemob.easeui.ui.EaseChatFragment;
import com.easemob.easeui.ui.EaseChatFragment.EaseChatFragmentListener;
import com.easemob.easeui.widget.chatrow.EaseCustomChatRowProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;

/**
 * ��Ӧ�̶��������
 * ������ҳ����Ҫ���ݽ����Ĳ���userId �� ȡֵ�ػ���UserName conversation.getUserName()
 * startActivity(new Intent(MainActivity.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, conversation.getUserName()));
 * 
 */
public class ChatActivity extends  EaseBaseActivity{
    public static ChatActivity activityInstance;
    private EaseChatFragment chatFragment;
    String toChatUsername;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_chat);
        activityInstance = this;
        //�����˻�Ⱥid
        toChatUsername = getIntent().getExtras().getString("userId");
        chatFragment = new EaseChatFragment();
        //�������
        chatFragment.setArguments(getIntent().getExtras());
        chatFragment.setChatFragmentListener(new EaseChatFragmentListener() {
			@Override
			public void onSetMessageAttributes(EMMessage message) {
			}
			
			@Override
			public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
				return null;
			}
			
			@Override
			public void onMessageBubbleLongClick(EMMessage message) {
				//�����¼�
				 switch ( message.getType()) {
				case IMAGE://�����ͼƬ �����¼�����
					break;
				}
			}
			
			@Override
			public boolean onMessageBubbleClick(EMMessage message) {
				return false;
			}
			
			@Override
			public boolean onExtendMenuItemClick(int itemId, View view) {
				return false;
			}
			
			@Override
			public void onEnterToChatDetails() {
			}
			
			@Override
			public void onAvatarClick(String username) {
			}
		});
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
        // ���notification bar��������ҳ�棬��ֻ֤��һ������ҳ��
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }
    @Override
    public void onBackPressed() {
        chatFragment.onBackPressed();
    }
    
    public String getToChatUsername(){
        return toChatUsername;
    }
}
