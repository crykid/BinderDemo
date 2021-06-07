package com.example.binderdemo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import com.example.demoserver.MessageController;
import com.example.demoserver.SnapMessage;

import java.util.List;

public class ClientMainActivity extends AppCompatActivity {
    private static final String TAG = "ClientMainActivity";
    private MessageController mMessageController;
    private boolean mIsServiceConnected;
    List<SnapMessage> mSnapMessages;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected: " + name.toShortString());
            mMessageController = MessageController.Stub.asInterface(service);
            mIsServiceConnected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected: " + name.toShortString());
            mIsServiceConnected = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindService();
    }

    private void bindService() {
        Intent serviceIntent = new Intent()
                .setComponent(new ComponentName("com.example.demoserver", "com.example.demoserver.SnapService"));
        boolean success = bindService(serviceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "bindService: success = " + success);
    }

    public void getMessage(View view) {
        Log.d(TAG, "getMessage: ");
        if (mIsServiceConnected) {
            try {
                mSnapMessages = mMessageController.getMessages();
                Log.d(TAG, "getMessage: " + mSnapMessages.size());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        printMessages();
    }

    private void printMessages() {
        Log.d(TAG, "printMessages: ");
        if (mSnapMessages == null) {
            Log.d(TAG, "printMessages: mSnapMessages == null");
            return;
        }
        for (SnapMessage snapMessage : mSnapMessages) {
            Log.d(TAG, "printMessages: " + snapMessage);
        }
    }

    public void sendMessage(View view) {
        if (mIsServiceConnected) {
            try {
                mMessageController.sendMessage(new SnapMessage("text", System.currentTimeMillis() + ""));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}