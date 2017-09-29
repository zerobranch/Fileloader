//package ru.agima.mobile.loader;
//
//import android.app.Notification;
//import android.app.Service;
//import android.content.Intent;
//import android.os.IBinder;
//
///**
// * Created by arman on 20.09.17.
// */
//
//public class HideNotificationService extends Service {
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public void onCreate() {
//        Notification.Builder builder = new Notification.Builder(this)
//                .setSmallIcon(android.R.drawable.btn_dialog);
//        Notification notification;
//        notification = builder.build();
//        startForeground(777, notification);
//        stopForeground(true);
//    }
//}
