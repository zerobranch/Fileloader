package ru.agima.mobile.loader.core;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;

import ru.agima.mobile.loader.callbacks.lifecycle.OnCompleted;
import ru.agima.mobile.loader.callbacks.lifecycle.OnCompletedNext;
import ru.agima.mobile.loader.callbacks.lifecycle.OnErrorNext;
import ru.agima.mobile.loader.callbacks.lifecycle.OnProgress;
import ru.agima.mobile.loader.callbacks.lifecycle.OnStart;
import ru.agima.mobile.loader.callbacks.lifecycle.OnStartNext;
import ru.agima.mobile.loader.callbacks.receiving.ReceivedFile;
import ru.agima.mobile.loader.callbacks.receiving.ReceivedFileSource;
import ru.agima.mobile.loader.core.Loader.Configurator;
import ru.agima.mobile.loader.service.LoaderService;
import ru.agima.mobile.loader.utils.BundleConst;
import ru.agima.mobile.loader.utils.Validator;

final class Core {
    private Context context;
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
    private boolean isHideDefaultNotification;
    private boolean isViewNotificationOnFinish;
    private boolean isImmortal;
    private boolean isParallel;
    private boolean isSkipCache;
    private boolean isSkipIfFileExist;
    private boolean isBreakNextIfError;
    private int redownloadAttemptCount;
    private long delayBetweenLoad;

    public void build(Configurator configurator, Context context) {
        this.context = context;
        destroyService();
        url = configurator.getUrl();                                            // OK
        path = configurator.getPath();                                          // OK
        receivedFile = configurator.getReceivedFile();                          // OK
        receivedFileSource = configurator.getReceivedFileSource();              // OK
        onStart = configurator.getOnStart();                                    // OK
        onStartNext = configurator.getOnStartNext();                            // OK
        onCompletedNext = configurator.getOnCompletedNext();                    // OK
        onCompleted = configurator.getOnCompleted();                            // OK
        onProgress = configurator.getOnProgress();                              // OK
        onErrorNext = configurator.getOnErrorNext();                            // OK
        notification = configurator.getNotification();                          // OK
        isHideDefaultNotification = configurator.isHideDefaultNotification();   // НЕ РЕКОМЕНДОВАНО OK
        isViewNotificationOnFinish = configurator.isHideDefaultNotification();   // OK
        isImmortal = configurator.isImmortal();                                 // OK
        isParallel = configurator.isParallel();
        isSkipCache = configurator.isSkipCache();
        isSkipIfFileExist = configurator.isSkipIfFileExist();
        isBreakNextIfError = configurator.isBreakNextIfError();
        redownloadAttemptCount = configurator.getRedownloadAttemptCount();
        delayBetweenLoad = configurator.getDelayBetweenLoad();
        load();
    }

    private void load() {
        if (isSkipCache) {
            path = null;
        }
        validate();
        final Intent intent = new Intent();
        intent.setClass(context, LoaderService.class);
        intent.putExtra(BundleConst.URL, url);
        intent.putExtra(BundleConst.PATH, path);
        intent.putExtra(BundleConst.IMMORTAL, isImmortal);
        intent.putExtra(BundleConst.DEFAULT_NOTIFICATION, isHideDefaultNotification);
        intent.putExtra(BundleConst.VIEW_NOTIFICATION_ON_FINISH, isViewNotificationOnFinish);
        intent.putExtra(BundleConst.NOTIFICATION, notification);
        intent.putExtra(BundleConst.RECEIVED_FILE, receivedFile);
        intent.putExtra(BundleConst.RECEIVED_FILE_SOURCE, receivedFileSource);
        intent.putExtra(BundleConst.ON_START, onStart);
        intent.putExtra(BundleConst.ON_START_NEXT, onStartNext);
        intent.putExtra(BundleConst.ON_COMPLETED_NEXT, onCompletedNext);
        intent.putExtra(BundleConst.ON_COMPLETED, onCompleted);
        intent.putExtra(BundleConst.ON_PROGRESS, onProgress);
        intent.putExtra(BundleConst.ON_ERROR_NEXT, onErrorNext);
        context.startService(intent);
    }

    private void validate() {
        if (path != null) {
            Validator.getNonEmptyValue(path, "Argument 'path' should not be empty");
        }
        Validator.getNonEmptyValue(url, "Argument 'url' should not be empty");
        Validator.getNonNull(context, "Context should not be null");
    }

    void addInQueue(String path, String url) {
        this.url = url;
        this.path = path;
        load();
    }

    void cancelByName(String fileName) {
      /*  Context.startService()
        Context.stopService()
        Service.stopSelf()
        Service.stopSelfResult()*/
    }

    void cancelByUrl(String url) {
        // remove first and last space
    }

    void cancelAll() {
        destroyService();
        context = null;
    }

    private void destroyService() {
        context.stopService(new Intent(context, LoaderService.class));
        onDestroyCallback();
    }

    void onDestroyCallback() {
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
