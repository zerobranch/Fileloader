package ru.agima.mobile.loader.core;

import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.webkit.URLUtil;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import ru.agima.mobile.loader.core.DownloadReceiver.ReceiveCode;
import ru.agima.mobile.loader.utils.BundleConst;
import ru.agima.mobile.loader.utils.Logger;

public class LoadManager {
    private ResultReceiver receiver;
    private File currentFile;
    private String fileName;

    public synchronized void loadFile(String path, String url, ResultReceiver resultReceiver) {
        this.receiver = resultReceiver;
        sendTrail(ReceiveCode.ON_START.getCode(), null);
        fileName = URLUtil.guessFileName(url, null, URLConnection.guessContentTypeFromName(url));
        Logger.info("Start downloading", fileName, "file");

        try {
            if (path == null || path.isEmpty()) {
                downloading(url, null);
            } else {
                downloading(url, getOutputStream(path, fileName));
            }
        } catch (Throwable throwable) {
            Logger.debug(throwable.getMessage(), throwable);
            final Bundle bundle = new Bundle();
            bundle.putSerializable(BundleConst.THROWABLE, throwable);
            bundle.putString(BundleConst.FILE_NAME, fileName);
            sendTrail(ReceiveCode.ON_ERROR.getCode(), bundle);
            return;
        }
        Logger.info("Download of", fileName, "file completed");
        sendTrail(ReceiveCode.ON_COMPLETED.getCode(), null);
    }

    private OutputStream getOutputStream(String path, String fileName) throws IOException {
        currentFile = new File(path, fileName);
        if (!currentFile.createNewFile() && !currentFile.exists()) {
            throw new IOException("Could not create " + currentFile.getName() + " file");
        }
        return new FileOutputStream(currentFile);
    }

    private void downloading(String url, @Nullable OutputStream output) throws IOException {
        InputStream input = null;
        if (output == null) {
            output = new ByteArrayOutputStream();
        }
        try {
            final URLConnection connection = new URL(url).openConnection();
            connection.connect();
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
                    sendTrail(ReceiveCode.ON_PROGRESS.getCode(), resultData);
                }
                Logger.info("Uploaded", total, "from", fileLength, "== progress", progress, "%");
                output.write(data, 0, count);
            }
            output.flush();
        } finally {
            closeStream(input);
            closeStream(output);
        }

        final Bundle resultData = new Bundle();
        resultData.putInt(BundleConst.PROGRESS, 100);
        sendTrail(ReceiveCode.ON_PROGRESS.getCode(), resultData);

        if (output instanceof ByteArrayOutputStream) {
            Logger.info("Sources of", fileName, "are redirected to bytes");
            resultData.putByteArray(BundleConst.BYTES, ((ByteArrayOutputStream) output).toByteArray());
            resultData.putString(BundleConst.FILE_NAME, fileName);
            sendTrail(ReceiveCode.RECEIVED_FILE_SOURCE.getCode(), resultData);
        } else {
            Logger.info("Sources of", fileName, "are redirected to file");
            resultData.putSerializable(BundleConst.FILE, currentFile);
            sendTrail(ReceiveCode.RECEIVED_FILE.getCode(), resultData);
        }
    }

    private void sendTrail(int resultCode, Bundle resultData) {
        if (receiver != null) {
            receiver.send(resultCode, resultData);
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
}
