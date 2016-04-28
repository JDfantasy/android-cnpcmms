package com.easemob.easeui.ui;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.ClipboardManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.Toast;

import com.cnpc.zhibo.app.R;
import com.easemob.EMChatRoomChangeListener;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.EMValueCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatRoom;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.controller.EaseUI;
import com.easemob.easeui.domain.EaseEmojicon;
import com.easemob.easeui.utils.EaseCommonUtils;
import com.easemob.easeui.utils.EaseImageUtils;
import com.easemob.easeui.utils.EaseUserUtils;
import com.easemob.easeui.widget.EaseAlertDialog;
import com.easemob.easeui.widget.EaseAlertDialog.AlertDialogUser;
import com.easemob.easeui.widget.EaseChatExtendMenu;
import com.easemob.easeui.widget.EaseChatInputMenu;
import com.easemob.easeui.widget.EaseChatInputMenu.ChatInputMenuListener;
import com.easemob.easeui.widget.EaseChatMessageList;
import com.easemob.easeui.widget.EaseVoiceRecorderView;
import com.easemob.easeui.widget.EaseVoiceRecorderView.EaseVoiceRecorderCallback;
import com.easemob.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.easemob.util.EMLog;
import com.easemob.util.PathUtil;

/**
 * ����ֱ��new����ʹ�õ�����Ի�ҳ��fragment��
 * ʹ��ʱ�����setArguments��������chatType(�Ự����)��userId(�û���Ⱥid)
 * appҲ�ɼ̳д�fragment��д
 * <br/>
 * <br/>
 * ��������ʾ���ɲ鿴demo���ChatActivity
 *
 */
public class EaseChatFragment extends EaseBaseFragment implements EMEventListener {
    protected static final String TAG = "EaseChatFragment";
    protected static final int REQUEST_CODE_MAP = 1;
    protected static final int REQUEST_CODE_CAMERA = 2;
    protected static final int REQUEST_CODE_LOCAL = 3;

    /**
     * ����fragment�Ĳ���
     */
    protected Bundle fragmentArgs;
    protected int chatType;
    protected String toChatUsername;
    protected EaseChatMessageList messageList;
    protected EaseChatInputMenu inputMenu;

    protected EMConversation conversation;
    
    protected InputMethodManager inputManager;
    protected ClipboardManager clipboard;

    protected Handler handler = new Handler();
    protected File cameraFile;
    protected EaseVoiceRecorderView voiceRecorderView;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected ListView listView;

    protected boolean isloading;
    protected boolean haveMoreData = true;
    protected int pagesize = 20;
    protected EMMessage contextMenuMessage;
    
    static final int ITEM_TAKE_PICTURE = 1;
    static final int ITEM_PICTURE = 2;
    static final int ITEM_LOCATION = 3;
    
//    protected int[] itemStrings = { R.string.attach_take_pic, R.string.attach_picture, R.string.attach_location };
//    protected int[] itemdrawables = { R.drawable.ease_chat_takepic_selector, R.drawable.ease_chat_image_selector,
//            R.drawable.ease_chat_location_selector };
//    protected int[] itemIds = { ITEM_TAKE_PICTURE, ITEM_PICTURE, ITEM_LOCATION };
    //���� λ��
    protected int[] itemStrings = { R.string.attach_take_pic, R.string.attach_picture };
    protected int[] itemdrawables = { R.drawable.ease_chat_takepic_selector, R.drawable.ease_chat_image_selector};
    protected int[] itemIds = { ITEM_TAKE_PICTURE, ITEM_PICTURE};
    
    
    private EMChatRoomChangeListener chatRoomChangeListener;
    private boolean isMessageListInited;
    protected MyItemClickListener extendMenuItemClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ease_fragment_chat, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        fragmentArgs = getArguments();
        // �жϵ��Ļ���Ⱥ��
        chatType = fragmentArgs.getInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        // �Ự�˻�Ⱥ��id
        toChatUsername = fragmentArgs.getString(EaseConstant.EXTRA_USER_ID);

