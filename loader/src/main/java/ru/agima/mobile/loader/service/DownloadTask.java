//package ru.agima.mobile.loader;
//
//import android.app.Notification;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.HandlerThread;
//import android.os.IBinder;
//import android.os.Looper;
//import android.os.Message;
//import android.os.ResultReceiver;
//import android.support.v4.app.NotificationManagerCompat;
//
//import java.io.BufferedInputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.URL;
//import java.net.URLConnection;
//
//public class DownloadTask extends Service {
//    private Intent myIntent;
//    private Looper mServiceLooper;
//    private ServiceHandler mServiceHandler;
//
//    private final class ServiceHandler extends Handler {
//        public ServiceHandler(Looper looper) {
//            super(looper);
//        }
//        @Override
//        public void handleMessage(Message msg) {
//            loadFile();
//            stopSelf(msg.arg1);
//        }
//    }
//
//    @Override
//    public void onCreate() {
//        HandlerThread thread = new HandlerThread("ServiceStartArguments");
//        thread.start();
//
//        // Get the HandlerThread's Looper and use it for our Handler
//        mServiceLooper = thread.getLooper();
//        mServiceHandler = new ServiceHandler(mServiceLooper);
//
//        Notification.Builder builder = new Notification.Builder(this)
//                .setSmallIcon(android.R.drawable.arrow_down_float);
//        Notification notification;
//        notification = builder.build();
//        startForeground(777, notification);
//        Intent hideIntent = new Intent(this, HideNotificationService.class);
//        startService(hideIntent);
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        myIntent = intent;
//        Message msg = mServiceHandler.obtainMessage();
//        msg.arg1 = startId;
//        mServiceHandler.sendMessage(msg);
//        return START_STICKY;
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public void onDestroy() {
//    }
//
////    @Override
////    public void onTaskRemoved(Intent rootIntent){
//////        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
//////        restartServiceIntent.setPackage(getPackageName());
//////
//////        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
//////        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
//////        alarmService.set(
//////                AlarmManager.ELAPSED_REALTIME,
//////                SystemClock.elapsedRealtime() + 1000,
//////                restartServicePendingIntent);
////        currentFile.delete();
////        super.onTaskRemoved(rootIntent);
////    }
//
//    public static final int UPDATE_PROGRESS = 8344;
//    public static final String RECEIVER = "receiver";
//    int notificationId = 123;
//
//    File currentFile;
//    private void loadFile() {
//        System.out.println("---- start");
//        if (myIntent == null) {
//            return;
//        }
//        String urlToDownload = myIntent.getStringExtra("URL");
//        String path = myIntent.getStringExtra("PATH");
//        currentFile = new File(path, "myFile.pdf");
//
//        if (!currentFile.exists()) {
//            try {
//                currentFile.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        ResultReceiver receiver = myIntent.getParcelableExtra(RECEIVER);
//        try {
//            URL url = new URL(urlToDownload);
//            URLConnection connection = url.openConnection();
//            connection.connect();
//            // this will be useful so that you can show a typical 0-100% progress bar
//            int fileLength = connection.getContentLength();
//
//            // download the file
//            InputStream input = new BufferedInputStream(connection.getInputStream());
//            OutputStream output = new FileOutputStream(currentFile);
//
//            byte data[] = new byte[1024];
//            long total = 0;
//            int count;
//            while ((count = input.read(data)) != -1) {
//                total += count;
//                // publishing the progress....
//                Bundle resultData = new Bundle();
//                resultData.putInt("progress" ,(int) (total * 100 / fileLength));
//                receiver.send(UPDATE_PROGRESS, resultData);
//                output.write(data, 0, count);
////                System.out.println("--- " + (int) (total * 100 / fileLength));
//                showPrepareDownloadingPush((int) (total * 100 / fileLength));
////                Thread.sleep(5);
//            }
//
//            output.flush();
//            output.close();
//            input.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Bundle resultData = new Bundle();
//        resultData.putInt("progress" ,100);
//        receiver.send(UPDATE_PROGRESS, resultData);
//
//        currentFile.delete();
//    }
//
//    public void showPrepareDownloadingPush(int progress) {
//        PendingIntent contentPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), PendingIntent.FLAG_ONE_SHOT);
//        NotificationManagerCompat.from(getApplicationContext()).notify(notificationId,
//                new Notification.Builder(getApplicationContext())
//                        .setContentIntent(contentPendingIntent)
//                        .setSmallIcon(android.R.drawable.stat_sys_download)
//                        .setWhen(System.currentTimeMillis())
//                        .setProgress(100, progress, false)
//                        .setContentTitle("Заголовок")
//                        .build());
//    }
//}
