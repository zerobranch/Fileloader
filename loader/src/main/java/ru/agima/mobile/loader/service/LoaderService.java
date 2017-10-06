package ru.agima.mobile.loader.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import ru.agima.mobile.loader.core.LoadManager;
import ru.agima.mobile.loader.utils.BundleConst;

public class LoaderService extends Service {
    private volatile Looper serviceLooper;
    private volatile Handler handler;
    public static final int DEFAULT_NOTIFICATION_ID = 43534;

    @Override
    public void onCreate() {
        super.onCreate();
        final HandlerThread thread = new HandlerThread(LoaderService.class.getName());
        thread.start();
        serviceLooper = thread.getLooper();
        handler = new Handler(serviceLooper, handlerCallback);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final Message msg = handler.obtainMessage();
        msg.arg1 = startId;
        msg.obj = intent;
        handler.sendMessage(msg);
        return START_NOT_STICKY;
    }

    @SuppressWarnings("deprecation")
    private void setImmortal(Intent intent) {
        final boolean isHideNotification = intent.getBooleanExtra(BundleConst.DEFAULT_NOTIFICATION, false);
        final boolean isImmortal = intent.getBooleanExtra(BundleConst.IMMORTAL, false);
        final Notification notification = intent.getParcelableExtra(BundleConst.NOTIFICATION);
        if (isImmortal) {
            if (notification != null) {
                startForeground(DEFAULT_NOTIFICATION_ID, notification);
            } else {
                startForeground(DEFAULT_NOTIFICATION_ID, new Notification.Builder(this).build());
                if (isHideNotification) {
                    startService(new Intent(this, HideNotificationService.class));
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        serviceLooper.quit();
        serviceLooper.getThread().interrupt();
    }

    @Override
    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    @WorkerThread
    protected void onHandleIntent(Intent intent) {
        final String url = intent.getStringExtra(BundleConst.URL);
        final String path = intent.getStringExtra(BundleConst.PATH);
        final ResultReceiver receiver = intent.getParcelableExtra(BundleConst.RECEIVER);
        new LoadManager().skipIfFileExist(intent.getBooleanExtra(BundleConst.SKIP_IF_EXIST, false))
                .abortNextIfError(intent.getBooleanExtra(BundleConst.ABORT_IF_ERROR, false))
                .redownloadAttemptCount(intent.getIntExtra(BundleConst.REDOWNLOAD_COUNT, 0))
                .loadFile(path, url, receiver);
    }

    private final Handler.Callback handlerCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            final Intent intent = (Intent) msg.obj;
            setImmortal(intent);
            onHandleIntent(intent);
            stopForeground(intent.getBooleanExtra(BundleConst.VIEW_NOTIFICATION_ON_FINISH, false));
            stopSelf(msg.arg1);
            return true;
        }
    };
}
