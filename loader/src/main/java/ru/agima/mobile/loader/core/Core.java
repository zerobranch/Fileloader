package ru.agima.mobile.loader.core;

import android.app.Notification;
import android.content.Context;

import ru.agima.mobile.loader.callbacks.lifecycle.OnCompleted;
import ru.agima.mobile.loader.callbacks.lifecycle.OnCompletedNext;
import ru.agima.mobile.loader.callbacks.lifecycle.OnErrorNext;
import ru.agima.mobile.loader.callbacks.lifecycle.OnProgress;
import ru.agima.mobile.loader.callbacks.lifecycle.OnStart;
import ru.agima.mobile.loader.callbacks.lifecycle.OnStartNext;
import ru.agima.mobile.loader.callbacks.receiving.ReceivedFile;
import ru.agima.mobile.loader.callbacks.receiving.ReceivedFileSource;
import ru.agima.mobile.loader.core.Loader.Configurator;

public final class Core {
    private String url;
    private String path;
    private ReceivedFile receivedFile;
    private ReceivedFileSource receivedFileSource;
    private OnStart onStart;
    private OnStartNext onStartNext;
    private OnCompletedNext onCompletedNext;
    private OnCompleted onCompleted;
    private OnProgress onProgress;
    private OnErrorNext onErrorNext;
    private Notification notification;
    private boolean isEnableDefaultNotification;
    private boolean isImmortal;
    private boolean isParallel;
    private boolean isSkipIfFileExist;
    private boolean isBreakNextIfError;
    private int redownloadAttemptCount;
    private long delayBetweenLoad;

    public void build(Configurator configurator, Context context) {
        cancelAll();
        url = configurator.getUrl();
        path = configurator.getPath();
        receivedFile = configurator.getReceivedFile();
        receivedFileSource = configurator.getReceivedFileSource();
        onStart = configurator.getOnStart();
        onStartNext = configurator.getOnStartNext();
        onCompletedNext = configurator.getOnCompletedNext();
        onCompleted = configurator.getOnCompleted();
        onProgress = configurator.getOnProgress();
        onErrorNext = configurator.getOnErrorNext();
        notification = configurator.getNotification();
        isEnableDefaultNotification = configurator.isEnableDefaultNotification();
        isImmortal = configurator.isImmortal();
        isParallel = configurator.isParallel();
        isSkipIfFileExist = configurator.isSkipIfFileExist();
        isBreakNextIfError = configurator.isBreakNextIfError();
        redownloadAttemptCount = configurator.getRedownloadAttemptCount();
        delayBetweenLoad = configurator.getDelayBetweenLoad();
        load(context);
    }

    private void load(Context context) {

    }

    public void addInQueue() {

    }

    public void cancelByName(String fileName) {

    }

    public void cancelByUrl(String url) {

    }

    public void cancelAll() {

    }

    public void onDestroyCallback() {
        receivedFile = null;
        receivedFileSource = null;
        onStart = null;
        onStartNext = null;
        onCompletedNext = null;
        onCompleted = null;
        onProgress = null;
        onErrorNext = null;
    }
}
