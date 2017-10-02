package ru.agima.mobile.loader.service;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class DownloadReceiver extends ResultReceiver {
    private Receiver mReceiver;

    public interface Receiver {
        void onReceiveResult(int progress);
    }

    public DownloadReceiver(Handler handler) {
        super(handler);
    }

    public void setReceiver(Receiver receiver) {
        mReceiver = receiver;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        super.onReceiveResult(resultCode, resultData);
        if (resultCode == LoaderService.class.getModifiers()) {
            int progress = resultData.getInt("progress");
            if (mReceiver != null) {
                mReceiver.onReceiveResult(progress);
            }
        }
    }
}
