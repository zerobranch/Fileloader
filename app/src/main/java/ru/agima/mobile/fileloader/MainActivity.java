package ru.agima.mobile.fileloader;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import ru.agima.mobile.loader.core.Loader;
import ru.agima.mobile.loader.service.DownloadReceiver;

public class MainActivity extends AppCompatActivity {
    DownloadReceiver receiver;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.hello);

//        ActivityManager am = (ActivityManager) this
//                .getSystemService(ACTIVITY_SERVICE);
//        List<ActivityManager.RunningServiceInfo> rs = am.getRunningServices(50);
//
//        for (int i = 0; i < rs.size(); i++) {
//            ActivityManager.RunningServiceInfo rsi = rs.get(i);
//            Log.i("Service", "Process " + rsi.process + " with component "
//                    + rsi.service.getClassName());
//        }


        // Добавить onError в случае если напрмер не смог создать файл

        receiver = new DownloadReceiver(new Handler());
        receiver.setReceiver(receiverCallBack);
        Loader loader = Loader.with(this).fromUrl("https://kniga.biz.ua/pdf/4434-mechtat-ne_vredno-1.pdf")
                .makeImmortal()
                .load();
        loader.addInQueue("https://kniga.biz.ua/pdf/4434-mechtat-ne_vredno-1.pdf");
    }

    DownloadReceiver.Receiver receiverCallBack = new DownloadReceiver.Receiver() {
        @Override
        public void onReceiveResult(int progress) {
            if (progress == 100) {
                textView.setText("onFinished");
            } else {
                textView.setText(String.valueOf(progress));
            }
        }
    };


//    @Override
//    protected void onResume() {
//        super.onResume();
//        receiver = new DownloadReceiver(new Handler());
//        receiver.setReceiver(receiverCallBack);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        receiver.setReceiver(null);
//    }
}


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
