package com.example.demoserver;

import android.os.RemoteException;
import android.util.Log;

import com.margin.base.utils.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by : mr.lu
 * Created at : 2021/6/8 at 23:27
 * Description:
 */
public class SnapMessageControlImpl extends MessageController.Stub {
    private static final L.TAG TAG = new L.TAG("MessageControl");
    private List<SnapMessage> mMessages;

    private SnapMessageControlImpl() {
        mMessages = new ArrayList<>();
        initData();
    }

    public static MessageController.Stub CREATE() {
        return new SnapMessageControlImpl();
    }

    @Override
    public List<SnapMessage> getMessages() throws RemoteException {
        L.i(TAG, "getMessages");
        return mMessages;
    }

    @Override
    public void sendMessage(SnapMessage message) throws RemoteException {
        L.i(TAG, " sendMessage >>> " + message);
        if (message == null) {
            L.i(TAG, "sendMessage: message is null");
            return;
        }
        addMessage(message);
    }

    private void initData() {
        int count = 5;
        int num = 1;
        while (num <= count) {
            mMessages.add(new SnapMessage("text", "hello," + num));
            num++;
        }
        L.d(TAG, "initData: dataSize = " + mMessages.size());
    }

    private void addMessage(SnapMessage message) {
        mMessages.add(message);
    }
}
