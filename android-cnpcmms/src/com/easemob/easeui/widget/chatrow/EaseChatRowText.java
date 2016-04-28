package com.easemob.easeui.widget.chatrow;

import com.cnpc.zhibo.app.R;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.TextMessageBody;
import com.easemob.easeui.utils.EaseSmileUtils;
import com.easemob.exceptions.EaseMobException;

import android.content.Context;
import android.text.Spannable;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class EaseChatRowText extends EaseChatRow{

	private TextView contentView;

    public EaseChatRowText(Context context, EMMessage message, int position, BaseAdapter adapter) {
		super(context, message, position, adapter);
	}

	@Override
	protected void onInflatView() {
		inflater.inflate(message.direct == EMMessage.Direct.RECEIVE ?
				R.layout.ease_row_received_message : R.layout.ease_row_sent_message, this);
	}

	@Override
	protected void onFindViewById() {
		contentView = (TextView) findViewById(R.id.tv_chatcontent);
	}

    @Override
    public void onSetUpView() {
        TextMessageBody txtBody = (TextMessageBody) message.getBody();
        Spannable span = EaseSmileUtils.getSmiledText(context, txtBody.getMessage());
        // ��������
        contentView.setText(span, BufferType.SPANNABLE);

        handleTextMessage();
    }

    protected void handleTextMessage() {
        if (message.direct == EMMessage.Direct.SEND) {
            setMessageSendCallback();
            switch (message.status) {
            case CREATE: 
                progressBar.setVisibility(View.VISIBLE);
                statusView.setVisibility(View.GONE);
                // ������Ϣ
//                sendMsgInBackground(message);
                break;
            case SUCCESS: // ���ͳɹ�
                progressBar.setVisibility(View.GONE);
                statusView.setVisibility(View.GONE);
                break;
            case FAIL: // ����ʧ��
                progressBar.setVisibility(View.GONE);
                statusView.setVisibility(View.VISIBLE);
                break;
            case INPROGRESS: // ������
                progressBar.setVisibility(View.VISIBLE);
                statusView.setVisibility(View.GONE);
                break;
            default:
               break;
            }
        }else{
            if(!message.isAcked() && message.getChatType() == ChatType.Chat){
                try {
                    EMChatManager.getInstance().ackMessageRead(message.getFrom(), message.getMsgId());
                    message.isAcked = true;
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onUpdateView() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onBubbleClick() {
        // TODO Auto-generated method stub
        
    }



}