package ru.agima.mobile.loader.core;

import android.app.Notification;
import android.content.Context;

import java.io.File;

import ru.agima.mobile.loader.callbacks.lifecycle.OnCompleted;
import ru.agima.mobile.loader.callbacks.lifecycle.OnCompletedNext;
import ru.agima.mobile.loader.callbacks.lifecycle.OnErrorNext;
import ru.agima.mobile.loader.callbacks.lifecycle.OnProgress;
import ru.agima.mobile.loader.callbacks.lifecycle.OnStart;
import ru.agima.mobile.loader.callbacks.lifecycle.OnStartNext;
import ru.agima.mobile.loader.callbacks.receiving.ReceivedFile;
import ru.agima.mobile.loader.callbacks.receiving.ReceivedFileSource;

public final class Loader {
    private Context context;
    private Core core;

    private Loader(Context context) {
        this.context = context;
    }

    public static Loader with(Context context) {
        return new Loader(context);
    }

    private Loader build(Configurator configurator) {
        core = new Core();
        core.build(configurator, context);
        return this;
    }

    public Configurator fromUrl(String url) {
        return new Configurator(url);
    }

    public void addInQueue() {
        core.addInQueue();
    }

    public void cancelByName(String fileName) {
        core.cancelByName(fileName);
    }

    public void cancelByUrl(String url) {
        core.cancelByUrl(url);
    }

    public void cancelAll() {
        core.cancelAll();
        context = null;
    }

    public void onDestroyCallback() {
        core.onDestroyCallback();
    }

    public final class Configurator {
        private final String url;
        private String path = Repository.DEFAULT.getPath();
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

        private Configurator(String url) {
            this.url = url;
        }

        public Configurator to(String path) {
            this.path = path;
            return this;
        }

        public Configurator to(File file) {
            this.path = file.getAbsolutePath();
            return this;
        }

        public Configurator to(Repository repository) {
            this.path = repository.getPath();
            return this;
        }

        public Configurator makeImmortal() {
            isImmortal = true;
            return this;
        }

        public Configurator delayBetweenLoad(long delayBetweenLoad) {
            this.delayBetweenLoad = delayBetweenLoad;
            return this;
        }

        public Configurator enableDefaultNotification() {
            isEnableDefaultNotification = true;
            return this;
        }

        public Configurator parallelLoad() {
            isParallel = true;
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

        public Configurator breakNextIfError() {
            isBreakNextIfError = true;
            return this;
        }

        public Configurator notification(Notification notification) {
            this.notification = notification;
            return this;
        }

        public Configurator receivedFile(ReceivedFile receivedFile) {
            this.receivedFile = receivedFile;
            return this;
        }

        public Configurator receivedFileSource(ReceivedFileSource receivedFileSource) {
            this.receivedFileSource = receivedFileSource;
            return this;
        }

        public Configurator onStart(OnStart onStart) {
            this.onStart = onStart;
            return this;
        }

        public Configurator onStartNext(OnStartNext onStartNext) {
            this.onStartNext = onStartNext;
            return this;
        }

        public Configurator onCompletedNext(OnCompletedNext onCompletedNext) {
            this.onCompletedNext = onCompletedNext;
            return this;
        }

        public Configurator onCompleted(OnCompleted onCompleted) {
            this.onCompleted = onCompleted;
            return this;
        }

        public Configurator receivedFileSource(OnProgress onProgress) {
            this.onProgress = onProgress;
            return this;
        }

        public Configurator receivedFileSource(OnErrorNext onErrorNext) {
            this.onErrorNext = onErrorNext;
            return this;
        }

        public String getUrl() {
            return url;
        }

        public String getPath() {
            return path;
        }

        public ReceivedFile getReceivedFile() {
            return receivedFile;
        }

        public ReceivedFileSource getReceivedFileSource() {
            return receivedFileSource;
        }

        public OnStart getOnStart() {
            return onStart;
        }

        public OnStartNext getOnStartNext() {
            return onStartNext;
        }

        public OnCompletedNext getOnCompletedNext() {
            return onCompletedNext;
        }

        public OnCompleted getOnCompleted() {
            return onCompleted;
        }

        public OnProgress getOnProgress() {
            return onProgress;
        }

        public OnErrorNext getOnErrorNext() {
            return onErrorNext;
        }

        public Notification getNotification() {
            return notification;
        }

        public boolean isEnableDefaultNotification() {
            return isEnableDefaultNotification;
        }

        public boolean isImmortal() {
            return isImmortal;
        }

        public boolean isParallel() {
            return isParallel;
        }

        public boolean isBreakNextIfError() {
            return isBreakNextIfError;
        }

        public int getRedownloadAttemptCount() {
            return redownloadAttemptCount;
        }

        public long getDelayBetweenLoad() {
            return delayBetweenLoad;
        }

        public boolean isSkipIfFileExist() {
            return isSkipIfFileExist;
        }

        public Loader load() {
            return Loader.this.build(this);
        }
    }
}
