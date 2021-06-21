package com.example.binderdemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demoserver.MessageController;
import com.example.demoserver.SnapMessage;
import com.margin.base.utils.L;
import com.margin.base.utils.Snap;
import com.margin.base.utils.SnapUtil;

import java.util.List;

public class ClientMainActivity extends AppCompatActivity {
    private static final L.TAG TAG = new L.TAG("ClientMainActivity");
    private MessageController mMessageController;
    private boolean mIsServiceConnected;
    private List<SnapMessage> mSnapMessages;

    //另一个app B的包名
    private final static String TARGET_APP_PACKAGE = "com.example.demoserver";
    //app B 目标service的名字
    private final static String TARGET_SERVICE_NAME = TARGET_APP_PACKAGE + ".SnapService";
    private final static String TARGET_SERVICE_ACTION = TARGET_APP_PACKAGE + ".action";

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            L.i(TAG, "onServiceConnected: " + name.toShortString());
            mMessageController = MessageController.Stub.asInterface(service);
            mIsServiceConnected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            L.i(TAG, "onServiceDisconnected: " + name.toShortString());
            mIsServiceConnected = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.i(TAG, "onCreate");
        setContentView(R.layout.activity_main);
        SnapUtil.findMethod(this, findViewById(R.id.listview_main_snaps));
    }

    @Snap
    private void startService() {

//        // -- 显式启动
//        Intent intent = new Intent();
////        intent.setClassName(TARGET_APP_PACKAGE, TARGET_SERVICE_NAME);
//        intent.setComponent(new ComponentName(
//                TARGET_APP_PACKAGE,  //这个参数是另外一个app的包名
//                TARGET_SERVICE_NAME));//这个是要启动的Service的全路径名
//        startService(intent);

        // --- 隐式启动 ---
        Intent intent = new Intent();
        intent.setPackage(TARGET_APP_PACKAGE);
        intent.setAction(TARGET_SERVICE_ACTION);
        startService(intent);

    }

    @Snap
    public void stopService() {
        Intent intent = new Intent();
        intent.setPackage(TARGET_APP_PACKAGE);
        intent.setAction(TARGET_SERVICE_ACTION);
        stopService(intent);
    }

    @Snap
    private void bindService() {
        // --- 显式绑定 ---
        Intent serviceIntent = new Intent();
//        intent.setClassName(TARGET_APP_PACKAGE, TARGET_SERVICE_NAME);
        serviceIntent.setComponent(new ComponentName(
                TARGET_APP_PACKAGE,  //这个参数是另外一个app的包名
                TARGET_SERVICE_NAME));//这个是要启动的Service的全路径名
        boolean success = bindService(serviceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);

        // --- 隐式绑定 ---
//        Intent serviceIntent = new Intent();
//        serviceIntent.setPackage(TARGET_APP_PACKAGE);
//        serviceIntent.setAction(TARGET_SERVICE_ACTION);
//        boolean success = bindService(serviceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);

        L.d(TAG, "bindService: success = " + success);
    }

    @Snap
    public void unBindService() {
        unbindService(mServiceConnection);
    }

    @Snap
    public void getMessage() {
        L.i(TAG, "getMessage: ");
        if (mIsServiceConnected) {
            try {
                mSnapMessages = mMessageController.getMessages();
                L.d(TAG, "getMessage: " + mSnapMessages.size());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        printMessages();
    }


    private void printMessages() {
        L.i(TAG, "printMessages: ");
        if (mSnapMessages == null) {
            L.d(TAG, "printMessages: mSnapMessages == null");
            return;
        }
        for (SnapMessage snapMessage : mSnapMessages) {
            L.d(TAG, "printMessages: " + snapMessage);
        }
    }

    @Snap
    public void sendMessage() {
        if (mIsServiceConnected) {
            try {
                mMessageController.sendMessage(new SnapMessage("text", System.currentTimeMillis() + ""));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }
}