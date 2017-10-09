package ru.agima.mobile.loader.core;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import java.io.File;

import ru.agima.mobile.loader.callbacks.lifecycle.OnCompleted;
import ru.agima.mobile.loader.callbacks.lifecycle.OnError;
import ru.agima.mobile.loader.callbacks.lifecycle.OnProgress;
import ru.agima.mobile.loader.callbacks.lifecycle.OnStart;
import ru.agima.mobile.loader.callbacks.receiving.ReceivedFile;
import ru.agima.mobile.loader.callbacks.receiving.ReceivedFileSource;
import ru.agima.mobile.loader.utils.BundleConst;

final class DownloadReceiver extends ResultReceiver {
    private ReceivedFile receivedFile;
    private ReceivedFileSource receivedFileSource;
    private OnStart onStart;
    private OnCompleted onCompleted;
    private OnProgress onProgress;
    private OnError onError;
    private boolean isSubscribed;

    DownloadReceiver() {
        this(new Handler());
    }

    DownloadReceiver(Handler handler) {
        super(handler);
    }

    void unsubscribe() {
        setSubscribed(false);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        super.onReceiveResult(resultCode, resultData);
        if (!isSubscribed) {
            return;
        }

        final ReceiveCode receiveCode = ReceiveCode.get(resultCode);
        switch (receiveCode) {
            case RECEIVED_FILE:
                if (receivedFile != null) {
                    receivedFile.set((File) resultData.getSerializable(BundleConst.FILE));
                }
                break;
            case RECEIVED_FILE_SOURCE:
                if (receivedFileSource != null) {
                    receivedFileSource.set(resultData.getByteArray(BundleConst.BYTES),
                            resultData.getString(BundleConst.FILE_NAME));
                }
                break;
            case ON_START:
                if (onStart != null) {
                    onStart.apply();
                }
                break;
            case ON_COMPLETED:
                if (onCompleted != null) {
                    onCompleted.apply();
                }
                break;
            case ON_ERROR:
                if (onError != null) {
                    onError.apply(resultData.getString(BundleConst.FILE_NAME),
                            (Throwable) resultData.getSerializable(BundleConst.THROWABLE));
                }
                break;
            case ON_PROGRESS:
                if (onProgress != null) {
                    onProgress.apply(resultData.getInt(BundleConst.PROGRESS));
                }
                break;
        }
    }

    enum ReceiveCode {
        EMPTY(-1),
        RECEIVED_FILE(0),
        RECEIVED_FILE_SOURCE(1),
        ON_START(2),
        ON_COMPLETED(3),
        ON_ERROR(4),
        ON_PROGRESS(5);

        private int code;

        ReceiveCode(int code) {
            this.code = code;
        }

        private static ReceiveCode get(int code) {
            for (ReceiveCode receiveCode : ReceiveCode.values()) {
                if (receiveCode.code == code) {
                    return receiveCode;
                }
            }
            return EMPTY;
        }

        int getCode() {
            return code;
        }
    }

    void setReceivedFile(ReceivedFile receivedFile) {
        if (receivedFile != null) {
            this.receivedFile = receivedFile;
            setSubscribed(true);
        }
    }

    void setReceivedFileSource(ReceivedFileSource receivedFileSource) {
        if (receivedFileSource != null) {
            this.receivedFileSource = receivedFileSource;
            setSubscribed(true);
        }
    }

    void setOnStart(OnStart onStart) {
        if (onStart != null) {
            this.onStart = onStart;
            setSubscribed(true);
        }
    }

    void setOnCompleted(OnCompleted onCompleted) {
        if (onCompleted != null) {
            this.onCompleted = onCompleted;
            setSubscribed(true);
        }
    }

    void setOnProgress(OnProgress onProgress) {
        if (onProgress != null) {
            this.onProgress = onProgress;
            setSubscribed(true);
        }
    }

    void setOnError(OnError onError) {
        if (onError != null) {
            this.onError = onError;
            setSubscribed(true);
        }
    }

    private void setSubscribed(boolean subscribed) {
        isSubscribed = subscribed;
    }

    ReceivedFileSource getReceivedFileSource() {
        return receivedFileSource;
    }
}
