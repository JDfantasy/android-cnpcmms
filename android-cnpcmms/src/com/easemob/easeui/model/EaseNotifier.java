/************************************************************
 *  * EaseMob CONFIDENTIAL 
 * __________________ 
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved. 
 *  
 * NOTICE: All information contained herein is, and remains 
 * the property of EaseMob Technologies.
 * Dissemination of this information or reproduction of this material 
 * is strictly forbidden unless prior written permission is obtained
 * from EaseMob Technologies.
 */
package com.easemob.easeui.model;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import com.cnpc.zhibo.app.R;
import com.cnpc.zhibo.app.application.SysApplication;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.easeui.controller.EaseUI;
import com.easemob.easeui.controller.EaseUI.EaseSettingsProvider;
import com.easemob.util.EMLog;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

/**
 * ����Ϣ����class
 * 
 * this class is subject to be inherited and implement the relative APIs
 */
public class EaseNotifier {
    private final static String TAG = "notify";
    Ringtone ringtone = null;

    protected final static String[] msg_eng = { "sent a message", "sent a picture", "sent a voice",
                                                "sent location message", "sent a video", "sent a file", "%1 contacts sent %2 messages"
                                              };
    protected final static String[] msg_ch = { "����һ����Ϣ", "����һ��ͼƬ", "����һ������", "����λ����Ϣ", "����һ����Ƶ", "����һ���ļ�",
                                               "%1����ϵ�˷���%2����Ϣ"
                                             };

    protected static int notifyID = 0525; // start notification id
    protected static int foregroundNotifyID = 0555;

    protected NotificationManager notificationManager = null;

    protected HashSet<String> fromUsers = new HashSet<String>();
    protected int notificationNum = 0;

    protected Context appContext;
    protected String packageName;
    protected String[] msgs;
    protected long lastNotifiyTime;
    protected AudioManager audioManager;
    protected Vibrator vibrator;
    protected EaseNotificationInfoProvider notificationInfoProvider;

    public EaseNotifier() {
    }
    
    /**
     * �����߿������ش˺���
     * this function can be override
     * @param context
     * @return
     */
    public EaseNotifier init(Context context){
        appContext = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        packageName = appContext.getApplicationInfo().packageName;
        if (Locale.getDefault().getLanguage().equals("zh")) {
            msgs = msg_ch;
        } else {
            msgs = msg_eng;
        }

        audioManager = (AudioManager) appContext.getSystemService(Context.AUDIO_SERVICE);
        vibrator = (Vibrator) appContext.getSystemService(Context.VIBRATOR_SERVICE);
        
        return this;
    }
    
    /**
     * �����߿������ش˺���
     * this function can be override
     */
    public void reset(){
        resetNotificationCount();
        cancelNotificaton();
    }

    void resetNotificationCount() {
        notificationNum = 0;
        fromUsers.clear();
    }
    
    void cancelNotificaton() {
        if (notificationManager != null)
            notificationManager.cancel(notifyID);
    }

    /**
     * �������յ�����Ϣ��Ȼ����֪ͨ
     * 
     * �����߿������ش˺���
     * this function can be override
     * 
     * @param message
     */
    public synchronized void onNewMsg(EMMessage message) {
        if(EMChatManager.getInstance().isSlientMessage(message)){
            return;
        }
        EaseSettingsProvider settingsProvider = EaseUI.getInstance().getSettingsProvider();
        if(!settingsProvider.isMsgNotifyAllowed(message)){
            return;
        }
        // �ж�app�Ƿ��ں�̨
//        if (!EasyUtils.isAppRunningForeground(appContext)) {
//            EMLog.d(TAG, "app is running in backgroud");
            sendNotification(message, false);
//        } else {
//            sendNotification(message, true);
//        }
            viberateAndPlayTone(message);
    }
    
