package ru.agima.mobile.loader.core;

import android.os.Bundle;
import android.os.ResultReceiver;
import android.webkit.URLUtil;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import ru.agima.mobile.loader.core.DownloadReceiver.ReceiveCode;
import ru.agima.mobile.loader.exception.BadResponseException;
import ru.agima.mobile.loader.exception.FileIsExistException;
import ru.agima.mobile.loader.utils.BundleConst;
import ru.agima.mobile.loader.utils.Logger;

public class LoadManager {
    private ResultReceiver receiver;
    private File currentFile;
    private String fileName;
    private boolean isSkipIfFileExist;
    private boolean isAbortNextIfError;
    private int redownloadAttemptCount = 1;
    private int currentRedownloadAttempt = 1;
    private static volatile boolean isErrorPreviousDownload;

    public void loadFile(String path, String url, ResultReceiver resultReceiver) {
        loadFile(path, url, resultReceiver, true);
    }

    private synchronized void loadFile(String path, String url, ResultReceiver resultReceiver, boolean isFirstAttempt) {
        if (isAbortNextIfError && isErrorPreviousDownload) {
            Logger.debug("All next downloads were interrupted");
            return;
        }
        this.receiver = resultReceiver;
        if (isFirstAttempt) {
            sendTrail(ReceiveCode.ON_START, null);
        }
        fileName = URLUtil.guessFileName(url, null, URLConnection.guessContentTypeFromName(url));
        Logger.debug("Start downloading", fileName, "file. Attempt", currentRedownloadAttempt, "from", redownloadAttemptCount);

        try {
            tryDownloading(path, url);
        } catch (FileIsExistException e) {
            Logger.debug("File", fileName, "is exist in", path);
            sendTrail(ReceiveCode.ON_COMPLETED, null);
        } catch (InterruptedIOException e) {
            Logger.debug("All downloads were interrupted");
            return;
        } catch (Throwable throwable) {
            Logger.error("Downloading file", fileName, "is falled!");
            Logger.error(throwable.getMessage(), throwable);
            if (redownloadAttemptCount > currentRedownloadAttempt) {
                currentRedownloadAttempt++;
                loadFile(path, url, resultReceiver, false);
            } else {
                isErrorPreviousDownload = true;
                final Bundle bundle = new Bundle();
                bundle.putSerializable(BundleConst.THROWABLE, throwable);
                bundle.putString(BundleConst.FILE_NAME, fileName);
                sendTrail(ReceiveCode.ON_ERROR, bundle);
            }
            return;
        }
        Logger.debug("Download of", fileName, "file completed");
        sendTrail(ReceiveCode.ON_COMPLETED, null);
    }

    private void tryDownloading(String path, String url) throws IOException{
        if (path == null || path.isEmpty()) {
            downloading(url, null);
        } else {
            downloading(url, getOutputStream(path, fileName));
        }
    }

    private OutputStream getOutputStream(String path, String fileName) throws IOException {
        currentFile = new File(path, fileName);
        if (isSkipIfFileExist && currentFile.exists()) {
            throw new FileIsExistException("File " + currentFile.getName() + " is exist");
        }

        if (!currentFile.createNewFile() && !currentFile.exists()) {
            throw new IOException("Could not create " + currentFile.getName() + " file");
        }
        return new FileOutputStream(currentFile);
    }

    private void downloading(String url, OutputStream output) throws IOException {
        InputStream input = null;
        if (output == null) {
            output = new ByteArrayOutputStream();
        }
        try {
            final HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.connect();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new BadResponseException("Server return " + connection.getResponseCode() + " " + connection.getResponseMessage());
            }
            final int fileLength = connection.getContentLength();
            input = new BufferedInputStream(connection.getInputStream());
            final byte data[] = new byte[1024];
            long total = 0;
            int count;
            int progress;
            int lastProgress = 0;
            while ((count = input.read(data)) != -1) {
                total += count;
                progress = (int) (total * 100 / fileLength);
                if (progress - lastProgress != 0 && progress != 100) {
                    final Bundle resultData = new Bundle();
                    lastProgress = progress;
                    resultData.putInt(BundleConst.PROGRESS, progress);
                    sendTrail(ReceiveCode.ON_PROGRESS, resultData);
                }
                Logger.debug("Uploaded", total, "from", fileLength, "== progress", progress, "%");
                output.write(data, 0, count);
            }
            output.flush();
        } finally {
            closeStream(input);
            closeStream(output);
        }

        final Bundle resultData = new Bundle();
        resultData.putInt(BundleConst.PROGRESS, 100);
        sendTrail(ReceiveCode.ON_PROGRESS, resultData);

        if (output instanceof ByteArrayOutputStream) {
            Logger.debug("Sources of", fileName, "are redirected to bytes");
            resultData.putByteArray(BundleConst.BYTES, ((ByteArrayOutputStream) output).toByteArray());
            resultData.putString(BundleConst.FILE_NAME, fileName);
            sendTrail(ReceiveCode.RECEIVED_FILE_SOURCE, resultData);
        } else {
            Logger.debug("Sources of", fileName, "are redirected to file");
            resultData.putSerializable(BundleConst.FILE, currentFile);
            sendTrail(ReceiveCode.RECEIVED_FILE, resultData);
        }
    }

    private void sendTrail(ReceiveCode receiveCode, Bundle resultData) {
        if (receiver != null) {
            receiver.send(receiveCode.getCode(), resultData);
        }
    }

    private void closeStream(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public LoadManager skipIfFileExist(boolean isSkipIfFileExist) {
        this.isSkipIfFileExist = isSkipIfFileExist;
        return this;
    }

    public LoadManager abortNextIfError(boolean isAbortNextIfError) {
        this.isAbortNextIfError = isAbortNextIfError;
        return this;
    }

    public LoadManager redownloadAttemptCount(int redownloadAttemptCount) {
        if (redownloadAttemptCount > 0) {
            this.redownloadAttemptCount = redownloadAttemptCount;
        }
        return this;
    }
}
