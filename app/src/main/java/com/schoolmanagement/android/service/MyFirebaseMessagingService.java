package com.schoolmanagement.android.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import com.schoolmanagement.android.R;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.schoolmanagement.android.BuildConfig;
import com.schoolmanagement.android.activities.MainActivity;
import com.schoolmanagement.android.utils.DebugLog;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.annotations.Nullable;

/**
 * Created by j on 10/10/16.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    /**
     * Request code used for notification action.
     */
    public static final int REQUEST_NOTIFICATION_ACTION = 12345;

    /**
     * Used to compare action code for doctor request in push notification.
     */
    public static final String ACTION_CODE_DOCTOR_REQUEST = /*BuildConfig.APPLICATION_ID +*/ "NETWORK_JOIN_REQUEST";

    /**
     * Notification id for notifications other than doctor request.
     */
    public static final int NOTIFICATION_ID = 12;

    /**
     * Notification id for doctor request notification.
     */
    public static final int NOTIFICATION_ID_DOCTOR_REQUEST = 0;
    /**
     * Notification id for doctor doctor conversation message.
     */
    public static final int NOTIFICATION_ID_DOCTOR_MESSAGE = 123;

    /**
     * Notification id for referral confirmation notification.
     */
    public static final int NOTIFICATION_ID_REFERRAL_CONFIRM = 1;
    /**
     * Notification id for comment on case.
     */
    public static final int NOTIFICATION_ID_CASE_COMMENT = 120;
    /**
     * Notification id for Case collaboration request.
     */
    public static final int NOTIFICATION_ID_CASE_COLLABORATION_REQUEST = 130;
    /**
     * Action for doctor invitation notification.
     */
    public static final String ACTION_ACCEPT = BuildConfig.APPLICATION_ID + "ACTION_ACCEPT";

    /**
     * Action code for user verification notification
     */
    public static final String ACTION_CODE_VERIFICATION_SUCCESS = BuildConfig.APPLICATION_ID + "VERIFICATION_SUCCESS";

    /**
     * Notification channel id to create notification channel for above SDK version 26
     */
    public static final String NOTIFICATION_CHANNEL_ID = "notification_channel_id";

    public static final String ACTION_IGNORE = BuildConfig.APPLICATION_ID + "ACTION_IGNORE";
    public static final String ACTION_CLOSE = BuildConfig.APPLICATION_ID + "ACTION_CLOSE";
    /**
     * Constant for parsing data, which received from firebase messaging.
     */
    private static final String TITLE = "title";
    private static final String ACTION_CODE = "actionCode";
    private static final String ACTION_DATA = "actionData";
    private static final String MESSAGE_BODY = "body";

    public static final String ACTION_CODE_DOCTOR_MESSAGE = "DDC_POST_MESSAGE";
    public static final String ACTION_CODE_CASE_COMMENT_MESSAGE = "CASE_COMMENT";
    public static final String ACTION_CODE_CASE_COLLABORATION_REQUEST = "CASE_COLLABORATION_REQUEST";
    public static final String NETWORK_JOIN_REQUEST_RESPONSE = "NETWORK_JOIN_REQUEST_RESPONSE";
    public static final String ACTION_CODE_REQUEST_WITHDRAW = "REQUEST_WITHDRAW";
    public static final String DOCTOR_NETWORK_CHANNEL_NAME = "Doctor Network";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        //DebugLog.d("From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            DebugLog.d("DoctorPatientInteractionMessage data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            DebugLog.d("DoctorPatientInteractionMessage Notification Body: "
                    + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

        //Calling method to generate notification
        // sendNotification(remoteMessage.getData().get("DoctorPatientInteractionMessage"));
       /* if(remoteMessage.getNotification() != null ){
             String title = remoteMessage.getNotification().getTitle();
             String actionCode = remoteMessage.getData().get("actionCode");

            if(remoteMessage.getData().get("actionData") != null){
                sendNotification(remoteMessage.getNotification().getBody(),title,actionCode,
                remoteMessage.getData().get("actionData"));
            }else {
                sendNotification(remoteMessage.getNotification().getBody(),title,actionCode,null);
            }
        }*/
        String title = remoteMessage.getData().get(TITLE);
        String actionCode = remoteMessage.getData().get(ACTION_CODE);
        String body = remoteMessage.getData().get(MESSAGE_BODY);

        if (remoteMessage.getData().get(ACTION_DATA) != null) {
            // To initiate sync to update local database
            // SyncUtils.forceRefreshAll(getApplicationContext(), null);
            sendNotification(body, title, actionCode, remoteMessage.getData().get(ACTION_DATA));
        } else if (remoteMessage.getData().get("sendbird") != null) {
            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
                DebugLog.d("Message Notification Body: " + remoteMessage.getNotification().getBody());
            }

            String channelUrl = null;
            try {
                JSONObject sendBird = new JSONObject(remoteMessage.getData().get("sendbird"));
                JSONObject channel = (JSONObject) sendBird.get("channel");
                channelUrl = (String) channel.get("channel_url");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Also if you intend on generating your own notifications as a result of a received FCM
            // message, here is where that should be initiated. See sendNotification method below.
            // sendNotification(this, remoteMessage.getData().get("message"), channelUrl);
        } else {
            sendNotification(body, title, actionCode, null);
        }

        DebugLog.i(remoteMessage.getData().get("DoctorPatientInteractionMessage"));
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     * @param title
     * @param actionCode
     * @param actionData
     */
    private void sendNotification(String messageBody, String title, String actionCode,
                                  String actionData) {


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager == null) {
            DebugLog.e("notificationManager is null");
            return;
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0   /*Request code*/, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder notificationChannelBuilder = null;
        NotificationCompat.Builder notificationBuilder = null;
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher
        );
        // Create the NotificationChannel, but only on API 26+
        // because the NotificationChannel class is new and not in the support library
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(MyFirebaseMessagingService.NOTIFICATION_CHANNEL_ID,
                    DOCTOR_NETWORK_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationManager.createNotificationChannel(notificationChannel);

            notificationChannelBuilder = new Notification.Builder(getApplicationContext(), MyFirebaseMessagingService.NOTIFICATION_CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setColor(ContextCompat.getColor(this, android.R.color.background_dark))
                    //.setSmallIcon(R.drawable.ic_notification_icon)
                    .setLargeIcon(largeIcon)
                    .setAutoCancel(true);
        } else {
            notificationBuilder = new NotificationCompat.Builder(this)
                   // .setSmallIcon(R.drawable.ic_notification_icon)
                    //.setColor(ContextCompat.getColor(this, R.color.hpcore_blue))
                    .setLargeIcon(largeIcon)
                    .setContentTitle(title)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                    .setContentText(messageBody)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri);
        }

        if (actionCode == null) {
            notifyApp(notificationChannelBuilder, notificationBuilder, notificationManager, pendingIntent, NOTIFICATION_ID);
        }
       /* if (actionCode.equalsIgnoreCase(ACTION_CODE_VERIFICATION_SUCCESS)) {
            Intent verificationIntent = new Intent(this, CommonActivity.class);
            verificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent =
                    PendingIntent.getActivity(this, 0  *//* Request code *//*,
                            verificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            sendBroadcast(ACTION_CODE_VERIFICATION_SUCCESS);
            notifyApp(notificationChannelBuilder, notificationBuilder, notificationManager, pendingIntent, NOTIFICATION_ID);

        } else if (actionCode.equalsIgnoreCase(ACTION_CODE_DOCTOR_REQUEST)) {
            // To send intent to update the doctor list and show the doctor request.
            DebugLog.v(ACTION_CODE_DOCTOR_REQUEST);
            Intent requestIntent = new Intent(this, CommonActivity.class);
            requestIntent.putExtra(CommonActivity.INTENT_EXTRAS_FRAGMENT, InvitationsTabFragmentV2.class.getSimpleName());
            PendingIntent requestScreenIntent =
                    PendingIntent.getActivity(this, 0  *//* Request code *//*,
                            requestIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            sendBroadcast(ACTION_CODE_DOCTOR_REQUEST);
            NetworkingDoctor networkingDoctor = new Gson().fromJson(actionData, NetworkingDoctor.class);
            TemporaryCache.getInstance().put(DoctorInvitationService.DOCTOR_REQUEST_ACTION_DATA, networkingDoctor);
            // Accept intent
            Intent acceptIntent = new Intent(this, DoctorInvitationService.class);
            acceptIntent.setAction(ACTION_ACCEPT);
            PendingIntent pendingIntentAccept =
                    PendingIntent.getService(this, REQUEST_NOTIFICATION_ACTION,
                            acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            if (notificationBuilder != null) {
                notificationBuilder.addAction(R.drawable.ic_done, getString(R.string.accept), pendingIntentAccept);
            } else {
                notificationChannelBuilder.addAction(R.drawable.ic_done, getString(R.string.accept), pendingIntentAccept);
            }
            // Ignore intent
            Intent ignoreIntent = new Intent(this, DoctorInvitationService.class);
            ignoreIntent.setAction(ACTION_IGNORE);
            PendingIntent pendingIntentIgnore =
                    PendingIntent.getService(this, REQUEST_NOTIFICATION_ACTION, ignoreIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
            if (notificationBuilder != null) {
                notificationBuilder.addAction(R.drawable.ic_cancel, getString(R.string.ignore), pendingIntentIgnore);
            } else {
                notificationChannelBuilder.addAction(R.drawable.ic_cancel, getString(R.string.ignore), pendingIntentIgnore);
            }
            notifyApp(notificationChannelBuilder, notificationBuilder, notificationManager, requestScreenIntent, NOTIFICATION_ID_DOCTOR_REQUEST);

        } else if (actionCode.equalsIgnoreCase(ACTION_CODE_DOCTOR_MESSAGE)) {
            // Doctor-Doctor conversation messages.
            DebugLog.v(ACTION_CODE_DOCTOR_MESSAGE);
            DoctorMessageNotification doctorMessageNotification =
                    new Gson().fromJson(actionData, DoctorMessageNotification.class);
            TemporaryCache.getInstance().put(DoctorConversationFragment.EXTRA_DOCTOR_DETAILS,
                    doctorMessageNotification);
            Intent messageIntent =
                    new Intent(this, ConversationActivity.class);
            messageIntent.putExtra(ConversationActivity.EXTRA_IS_DOCTOR_CONVERSATION,
                    true);
            messageIntent.putExtra(ConversationActivity.DOCTOR_TITLE, title);
            PendingIntent messagePendingIntent =
                    PendingIntent.getActivity(this, 0  *//* Request code *//*,
                            messageIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            sendBroadcast(ACTION_CODE_DOCTOR_MESSAGE);
            notifyApp(notificationChannelBuilder, notificationBuilder, notificationManager, messagePendingIntent, NOTIFICATION_ID_DOCTOR_MESSAGE);

        } else if (actionCode.equalsIgnoreCase(NETWORK_JOIN_REQUEST_RESPONSE)) {
            // Network Join request
            NetworkingDoctor networkingDoctor =
                    new Gson().fromJson(actionData, NetworkingDoctor.class);
            Intent networkIntent = new Intent(this, CommonActivity.class);
            networkIntent.putExtra(CommonActivity.INTENT_EXTRAS_FRAGMENT, BaseConnectionListFragment.class.getSimpleName());
            networkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            networkIntent.putExtra(NetworksFragment.FLAG_NETWORK_REQUEST_STATUS,
                    networkingDoctor.getStatus());
            User user = BaseAccountManager.getInstance(getApplicationContext(),
                    HealthPoleConfig.getInstance().getSyncAccountType())
                    .getUserDetails();
            DebugLog.v("user details " + new Gson().toJson(user));
            TemporaryCache.getInstance().put(BaseConnectionListFragment.EXTRA_DOCTOR_CONNECTION_DETAILS, user);
            pendingIntent = PendingIntent.getActivity(this,
                    0  *//* Request code *//*, networkIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT);
            sendBroadcast(NETWORK_JOIN_REQUEST_RESPONSE);
            notifyApp(notificationChannelBuilder, notificationBuilder, notificationManager, pendingIntent, NOTIFICATION_ID);
        } else if (actionCode.equalsIgnoreCase(ACTION_CODE_CASE_COMMENT_MESSAGE)) {
            DebugLog.v(ACTION_CODE_CASE_COMMENT_MESSAGE);
            Comment caseComment = new Gson().fromJson(actionData, Comment.class);
            TemporaryCache.getInstance().put(CaseDetailsFragment.EXTRAS_CASE_DETAILS, caseComment);

            Intent messageIntent = new Intent(this, CaseDetailsActivity.class);
            messageIntent.putExtra(CaseDetailsActivity.INTENT_EXTRAS_FRAGMENT, CaseDetailsFragment.class.getSimpleName());

            PendingIntent messagePendingIntent = PendingIntent.getActivity(this, 0  *//* Request code *//*, messageIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            sendBroadcast(ACTION_CODE_CASE_COMMENT_MESSAGE);
            notifyApp(notificationChannelBuilder, notificationBuilder, notificationManager, messagePendingIntent, NOTIFICATION_ID_CASE_COMMENT);
        } else if (actionCode.equalsIgnoreCase(ACTION_CODE_CASE_COLLABORATION_REQUEST)) {
            DebugLog.v(ACTION_CODE_CASE_COLLABORATION_REQUEST);
            CollaborationRequest collaborationRequest = new Gson().fromJson(actionData, CollaborationRequest.class);
            TemporaryCache.getInstance().put(CaseRequestFragment.EXTRAS_CASE_REQUEST_DETAILS, collaborationRequest);

            Intent messageIntent = new Intent(this, CommonActivity.class);
            messageIntent.putExtra(CommonActivity.INTENT_EXTRAS_FRAGMENT, CaseRequestFragment.class.getSimpleName());

            PendingIntent messagePendingIntent = PendingIntent.getActivity(this, 0  *//* Request code *//*, messageIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            sendBroadcast(ACTION_CODE_CASE_COLLABORATION_REQUEST);
            notifyApp(notificationChannelBuilder, notificationBuilder, notificationManager, messagePendingIntent, NOTIFICATION_ID_CASE_COLLABORATION_REQUEST);
        } */
        else {
            notifyApp(notificationChannelBuilder, notificationBuilder, notificationManager, pendingIntent, NOTIFICATION_ID);
        }
    }

    /**
     * Notifying app
     * Need to check builder here, because now we are providing push notification above android 8.0.
     * below API 26 {@link Notification.Builder} supported for notifying and for above API 26 {@link NotificationCompat.Builder} supports.
     *
     * @param notificationChannelBuilder
     * @param notificationBuilder
     * @param notificationManager
     * @param pendingIntent
     * @param notificationId
     */
    public void notifyApp(@Nullable Notification.Builder notificationChannelBuilder, @Nullable NotificationCompat.Builder notificationBuilder,
                          NotificationManager notificationManager, PendingIntent pendingIntent, int notificationId) {
        DebugLog.v("notifying app");
        if (notificationChannelBuilder == null) {
            notificationBuilder.setContentIntent(pendingIntent);
            notificationManager.notify(notificationId, notificationBuilder.build());
        } else {
            notificationChannelBuilder.setContentIntent(pendingIntent);
            notificationManager.notify(notificationId, notificationChannelBuilder.build());
        }
    }

    /**
     * To get notification channel to apply channel to notification.
     *
     * @param notificationChannelName
     * @param notificationChannelDescription
     * @return
     */
    public static NotificationChannel getNotificationChannel(String notificationChannelName,
                                                             String notificationChannelDescription) {
        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    notificationChannelName,
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(notificationChannelDescription);
        }
        return notificationChannel;
    }

    /**
     * @param clinicRequestSend
     */
    private void sendBroadcast(String clinicRequestSend) {
        Intent intent = new Intent();
        intent.setAction(clinicRequestSend);
        sendBroadcast(intent);
    }

    /**
     * Remove notification from status bar.
     *
     * @param context
     * @param notificationId
     */
    public static void cancelNotification(Context context, int notificationId) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notificationId);
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    /*public static void sendNotification(Context context, String messageBody, String channelUrl) {
        com.sendbird.android.User user = SendBird.getCurrentUser();
        DebugLog.v("Send bird User: " + user == null ? "null" : new Gson().toJson(user));
        Doctor doctor = BaseAccountManager.getInstance(context, BuildConfig.SYNC_ACCOUNT_TYPE).getUserDetails();
        if (doctor != null) {
            connectToSendBird(doctor);
        }

        Intent intent = new Intent(context, GroupChannelActivity.class);
        intent.putExtra("groupChannelUrl", channelUrl);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 *//* Request code *//*, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(com.healthpole.messaging.core.R.mipmap.ic_launcher)
                .setContentTitle(context.getResources().getString(com.healthpole.messaging.core.R.string.app_name))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent);

        if (PreferenceUtils.getNotificationsShowPreviews()) {
            notificationBuilder.setContentText(messageBody);
        } else {
            notificationBuilder.setContentText("Somebody sent you a message.");
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 *//* ID of notification *//*, notificationBuilder.build());
    }

    private static void connectToSendBird(Doctor doctor) {
        ConnectionManager.login(String.valueOf(doctor.getId()), new SendBird.ConnectHandler() {
            @Override
            public void onConnected(com.sendbird.android.User user, SendBirdException e) {
                // Callback received; hide the progress bar.
                // showProgressBar(false);

                if (e != null) {
                    // Error!
                    DebugLog.e("" + e.getCode() + ": " + e.getMessage());
                    // showMessage("Login to SendBird failed " + e.getCode() + ": " + e.getMessage());

                    //mConnectButton.setEnabled(true);
                    PreferenceUtils.setUserId(user.getUserId());
                    PreferenceUtils.setNickname(user.getNickname());
                    PreferenceUtils.setProfileUrl(user.getProfileUrl());
                    PreferenceUtils.setConnected(true);
                    return;
                }}});
    }*/
}