    public synchronized void onNewMesg(List<EMMessage> messages) {
        if(EMChatManager.getInstance().isSlientMessage(messages.get(messages.size()-1))){
            return;
        }
        EaseSettingsProvider settingsProvider = EaseUI.getInstance().getSettingsProvider();
        if(!settingsProvider.isMsgNotifyAllowed(null)){
            return;
        }
        // �ж�app�Ƿ��ں�̨
//        if (!EasyUtils.isAppRunningForeground(appContext)) {
//            EMLog.d(TAG, "app is running in backgroud");
//            sendNotification(messages, false);
//        } else {
//            sendNotification(messages, true);
//        }
        sendNotification(messages, false);
        viberateAndPlayTone(messages.get(messages.size()-1));
    }

    /**
     * ����֪ͨ����ʾ
     * This can be override by subclass to provide customer implementation
     * @param messages
     * @param isForeground
     */
    protected void sendNotification (List<EMMessage> messages, boolean isForeground){
        for(EMMessage message : messages){
            if(!isForeground){
                notificationNum++;
                fromUsers.add(message.getFrom());
            }
        }
        sendNotification(messages.get(messages.size()-1), isForeground, false);
    }
    
    protected void sendNotification (EMMessage message, boolean isForeground){
        sendNotification(message, isForeground, true);
    }
    
