package com.example.demoserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * FileName    :
 * Description :
 *
 * 当服务与所有客户端之间的绑定全部取消时，Android 系统会销毁该服务（除非还使用 startService() 调用启动了该服务）。
 * 因此，如果您的服务是纯粹的绑定服务，则无需对其生命周期进行管理，Android 系统会根据它是否绑定到任何客户端代您管理。
 */
public class SnapService extends Service {
    private static final String TAG = "SnapServer";

    public SnapService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return SnapMessageControlImpl.CREATE();
    }

}
