package ru.agima.mobile.loader.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class NewIntentService extends IntentService {
    private Intent myIntent;
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            loadFile();
            stopSelf(msg.arg1);
        }
    }

    public NewIntentService() {
        super("defaultAsd");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread thread = new HandlerThread("ServiceStartArguments");
        thread.start();

//         Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.arrow_down_float);
        Notification notification;
        notification = builder.build();
        startForeground(777, notification);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        myIntent = intent;
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = 123;
        mServiceHandler.sendMessage(msg);
    }

    public static final int UPDATE_PROGRESS = 8344;
    public static final String RECEIVER = "receiver";
    int notificationId = 123;

    File currentFile;
    private void loadFile() {
        System.out.println("---- start");
        if (myIntent == null) {
            return;
        }
        String urlToDownload = myIntent.getStringExtra("URL");
        String path = myIntent.getStringExtra("PATH");
        currentFile = new File(path, "myFile.pdf");

        if (!currentFile.exists()) {
            try {
                currentFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ResultReceiver receiver = myIntent.getParcelableExtra(RECEIVER);
        try {
            URL url = new URL(urlToDownload);
            URLConnection connection = url.openConnection();
            connection.connect();
            // this will be useful so that you can show a typical 0-100% progress bar
            int fileLength = connection.getContentLength();

            // download the file
            InputStream input = new BufferedInputStream(connection.getInputStream());
            OutputStream output = new FileOutputStream(currentFile);

            byte data[] = new byte[1024];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                Bundle resultData = new Bundle();
                resultData.putInt("progress" ,(int) (total * 100 / fileLength));
                receiver.send(UPDATE_PROGRESS, resultData);
                output.write(data, 0, count);
//                System.out.println("--- " + (int) (total * 100 / fileLength));
                if ((int) (total * 100 / fileLength) % 5 == 0) {
                    showPrepareDownloadingPush((int) (total * 100 / fileLength));
                }
                Thread.sleep(100);
            }

            output.flush();
            output.close();
            input.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        Bundle resultData = new Bundle();
        resultData.putInt("progress" ,100);
        receiver.send(UPDATE_PROGRESS, resultData);

        currentFile.delete();
        stopForeground(true);
    }

    public void showPrepareDownloadingPush(int progress) {
        PendingIntent contentPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), PendingIntent.FLAG_ONE_SHOT);
        NotificationManagerCompat.from(getApplicationContext()).notify(notificationId,
                new Notification.Builder(getApplicationContext())
                        .setContentIntent(contentPendingIntent)
                        .setSmallIcon(android.R.drawable.stat_sys_download)
                        .setWhen(System.currentTimeMillis())
                        .setProgress(100, progress, false)
                        .setContentTitle("Заголовок")
                        .build());
    }
}