    /**
     * ����֪ͨ����ʾ
     * This can be override by subclass to provide customer implementation
     * @param message
     */
    protected void sendNotification(EMMessage message, boolean isForeground, boolean numIncrease) {
        String username = message.getFrom();
        try {
            String notifyText = username + " ";
            switch (message.getType()) {
            case TXT:
                notifyText += msgs[0];
                break;
            case IMAGE:
                notifyText += msgs[1];
                break;
            case VOICE:
                notifyText += msgs[2];
                break;
            case LOCATION:
                notifyText += msgs[3];
                break;
            case VIDEO:
                notifyText += msgs[4];
                break;
            case FILE:
                notifyText += msgs[5];
                break;
            }
            PackageManager packageManager = appContext.getPackageManager();
            String appname = (String) packageManager.getApplicationLabel(appContext.getApplicationInfo());
            
            // notification titile
            String contentTitle = appname;
            if (notificationInfoProvider != null) {
                String customNotifyText = notificationInfoProvider.getDisplayedText(message);
                String customCotentTitle = notificationInfoProvider.getTitle(message);
                if (customNotifyText != null){
                    // �����Զ����״̬����ʾ����
                    notifyText = customNotifyText;
                }
                    
                if (customCotentTitle != null){
                    // �����Զ����֪ͨ������
                    contentTitle = customCotentTitle;
                }   
            }

            // create and send notificaiton
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(appContext)
                                                                        .setSmallIcon(appContext.getApplicationInfo().icon)
                                                                        .setWhen(System.currentTimeMillis())
                                                                        .setAutoCancel(true);

            Intent msgIntent = appContext.getPackageManager().getLaunchIntentForPackage(packageName);
            if (notificationInfoProvider != null) {
                // �����Զ����notification�����תintent
                msgIntent = notificationInfoProvider.getLaunchIntent(message);
            }

            PendingIntent pendingIntent = PendingIntent.getActivity(appContext, notifyID, msgIntent,PendingIntent.FLAG_UPDATE_CURRENT);

            if(numIncrease){
                // prepare latest event info section
                if(!isForeground){
                    notificationNum++;
                    fromUsers.add(message.getFrom());
                }
            }

            int fromUsersNum = fromUsers.size();
            String summaryBody = msgs[6].replaceFirst("%1", Integer.toString(fromUsersNum)).replaceFirst("%2",Integer.toString(notificationNum));
            
            if (notificationInfoProvider != null) {
                // lastest text
                String customSummaryBody = notificationInfoProvider.getLatestText(message, fromUsersNum,notificationNum);
                if (customSummaryBody != null){
                    summaryBody = customSummaryBody;
                }
                // small icon
                int smallIcon = notificationInfoProvider.getSmallIcon(message);
                if (smallIcon != 0){
                    mBuilder.setSmallIcon(smallIcon);
                }
            }
            mBuilder.setContentTitle(contentTitle);
            mBuilder.setTicker(notifyText);
            mBuilder.setContentText(summaryBody);
            mBuilder.setContentIntent(pendingIntent);
            mBuilder.setNumber(notificationNum);
            Notification notification = mBuilder.build();
            if (isForeground) {
                notificationManager.notify(foregroundNotifyID, notification);
                notificationManager.cancel(foregroundNotifyID);
            } else {
            	if(message.getFrom().equals("admin")){//�����admin��������Ϣ�ֿ���ʾ
            		int num = (int)(Math.random()*1000);
            		notificationManager.notify(num, notification);
            	}else{//����������û���������Ϣ���ϲ���ʾ
            		notificationManager.notify(notifyID, notification);
            	}
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * �ֻ��𶯺�������ʾ
     */
    public void viberateAndPlayTone(EMMessage message) {
        if(message != null){
            if(EMChatManager.getInstance().isSlientMessage(message)){
                return;
            } 
        }
        
        
        if (System.currentTimeMillis() - lastNotifiyTime < 1000) {
            // received new messages within 2 seconds, skip play ringtone
            return;
        }
        
        try {
            lastNotifiyTime = System.currentTimeMillis();
            
            // �ж��Ƿ��ھ���ģʽ
            if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
                EMLog.e(TAG, "in slient mode now");
                return;
            }
            EaseSettingsProvider settingsProvider = EaseUI.getInstance().getSettingsProvider();
            if(settingsProvider.isMsgVibrateAllowed(message)){
                long[] pattern = new long[] { 0, 180, 80, 120 };
                vibrator.vibrate(pattern, -1);
            }

            if(settingsProvider.isMsgSoundAllowed(message)){
                if (ringtone == null) {
//                    Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Uri notificationUri = Uri.parse("android.resource://" + SysApplication.getInstance().getPackageName() + "/" + R.raw.notify_sound);

                    ringtone = RingtoneManager.getRingtone(appContext, notificationUri);
                    if (ringtone == null) {
                        EMLog.d(TAG, "cant find ringtone at:" + notificationUri.getPath());
                        return;
                    }
                }
                
                if (!ringtone.isPlaying()) {
                    String vendor = Build.MANUFACTURER;
                    
                    ringtone.play();
                    // for samsung S3, we meet a bug that the phone will
                    // continue ringtone without stop
                    // so add below special handler to stop it after 3s if
                    // needed
                    if (vendor != null && vendor.toLowerCase().contains("samsung")) {
                        Thread ctlThread = new Thread() {
                            public void run() {
                                try {
                                    Thread.sleep(3000);
                                    if (ringtone.isPlaying()) {
                                        ringtone.stop();
                                    }
                                } catch (Exception e) {
                                }
                            }
                        };
                        ctlThread.run();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * ����֪ͨ����ϢProvider
     * 
     * @param provider
     */
    public void setNotificationInfoProvider(EaseNotificationInfoProvider provider) {
        notificationInfoProvider = provider;
    }

    public interface EaseNotificationInfoProvider {
        /**
         * ���÷���notificationʱ״̬����ʾ����Ϣ������(����Xxx������һ��ͼƬ��Ϣ)
         * 
         * @param message
         *            ���յ�����Ϣ
         * @return nullΪʹ��Ĭ��
         */
        String getDisplayedText(EMMessage message);

        /**
         * ����notification������ʾ������Ϣ��ʾ(����2����ϵ�˷�����5����Ϣ)
         * 
         * @param message
         *            ���յ�����Ϣ
         * @param fromUsersNum
         *            �����˵�����
         * @param messageNum
         *            ��Ϣ����
         * @return nullΪʹ��Ĭ��
         */
        String getLatestText(EMMessage message, int fromUsersNum, int messageNum);

        /**
         * ����notification����
         * 
         * @param message
         * @return nullΪʹ��Ĭ��
         */
        String getTitle(EMMessage message);

        /**
         * ����Сͼ��
         * 
         * @param message
         * @return 0ʹ��Ĭ��ͼ��
         */
        int getSmallIcon(EMMessage message);

        /**
         * ����notification���ʱ����תintent
         * 
         * @param message
         *            ��ʾ��notification�������һ����Ϣ
         * @return nullΪʹ��Ĭ��
         */
        Intent getLaunchIntent(EMMessage message);
    }
}