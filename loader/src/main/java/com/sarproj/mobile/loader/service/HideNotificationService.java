package com.sarproj.mobile.loader.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class HideNotificationService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onCreate() {
        startForeground(LoaderService.DEFAULT_NOTIFICATION_ID, new Notification.Builder(this).build());
        stopForeground(true);
    }
}