        super.onActivityCreated(savedInstanceState);
    }

    /**
     * init view
     */
    protected void initView() {
        // ��ס˵��¼���ؼ�
        voiceRecorderView = (EaseVoiceRecorderView) getView().findViewById(R.id.voice_recorder);

        // ��Ϣ�б�layout
        messageList = (EaseChatMessageList) getView().findViewById(R.id.message_list);
        if(chatType != EaseConstant.CHATTYPE_SINGLE)
            messageList.setShowUserNick(true);
        listView = messageList.getListView();

        extendMenuItemClickListener = new MyItemClickListener();
        inputMenu = (EaseChatInputMenu) getView().findViewById(R.id.input_menu);
        registerExtendMenuItem();
        // init input menu
        inputMenu.init(null);
        inputMenu.setChatInputMenuListener(new ChatInputMenuListener() {

            @Override
            public void onSendMessage(String content) {
                // �����ı���Ϣ
                sendTextMessage(content);
            }

            @Override
            public boolean onPressToSpeakBtnTouch(View v, MotionEvent event) {
                return voiceRecorderView.onPressToSpeakBtnTouch(v, event, new EaseVoiceRecorderCallback() {
                    
                    @Override
                    public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
                        // ����������Ϣ
                        sendVoiceMessage(voiceFilePath, voiceTimeLength);
                    }
                });
            }

            @Override
            public void onBigExpressionClicked(EaseEmojicon emojicon) {
                //���ʹ����(��̬����)
                sendBigExpressionMessage(emojicon.getName(), emojicon.getIdentityCode());
            }
        });

        swipeRefreshLayout = messageList.getSwipeRefreshLayout();
        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);

        inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * �������ԣ�������
     */
    protected void setUpView() {
        set_title_text(toChatUsername);
        if (chatType == EaseConstant.CHATTYPE_SINGLE) { // ����
            // ���ñ���
            if(EaseUserUtils.getUserInfo(toChatUsername) != null){
                set_title_text(EaseUserUtils.getUserInfo(toChatUsername).getNick());
            }
        } 
        if (chatType != EaseConstant.CHATTYPE_CHATROOM) {
            onConversationInit();
            onMessageListInit();
        }
        setRefreshLayoutListener();
        // show forward message if the message is not null
        String forward_msg_id = getArguments().getString("forward_msg_id");
        if (forward_msg_id != null) {
            // ����Ҫת������Ϣ
            forwardMessage(forward_msg_id);
        }
    }
    
    /**
     * ע��ײ��˵���չ��item; ���Ǵ˷���ʱ�������������item��item��id�����3
     */
    protected void registerExtendMenuItem(){
        for(int i = 0; i < itemStrings.length; i++){
            inputMenu.registerExtendMenuItem(itemStrings[i], itemdrawables[i], itemIds[i], extendMenuItemClickListener);
        }
    }
    
    
    protected void onConversationInit(){
        // ��ȡ��ǰconversation����
        conversation = EMChatManager.getInstance().getConversation(toChatUsername);
        // �Ѵ˻Ự��δ������Ϊ0
        conversation.markAllMessagesAsRead();
        // ��ʼ��dbʱ��ÿ��conversation������Ŀ��getChatOptions().getNumberOfMessagesLoaded
        // �����Ŀ������û���������Ự����ʱ��ʾ�ĸ�����һ�����Ͷ����һЩ
        final List<EMMessage> msgs = conversation.getAllMessages();
        int msgCount = msgs != null ? msgs.size() : 0;
        if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
            String msgId = null;
            if (msgs != null && msgs.size() > 0) {
                msgId = msgs.get(0).getMsgId();
            }
            if (chatType == EaseConstant.CHATTYPE_SINGLE) {
                conversation.loadMoreMsgFromDB(msgId, pagesize - msgCount);
            } else {
                conversation.loadMoreGroupMsgFromDB(msgId, pagesize - msgCount);
            }
        }
        
    }
    
    protected void onMessageListInit(){
        messageList.init(toChatUsername, chatType, chatFragmentListener != null ? 
                chatFragmentListener.onSetCustomChatRowProvider() : null);
        //����list item��Ŀؼ��ĵ���¼�
        setListItemClickListener();
        
        messageList.getListView().setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                inputMenu.hideExtendMenuContainer();
                return false;
            }
        });
        
        isMessageListInited = true;
    }
    
    protected void setListItemClickListener() {
        messageList.setItemClickListener(new EaseChatMessageList.MessageListItemClickListener() {
            
            @Override
            public void onUserAvatarClick(String username) {
                if(chatFragmentListener != null){
                    chatFragmentListener.onAvatarClick(username);
                }
            }
            
            @Override
            public void onResendClick(final EMMessage message) {
                new EaseAlertDialog(getActivity(), R.string.resend, R.string.confirm_resend, null, new AlertDialogUser() {
                    @Override
                    public void onResult(boolean confirmed, Bundle bundle) {
                        if (!confirmed) {
                            return;
                        }
                        resendMessage(message);
                    }
                }, true).show();
            }
            
            @Override
            public void onBubbleLongClick(EMMessage message) {
                contextMenuMessage = message;
                if(chatFragmentListener != null){
                    chatFragmentListener.onMessageBubbleLongClick(message);
                }
            }
            
            @Override
            public boolean onBubbleClick(EMMessage message) {
                if(chatFragmentListener != null){
                    return chatFragmentListener.onMessageBubbleClick(message);
                }
                return false; 
            }
        });
    }

    protected void setRefreshLayoutListener() {
        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (listView.getFirstVisiblePosition() == 0 && !isloading && haveMoreData) {
                            List<EMMessage> messages;
                            try {
                                if (chatType == EaseConstant.CHATTYPE_SINGLE) {
                                    messages = conversation.loadMoreMsgFromDB(messageList.getItem(0).getMsgId(),
                                            pagesize);
                                } else {
                                    messages = conversation.loadMoreGroupMsgFromDB(messageList.getItem(0).getMsgId(),
                                            pagesize);
                                }
                            } catch (Exception e1) {
                                swipeRefreshLayout.setRefreshing(false);
                                return;
                            }
                            if (messages.size() > 0) {
                                messageList.refreshSeekTo(messages.size() - 1);
                                if (messages.size() != pagesize) {
                                    haveMoreData = false;
                                }
                            } else {
                                haveMoreData = false;
                            }

                            isloading = false;

                        } else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.no_more_messages),
                                    Toast.LENGTH_SHORT).show();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 600);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) { 
            if (requestCode == REQUEST_CODE_CAMERA) { // ������Ƭ
                if (cameraFile != null && cameraFile.exists())
                    sendImageMessage(cameraFile.getAbsolutePath());
            } else if (requestCode == REQUEST_CODE_LOCAL) { // ���ͱ���ͼƬ
                if (data != null) {
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        sendPicByUri(selectedImage);
                    }
                }
            } else if (requestCode == REQUEST_CODE_MAP) { // ��ͼ
                double latitude = data.getDoubleExtra("latitude", 0);
                double longitude = data.getDoubleExtra("longitude", 0);
                String locationAddress = data.getStringExtra("address");
                if (locationAddress != null && !locationAddress.equals("")) {
                    sendLocationMessage(latitude, longitude, locationAddress);
                } else {
                    Toast.makeText(getActivity(), R.string.unable_to_get_loaction, 0).show();
                }
                
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isMessageListInited)
            messageList.refresh();
        EaseUI.getInstance().pushActivity(getActivity());
        // register the event listener when enter the foreground
        EMChatManager.getInstance().registerEventListener(
                this,
                new EMNotifierEvent.Event[] { EMNotifierEvent.Event.EventNewMessage,
                        EMNotifierEvent.Event.EventOfflineMessage, EMNotifierEvent.Event.EventDeliveryAck,
                        EMNotifierEvent.Event.EventReadAck });
    }

    @Override
    public void onStop() {
        super.onStop();
        // unregister this event listener when this activity enters the
        // background
        EMChatManager.getInstance().unregisterEventListener(this);

        // �Ѵ�activity ��foreground activity �б����Ƴ�
        EaseUI.getInstance().popActivity(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(chatType == EaseConstant.CHATTYPE_CHATROOM){
            EMChatManager.getInstance().leaveChatRoom(toChatUsername);
        }
        
        if(chatRoomChangeListener != null){
            EMChatManager.getInstance().removeChatRoomChangeListener(chatRoomChangeListener);
        }
    }

    /**
     * �¼�����,registerEventListener��Ļص��¼�
     * 
     * see {@link EMNotifierEvent}
     */
    @Override
    public void onEvent(EMNotifierEvent event) {
        switch (event.getEvent()) {
        case EventNewMessage:
            // ��ȡ��message
            EMMessage message = (EMMessage) event.getData();

            String username = null;
            // Ⱥ����Ϣ
            if (message.getChatType() == ChatType.GroupChat || message.getChatType() == ChatType.ChatRoom) {
                username = message.getTo();
            } else {
                // ������Ϣ
                username = message.getFrom();
            }

            // ����ǵ�ǰ�Ự����Ϣ��ˢ������ҳ��
            if (username.equals(toChatUsername)) {
                messageList.refreshSelectLast();
                // ����������ʾ������Ϣ
                EaseUI.getInstance().getNotifier().viberateAndPlayTone(message);
            } else {
                // �����Ϣ���Ǻ͵�ǰ����ID����Ϣ
                EaseUI.getInstance().getNotifier().onNewMsg(message);
            }

            break;
        case EventDeliveryAck:
        case EventReadAck:
            // ��ȡ��message
            messageList.refresh();
            break;
        case EventOfflineMessage:
            // a list of offline messages
            // List<EMMessage> offlineMessages = (List<EMMessage>)
            // event.getData();
            messageList.refresh();
            break;
        default:
            break;
        }

    }

    public void onBackPressed() {
        if (inputMenu.onBackPressed()) {
            getActivity().finish();
            if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
                EMChatManager.getInstance().leaveChatRoom(toChatUsername);
            }
        }
    }

    protected void onChatRoomViewCreation() {
        final ProgressDialog pd = ProgressDialog.show(getActivity(), "", "Joining......");
        EMChatManager.getInstance().joinChatRoom(toChatUsername, new EMValueCallBack<EMChatRoom>() {

            @Override
            public void onSuccess(final EMChatRoom value) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(getActivity().isFinishing() || !toChatUsername.equals(value.getUsername()))
                            return;
                        pd.dismiss();
                        EMChatRoom room = EMChatManager.getInstance().getChatRoom(toChatUsername);
                        if (room != null) {
                            set_title_text(room.getName());
                        } else {
                        	set_title_text(toChatUsername);
                        }
                        EMLog.d(TAG, "join room success : " + room.getName());
                        addChatRoomChangeListenr();
                        onConversationInit();
                        onMessageListInit();
                    }
                });
            }

            @Override
            public void onError(final int error, String errorMsg) {
                // TODO Auto-generated method stub
                EMLog.d(TAG, "join room failure : " + error);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                    }
                });
                getActivity().finish();
            }
        });
    }
    

    protected void addChatRoomChangeListenr() {
        chatRoomChangeListener = new EMChatRoomChangeListener() {

            @Override
            public void onChatRoomDestroyed(String roomId, String roomName) {
                if (roomId.equals(toChatUsername)) {
                    showChatroomToast(" room : " + roomId + " with room name : " + roomName + " was destroyed");
                    getActivity().finish();
                }
            }

            @Override
            public void onMemberJoined(String roomId, String participant) {
                showChatroomToast("member : " + participant + " join the room : " + roomId);
            }

            @Override
            public void onMemberExited(String roomId, String roomName, String participant) {
                showChatroomToast("member : " + participant + " leave the room : " + roomId + " room name : " + roomName);
            }

            @Override
            public void onMemberKicked(String roomId, String roomName, String participant) {
                if (roomId.equals(toChatUsername)) {
                    String curUser = EMChatManager.getInstance().getCurrentUser();
                    if (curUser.equals(participant)) {
                        EMChatManager.getInstance().leaveChatRoom(toChatUsername);
                        getActivity().finish();
                    }else{
                        showChatroomToast("member : " + participant + " was kicked from the room : " + roomId + " room name : " + roomName);
                    }
                }
            }

        };
        
        EMChatManager.getInstance().addChatRoomChangeListener(chatRoomChangeListener);
    }
    
    protected void showChatroomToast(final String toastContent){
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getActivity(), toastContent, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * ��չ�˵���item����¼�
     *
     */
    class MyItemClickListener implements EaseChatExtendMenu.EaseChatExtendMenuItemClickListener{

        @Override
        public void onClick(int itemId, View view) {
            if(chatFragmentListener != null){
                if(chatFragmentListener.onExtendMenuItemClick(itemId, view)){
                    return;
                }
            }
            switch (itemId) {
            case ITEM_TAKE_PICTURE: // ����
                selectPicFromCamera();
                break;
            case ITEM_PICTURE:
                selectPicFromLocal(); // ͼ��ѡ��ͼƬ
                break;
            case ITEM_LOCATION: // λ��
//                startActivityForResult(new Intent(getActivity(), EaseBaiduMapActivity.class), REQUEST_CODE_MAP);
                break;

            default:
                break;
            }
        }

    }
    

    //������Ϣ����
    //==========================================================================
    protected void sendTextMessage(String content) {
        EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
        sendMessage(message);
    }
    
    protected void sendBigExpressionMessage(String name, String identityCode){
        EMMessage message = EaseCommonUtils.createExpressionMessage(toChatUsername, name, identityCode);
        sendMessage(message);
    }

    protected void sendVoiceMessage(String filePath, int length) {
        EMMessage message = EMMessage.createVoiceSendMessage(filePath, length, toChatUsername);
        sendMessage(message);
    }

    protected void sendImageMessage(String imagePath) {
        EMMessage message = EMMessage.createImageSendMessage(imagePath, false, toChatUsername);
        sendMessage(message);
    }

    protected void sendLocationMessage(double latitude, double longitude, String locationAddress) {
        EMMessage message = EMMessage.createLocationSendMessage(latitude, longitude, locationAddress, toChatUsername);
        sendMessage(message);
    }

    protected void sendVideoMessage(String videoPath, String thumbPath, int videoLength) {
        EMMessage message = EMMessage.createVideoSendMessage(videoPath, thumbPath, videoLength, toChatUsername);
        sendMessage(message);
    }

    protected void sendFileMessage(String filePath) {
        EMMessage message = EMMessage.createFileSendMessage(filePath, toChatUsername);
        sendMessage(message);
    }
    
    protected void sendMessage(EMMessage message){
        if(chatFragmentListener != null){
            //������չ����
            chatFragmentListener.onSetMessageAttributes(message);
        }
        // �����Ⱥ�ģ�����chattype,Ĭ���ǵ���
        if (chatType == EaseConstant.CHATTYPE_GROUP){
            message.setChatType(ChatType.GroupChat);
        }else if(chatType == EaseConstant.CHATTYPE_CHATROOM){
            message.setChatType(ChatType.ChatRoom);
        }
        //������Ϣ
        EMChatManager.getInstance().sendMessage(message, null);
        //ˢ��ui
        messageList.refreshSelectLast();
    }
    
    
    public void resendMessage(EMMessage message){
        message.status = EMMessage.Status.CREATE;
        EMChatManager.getInstance().sendMessage(message, null);
        messageList.refresh();
    }
    
    //===================================================================================
    

    /**
     * ����ͼ��ͼƬuri����ͼƬ
     * 
     * @param selectedImage
     */
    protected void sendPicByUri(Uri selectedImage) {
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;

            if (picturePath == null || picturePath.equals("null")) {
                Toast toast = Toast.makeText(getActivity(), R.string.cant_find_pictures, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            sendImageMessage(picturePath);
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
                Toast toast = Toast.makeText(getActivity(), R.string.cant_find_pictures, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;

            }
            sendImageMessage(file.getAbsolutePath());
        }

    }
    
    /**
     * ����uri�����ļ�
     * @param uri
     */
    protected void sendFileByUri(Uri uri){
        String filePath = null;
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = null;

            try {
                cursor = getActivity().getContentResolver().query(uri, filePathColumn, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            filePath = uri.getPath();
        }
        File file = new File(filePath);
        if (file == null || !file.exists()) {
            Toast.makeText(getActivity(), R.string.File_does_not_exist, 0).show();
            return;
        }
        //����10M���÷���
        if (file.length() > 10 * 1024 * 1024) {
            Toast.makeText(getActivity(), R.string.The_file_is_not_greater_than_10_m, 0).show();
            return;
        }
        sendFileMessage(filePath);
    }

    /**
     * �����ȡͼƬ
     */
    protected void selectPicFromCamera() {
        if (!EaseCommonUtils.isExitsSdcard()) {
            Toast.makeText(getActivity(), R.string.sd_card_does_not_exist, 0).show();
            return;
        }

        cameraFile = new File(PathUtil.getInstance().getImagePath(), EMChatManager.getInstance().getCurrentUser()
                + System.currentTimeMillis() + ".jpg");
        cameraFile.getParentFile().mkdirs();
        startActivityForResult(
                new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
                REQUEST_CODE_CAMERA);
    }

    /**
     * ��ͼ���ȡͼƬ
     */
    protected void selectPicFromLocal() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_LOCAL);
    }



    /**
     * �����������¼
     * 
     */
    protected void emptyHistory() {
        String msg = getResources().getString(R.string.Whether_to_empty_all_chats);
        new EaseAlertDialog(getActivity(),null, msg, null,new AlertDialogUser() {
            
            @Override
            public void onResult(boolean confirmed, Bundle bundle) {
                if(confirmed){
                    // ��ջỰ
                    EMChatManager.getInstance().clearConversation(toChatUsername);
                    messageList.refresh();
                }
            }
        }, true).show();;
    }

    /**
     * �������Ⱥ������
     * 
     */
    protected void toGroupDetails() {
        if (chatType == EaseConstant.CHATTYPE_GROUP) {
            EMGroup group = EMGroupManager.getInstance().getGroup(toChatUsername);
            if (group == null) {
                Toast.makeText(getActivity(), R.string.gorup_not_found, 0).show();
                return;
            }
            if(chatFragmentListener != null){
                chatFragmentListener.onEnterToChatDetails();
            }
        }else if(chatType == EaseConstant.CHATTYPE_CHATROOM){
        	if(chatFragmentListener != null){
        		chatFragmentListener.onEnterToChatDetails();
        	}
        }
    }

    /**
     * ����������
     */
    protected void hideKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    
    /**
     * ת����Ϣ
     * 
     * @param forward_msg_id
     */
    protected void forwardMessage(String forward_msg_id) {
        final EMMessage forward_msg = EMChatManager.getInstance().getMessage(forward_msg_id);
        EMMessage.Type type = forward_msg.getType();
        switch (type) {
        case TXT:
            if(forward_msg.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION, false)){
                sendBigExpressionMessage(((TextMessageBody) forward_msg.getBody()).getMessage(),
                        forward_msg.getStringAttribute(EaseConstant.MESSAGE_ATTR_EXPRESSION_ID, null));
            }else{
                // ��ȡ��Ϣ���ݣ�������Ϣ
                String content = ((TextMessageBody) forward_msg.getBody()).getMessage();
                sendTextMessage(content);
            }
            break;
        case IMAGE:
            // ����ͼƬ
            String filePath = ((ImageMessageBody) forward_msg.getBody()).getLocalUrl();
            if (filePath != null) {
                File file = new File(filePath);
                if (!file.exists()) {
                    // �����ڴ�ͼ��������ͼ
                    filePath = EaseImageUtils.getThumbnailImagePath(filePath);
                }
                sendImageMessage(filePath);
            }
            break;
        default:
            break;
        }
        
        if(forward_msg.getChatType() == EMMessage.ChatType.ChatRoom){
            EMChatManager.getInstance().leaveChatRoom(forward_msg.getTo());
        }
    }

   
    protected EaseChatFragmentListener chatFragmentListener;
    public void setChatFragmentListener(EaseChatFragmentListener chatFragmentListener){
        this.chatFragmentListener = chatFragmentListener;
    }
    
    public interface EaseChatFragmentListener{
        /**
         * ������Ϣ��չ����
         */
        void onSetMessageAttributes(EMMessage message);
        
        /**
         * ����Ự����
         */
        void onEnterToChatDetails();
        
        /**
         * �û�ͷ�����¼�
         * @param username
         */
        void onAvatarClick(String username);
        
        /**
         * ��Ϣ���ݿ����¼�
         */
        boolean onMessageBubbleClick(EMMessage message);
        
        /**
         * ��Ϣ���ݿ򳤰��¼�
         */
        void onMessageBubbleLongClick(EMMessage message);
        
        /**
         * ��չ������item����¼�,���Ҫ����EaseChatFragment���еĵ���¼���return true
         * @param view 
         * @param itemId 
         * @return
         */
        boolean onExtendMenuItemClick(int itemId, View view);
        
        /**
         * �����Զ���chatrow�ṩ��
         * @return
         */
        EaseCustomChatRowProvider onSetCustomChatRowProvider();
    }
    
    private Handler userHandler= new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		if(msg.what==100){
    			if (chatType == EaseConstant.CHATTYPE_SINGLE) { // ����
    	            // ���ñ���
    	            if(EaseUserUtils.getUserInfo(toChatUsername) != null){
    	                set_title_text(EaseUserUtils.getUserInfo(toChatUsername).getNick());
    	            }
    	        } 
    		}
    	};
    };
    
}