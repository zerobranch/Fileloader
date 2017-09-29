package ru.agima.mobile.fileloader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.TextView;

import ru.agima.mobile.loader.service.DownloadReceiver;
import ru.agima.mobile.loader.service.NewIntentService;

public class MainActivity extends AppCompatActivity {
    DownloadReceiver receiver;
    TextView textView;
    TextView old;
    private SharedPreferences sharedPreferences;
    AppCompatButton appCompatButton;

    // Проверить с RemoveTask
int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.hello);
        old = (TextView) findViewById(R.id.old);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        appCompatButton = (AppCompatButton) findViewById(R.id.button);
        appCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                old.setText(i++ + "");
            }
        });
        String oldtext = sharedPreferences.getString("FINISH", "default");
        old.setText(oldtext);

        receiver = new DownloadReceiver(new Handler());
        receiver.setReceiver(receiverCallBack);

        Intent intent = new Intent(this, NewIntentService.class);
        intent.putExtra("URL", "https://kniga.biz.ua/pdf/4434-mechtat-ne_vredno-1.pdf");
        intent.putExtra("PATH", getCacheDir().getAbsolutePath());
        intent.putExtra(NewIntentService.RECEIVER, receiver);
        startService(intent);
    }

    DownloadReceiver.Receiver receiverCallBack = new DownloadReceiver.Receiver() {
        @Override
        public void onReceiveResult(int progress) {
            if (progress == 100) {
                textView.setText("onFinished");
                sharedPreferences.edit().putString("FINISH", "true").apply();
            } else {
                textView.setText(progress + "");
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
