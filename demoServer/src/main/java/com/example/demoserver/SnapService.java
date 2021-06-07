package com.example.demoserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * FileName    :
 * Description :
 */
public class SnapService extends Service {
    private static final String TAG = "SnapServer";
    private List<SnapMessage> mMessages;

    public SnapService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        mMessages = new ArrayList<>();
        initData();
    }

    private void initData() {
        int count = 5;
        int num = 1;
        while (num <= count) {
            mMessages.add(new SnapMessage("text", "hello," + num));
            num++;
        }
        Log.d(TAG, "initData: dataSize = " + mMessages.size());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mStub;
    }

    private final MessageController.Stub mStub = new MessageController.Stub() {
        @Override
        public List<SnapMessage> getMessages() throws RemoteException {
            Log.d(TAG, "getMessages");
            return mMessages;
        }

        @Override
        public void sendMessage(SnapMessage message) throws RemoteException {
            Log.d(TAG, " sendMessage >>> " + message);
            if (message == null) {
                Log.d(TAG, "sendMessage: message is null");
                return;
            }
            addMessage(message);
        }
    };

    private void addMessage(SnapMessage message) {
        mMessages.add(message);
    }
}
