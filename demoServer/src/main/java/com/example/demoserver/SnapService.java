package com.example.demoserver;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.IBinder;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.margin.base.SnapConfig;
import com.margin.base.utils.L;


/**
 * FileName    :
 * Description :
 * <p>
 * 当服务与所有客户端之间的绑定全部取消时，Android 系统会销毁该服务（除非还使用 startService() 调用启动了该服务）。
 * 因此，如果您的服务是纯粹的绑定服务，则无需对其生命周期进行管理，Android 系统会根据它是否绑定到任何客户端代您管理。
 */
public class SnapService extends Service {
    private static final L.TAG TAG = new L.TAG("SnapServer");
    private final static String CHANNEL_ID = "snap_simple";

    public SnapService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        L.i(TAG, "onCreate: ");
        //服务前台化
//        sendNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final String action = intent.getAction();
        if (!TextUtils.isEmpty(action)) {
            L.i(TAG, "onStartCommand: action = " + action);
            switch (action) {
                case SnapAction.ACTION_ENABLE_LOG:
                    int enableLog = intent.getIntExtra(SnapAction.EXTRA_INT_ENABLE_LOG, 0);
                    SnapConfig.LOG_ENABLE = enableLog == 0;
                    L.i(TAG, "enable_log = " + enableLog);
                    break;
                case SnapAction.ACTION_OPEN_BROWSER:
                    String stringExtra = intent.getStringExtra(SnapAction.EXTRA_STRING_URL);
                    L.d(TAG, stringExtra);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @SuppressLint("NewApi")
    private void sendNotification() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_HIGH);
        channel.enableLights(true);//设置提示灯
        channel.setLightColor(Color.BLUE);//设置提示灯颜色
        channel.setShowBadge(true);//显示logo
        channel.setDescription("test");//设置描述
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);//锁屏可见
        manager.createNotificationChannel(channel);

        Notification this_is_a_test_notification = new Notification.Builder(this)
                .setChannelId(CHANNEL_ID)
                .setContentText("服务前台化")
                .setContentText("服务已经至于前台了")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .build();
        //id 不能是0
        startForeground(1, this_is_a_test_notification);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        L.i(TAG, "onBind: ");
        return SnapMessageControlImpl.CREATE();
    }



    @Override
    public boolean onUnbind(Intent intent) {
        L.i(TAG, "onUnbind: ");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        L.i(TAG, "onDestroy: ");
        super.onDestroy();
    }
}
