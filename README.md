# BinderDemo
## 服务端
    ### 创建AIDL
    在project模式下main目录下新建AIDL文件，会自动生成与java同级目录的aidl包：
  ```java
  package com.example.demoserver;

  import com.example.demoserver.SnapMessage;
  import java.util.List;
  interface MessageController {

      List<SnapMessage> getMessages();
      void sendMessage(inout SnapMessage message);
  }
  ```
  需要注意，需要import的类名必须显示的引入，并且形参需要注明inout类型；
  ### 构建 MessageController的实现类

  创建好AIDL文件之后，rebuild就会在build-generated-aidl_source_output_dir—debug-out下生成同报名目录，内有实现类：MessageController

  ### 创建服务

  新建android.app.Service的派生类，并实现onBind方法，返回MessageControl.Stub的实例:
  ```java
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
  ```

  - 最后，将该服务注册到清单文件manifest中，切记将exported属性设置为true，否则外部将不能启动它：
  ```xml
   <service
              android:name=".SnapService"
              android:enabled="true"
              android:exported="true">
              <intent-filter>
                  <action android:name="com.example.demoserver.action" />

              </intent-filter>
          </service>
  ```

  ## 客户端

  - 在Activity中，创建ServiceConnection的实例（他是服务绑定的监听），同时实现两个方法：`onServiceConnected` 和 `onServiceDisconnected`，
  并在onServiceConnected中获得IBinder的实例：
  ```java
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
  ```
  - 此时，我们获得了服务端的Binder实例mMessageController，后续就可以通过该实例与服务端交互。

  ```java
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
  ```

  - 最后，绑定服务：
  ```java
  private void bindService() {
          Intent serviceIntent = new Intent()
                  .setComponent(new ComponentName("com.example.demoserver", "com.example.demoserver.SnapService"));
          boolean success = bindService(serviceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
          Log.d(TAG, "bindService: success = " + success);
      }
  ```
  > 当服务与所有客户端之间的绑定全部取消时，Android 系统会销毁该服务（除非还使用 startService() 调用启动了该服务）。
    因此，如果您的服务是纯粹的绑定服务，则无需对其生命周期进行管理，Android 系统会根据它是否绑定到任何客户端代您管理。