package ru.agima.mobile.loader.core;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;

import ru.agima.mobile.loader.core.Loader.Configurator;
import ru.agima.mobile.loader.service.LoaderService;
import ru.agima.mobile.loader.utils.BundleConst;
import ru.agima.mobile.loader.utils.Logger;
import ru.agima.mobile.loader.utils.Validator;

final class Core {
    private Context context;
    private String url;
    private String path;
    private DownloadReceiver receiver;
    private Loader.ReceivedConfig receivedConfig;
    private Notification notification;
    private boolean isHideDefaultNotification;
    private boolean isViewNotificationOnFinish;
    private boolean isEnableLogging;
    private boolean isImmortal;
    private boolean isParallel;
    private boolean isSkipCache;
    private boolean isSkipIfFileExist;
    private boolean isBreakNextIfError;
    private int redownloadAttemptCount;
    private long delayBetweenLoad;

    public void build(Configurator configurator, Context context) {
        this.context = context;
        url = configurator.getUrl();                                            // OK
        path = configurator.getPath();                                          // OK
        notification = configurator.getNotification();                          // OK
        receiver = configurator.getDownloadReceiver();                          // OK
        receivedConfig = configurator.getReceivedConfig();                      // OK
        isHideDefaultNotification = configurator.isHideDefaultNotification();   // НЕ РЕКОМЕНДОВАНО OK
        isViewNotificationOnFinish = configurator.isViewNotificationOnFinish(); // OK
        isEnableLogging = configurator.isEnableLogging();                       // OK
        isImmortal = configurator.isImmortal();                                 // OK
        isParallel = configurator.isParallel();
        isSkipCache = configurator.isSkipCache();                               // OK
        isSkipIfFileExist = configurator.isSkipIfFileExist();
        isBreakNextIfError = configurator.isBreakNextIfError();
        redownloadAttemptCount = configurator.getRedownloadAttemptCount();
        delayBetweenLoad = configurator.getDelayBetweenLoad();
        load();
    }

    private void load() {
        configure();
        validate();
        context.startService(getPreparedIntent());
    }

    private void configure() {
        if (isEnableLogging) {
            Logger.enableLogging();
        }
        if (receiver != null) {
            setReceivers();
            if (receiver.getReceivedFileSource() != null) {
                isSkipCache = true;
            }
        }
        if (isSkipCache) {
            path = null;
        }
    }

    private Intent getPreparedIntent() {
        return new Intent()
                .setClass(context, LoaderService.class)
                .putExtra(BundleConst.URL, url)
                .putExtra(BundleConst.PATH, path)
                .putExtra(BundleConst.IMMORTAL, isImmortal)
                .putExtra(BundleConst.DEFAULT_NOTIFICATION, isHideDefaultNotification)
                .putExtra(BundleConst.VIEW_NOTIFICATION_ON_FINISH, isViewNotificationOnFinish)
                .putExtra(BundleConst.NOTIFICATION, notification)
                .putExtra(BundleConst.RECEIVER, receiver);
    }

    private void setReceivers() {
        receiver.setReceivedFile(receivedConfig.getReceivedFile());
        receiver.setReceivedFileSource(receivedConfig.getReceivedFileSource());
        receiver.setOnStart(receivedConfig.getOnStart());
        receiver.setOnCompleted(receivedConfig.getOnCompleted());
        receiver.setOnProgress(receivedConfig.getOnProgress());
        receiver.setOnError(receivedConfig.getOnError());
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
        unsubscribe();
        context.stopService(new Intent(context, LoaderService.class));
    }

    void unsubscribe() {
        if (receiver != null) {
            receiver.unsubscribe();
        }
    }
}
