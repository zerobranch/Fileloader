package ru.agima.mobile.loader.core;

import android.os.ResultReceiver;
import android.support.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class LoadManager {

    public static void loadFile(String path, String url) {
        loadFile(path, url, null);
    }

    public static synchronized void loadFile(String path, String url, ResultReceiver resultReceiver) {
        if (path == null || path.isEmpty()) {
            downloading(url, null, resultReceiver);
        } else {
            downloading(url, getOutputStream(path), resultReceiver);
        }
    }

    private static OutputStream getOutputStream(String path) {
        File currentFile;
        currentFile = new File(path, "myFile_end.pdf");
        try {
            if (!currentFile.createNewFile() && !currentFile.exists()) {
                throw new IOException("Could not create " + currentFile.getName() + " file");
            }
            return new FileOutputStream(currentFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void downloading(String url, @Nullable OutputStream output, ResultReceiver resultReceiver) {
        InputStream input = null;

        try {
            final URLConnection connection = new URL(url).openConnection();
            connection.connect();
            final int fileLength = connection.getContentLength();
            input = new BufferedInputStream(connection.getInputStream());
            final byte data[] = new byte[1024];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
//                Bundle resultData = new Bundle();
//                resultData.putInt("progress" ,(int) (total * 100 / fileLength));
//                receiver.send(UPDATE_PROGRESS, resultData);
                System.out.println("--- " + (int) (total * 100 / fileLength));
                Thread.sleep(10);

                if (output != null) output.write(data, 0, count);
            }

            if (output != null) output.flush();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            closeStream(input);
            closeStream(output);
        }

//        Bundle resultData = new Bundle();
//        resultData.putInt("progress" ,100);
//        receiver.send(UPDATE_PROGRESS, resultData);
    }

    private static void closeStream(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
