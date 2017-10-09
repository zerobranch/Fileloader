package ru.agima.mobile.loader.core;

import android.app.Notification;
import android.content.Context;

import java.io.File;
import java.util.ArrayList;

import ru.agima.mobile.loader.callbacks.lifecycle.OnCompleted;
import ru.agima.mobile.loader.callbacks.lifecycle.OnError;
import ru.agima.mobile.loader.callbacks.lifecycle.OnProgress;
import ru.agima.mobile.loader.callbacks.lifecycle.OnStart;
import ru.agima.mobile.loader.callbacks.receiving.ReceivedFile;
import ru.agima.mobile.loader.callbacks.receiving.ReceivedFileSource;
import ru.agima.mobile.loader.utils.Validator;

public final class Loader {
    private final String DEFAULT_PATH;
    private Context context;
    private Core core;
    private Configurator configurator;

    private Loader(Context context) {
        this.context = context;
        Validator.getNonNull(context, "Context should not be null");
        DEFAULT_PATH = context.getCacheDir().getAbsolutePath();
    }

    public static Loader with(Context context) {
        return new Loader(context);
    }

    private Loader build(Configurator configurator) {
        core = new Core();
        core.build(configurator, context);
        return this;
    }

    public Configurator addInQueue(String url) {
        configurator = new Configurator(url);
        return configurator;
    }

    public void cancel() {
        core.cancel();
        context = null;
    }

    public void unsubscribe() {
        core.unsubscribe();
    }

    public final class ReceivedConfig {
        private ReceivedFile receivedFile;
        private ReceivedFileSource receivedFileSource;
        private OnStart onStart;
        private OnCompleted onCompleted;
        private OnProgress onProgress;
        private OnError onError;

        public ReceivedConfig receivedFile(ReceivedFile receivedFile) {
            this.receivedFile = receivedFile;
            return this;
        }

        public ReceivedConfig receivedFileSource(ReceivedFileSource receivedFileSource) {
            this.receivedFileSource = receivedFileSource;
            return this;
        }

        public ReceivedConfig onStart(OnStart onStart) {
            this.onStart = onStart;
            return this;
        }

        public ReceivedConfig onCompleted(OnCompleted onCompleted) {
            this.onCompleted = onCompleted;
            return this;
        }

        public ReceivedConfig onProgress(OnProgress onProgress) {
            this.onProgress = onProgress;
            return this;
        }

        public ReceivedConfig onError(OnError onError) {
            this.onError = onError;
            return this;
        }

        public Loader load() {
            return configurator.load();
        }

        ReceivedFile getReceivedFile() {
            return receivedFile;
        }

        ReceivedFileSource getReceivedFileSource() {
            return receivedFileSource;
        }

        OnStart getOnStart() {
            return onStart;
        }

        OnCompleted getOnCompleted() {
            return onCompleted;
        }

        OnProgress getOnProgress() {
            return onProgress;
        }

        OnError getOnError() {
            return onError;
        }
    }

    public final class Configurator {
        private final ArrayList<String> urls;
        private String path = DEFAULT_PATH;
        private Notification notification;
        private DownloadReceiver downloadReceiver;
        private ReceivedConfig receivedConfig;
        private boolean isHideDefaultNotification;
        private boolean isEnableLogging;
        private boolean isViewNotificationOnFinish;
        private boolean isImmortal;
        private boolean isSkipCache;
        private boolean isSkipIfFileExist;
        private boolean isAbortNextIfError;
        private int redownloadAttemptCount;

        private Configurator(String url) {
            urls = new ArrayList<>();
            addInQueue(url);
        }

        public Configurator to(String path) {
            this.path = path;
            return this;
        }

        public Configurator to(File file) {
            this.path = file.getAbsolutePath();
            return this;
        }

        public Configurator skipCache() {
            this.isSkipCache = true;
            return this;
        }

        public Configurator enableLogging() {
            this.isEnableLogging = true;
            return this;
        }

        public Configurator makeImmortal() {
            isImmortal = true;
            return this;
        }

        @Deprecated
        public Configurator hideDefaultNotification() {
            isHideDefaultNotification = true;
            return this;
        }

        public Configurator viewNotificationOnFinish() {
            this.isViewNotificationOnFinish = true;
            return this;
        }

        public Configurator skipIfFileExist() {
            isSkipIfFileExist = true;
            return this;
        }

        public Configurator redownloadAttemptCount(int redownloadAttemptCount) {
            this.redownloadAttemptCount = redownloadAttemptCount;
            return this;
        }

        public Configurator abortNextIfError() {
            isAbortNextIfError = true;
            return this;
        }

        public Configurator notification(Notification notification) {
            this.notification = notification;
            return this;
        }

        public Configurator addInQueue(String url) {
            urls.add(url.trim());
            return this;
        }

        public ReceivedConfig downloadReceiver(DownloadReceiver downloadReceiver) {
            this.downloadReceiver = downloadReceiver;
            receivedConfig = new ReceivedConfig();
            return receivedConfig;
        }

        DownloadReceiver getDownloadReceiver() {
            return downloadReceiver;
        }

        ReceivedConfig getReceivedConfig() {
            return receivedConfig;
        }

        ArrayList<String> getUrls() {
            return urls;
        }

        String getPath() {
            return path;
        }

        Notification getNotification() {
            return notification;
        }

        boolean isHideDefaultNotification() {
            return isHideDefaultNotification;
        }

        boolean isViewNotificationOnFinish() {
            return isViewNotificationOnFinish;
        }

        boolean isImmortal() {
            return isImmortal;
        }

        boolean isSkipCache() {
            return isSkipCache;
        }

        boolean isEnableLogging() {
            return isEnableLogging;
        }

        boolean isAbortNextIfError() {
            return isAbortNextIfError;
        }

        int getRedownloadAttemptCount() {
            return redownloadAttemptCount;
        }

        boolean isSkipIfFileExist() {
            return isSkipIfFileExist;
        }

        public Loader load() {
            return Loader.this.build(this);
        }
    }
}
