package ru.agima.mobile.fileloader;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import ru.agima.mobile.loader.core.DownloadReceiver;
import ru.agima.mobile.loader.core.Loader;

public class MainActivity extends AppCompatActivity {
    DownloadReceiver receiver;
//    TextView textView;
//    PDFView pdfView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        textView = (TextView) findViewById(R.id.hello);
        button = (Button) findViewById(R.id.button);
//        pdfView = (PDFView) findViewById(R.id.pdfView);

        receiver = new DownloadReceiver(new Handler());

        Loader loader = Loader.with(this).fromUrl("https://kniga.biz.ua/pdf/4434-mechtat-ne_vredno-1.pdf")
                .load();
        loader.addInQueue("https://zimslifeintcs.files.wordpress.com/2011/12/head-first-java-2nd-edition.pdf");
        loader.cancel();
    }
}