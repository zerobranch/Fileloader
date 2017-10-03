package ru.agima.mobile.fileloader;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

import ru.agima.mobile.loader.callbacks.receiving.ReceivedFile;
import ru.agima.mobile.loader.callbacks.receiving.ReceivedFileSource;
import ru.agima.mobile.loader.core.DownloadReceiver;
import ru.agima.mobile.loader.core.Loader;

public class MainActivity extends AppCompatActivity {
    DownloadReceiver receiver;
//    TextView textView;
    PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        textView = (TextView) findViewById(R.id.hello);
        pdfView = (PDFView) findViewById(R.id.pdfView);

        receiver = new DownloadReceiver(new Handler());

        Loader loader = Loader.with(this).fromUrl("https://kniga.biz.ua/pdf/4434-mechtat-ne_vredno-1.pdf")
                .enableLogging()
                .downloadReceiver(receiver)
                /*.onStart(new OnStart() {
                    @Override
                    public void apply() {
                        System.out.println("====> OnStart");
                    }
                })
                .onCompleted(new OnCompleted() {
                    @Override
                    public void apply() {
                        System.out.println("====> OnCompleted");
//
                    }
                })*/.receivedFile(new ReceivedFile() {
                    @Override
                    public void set(File file) {
                        System.out.println("=====> file path " + file.getAbsolutePath());
                    }
                })
                /*.onError(new OnError() {
                    @Override
                    public void apply(String fileName, Throwable throwable) {
                        throwable.printStackTrace();
                        System.out.println("====>>>> ERROR" + throwable.toString());
                    }
                })*/
                .receivedFileSource(new ReceivedFileSource() {
                    @Override
                    public void set(byte[] source, String fileName) {
//                        System.out.println("====> file name " + fileName);
                        pdfView.fromBytes(source).defaultPage(0).load();
                    }
                })
                .load();
//
//        loader.addInQueue("https://zimslifeintcs.files.wordpress.com/2011/12/head-first-java-2nd-edition.pdf");
    }


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
