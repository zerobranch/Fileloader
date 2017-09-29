package ru.agima.mobile.loader.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

public class LoaderService extends Service {
    private volatile Looper mServiceLooper;
//    private volatile ServiceHandler mServiceHandler;
    private volatile Handler handler;
    private String mName;
    private boolean mRedelivery;

//    private final class ServiceHandler extends Handler {
//        public ServiceHandler(Looper looper) {
//            super(looper);
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            onHandleIntent((Intent)msg.obj);
//            stopSelf(msg.arg1);
//        }
//    }

    Handler.Callback callback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            onHandleIntent((Intent)msg.obj);
            stopSelf(msg.arg1);
            return true;
        }
    };

    public LoaderService(String name) {
        mName = name;
    }

    public void setIntentRedelivery(boolean enabled) {
        mRedelivery = enabled;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread thread = new HandlerThread("IntentService[" + mName + "]");
        thread.start();

        mServiceLooper = thread.getLooper();
//        mServiceHandler = new ServiceHandler(mServiceLooper);

        handler = new Handler(mServiceLooper, callback);
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        Message msg = handler.obtainMessage();
        msg.arg1 = startId;
        msg.obj = intent;
        handler.sendMessage(msg);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        onStart(intent, startId);
        return mRedelivery ? START_REDELIVER_INTENT : START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        mServiceLooper.quit();
    }

    @Override
    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    @WorkerThread
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
