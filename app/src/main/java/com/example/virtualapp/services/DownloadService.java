package com.example.virtualapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadService extends Service {
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String url = intent.getStringExtra("download_url");
            String fileName = intent.getStringExtra("file_name");
            if (url != null && fileName != null) {
                downloadFile(url, fileName);
            }
        }
        return START_NOT_STICKY;
    }

    private void downloadFile(String urlString, String fileName) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);
            connection.setDoInput(true);
            connection.setUseCaches(false);

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                File downloadDir = new File(getExternalFilesDir(null), "downloads");
                if (!downloadDir.exists()) downloadDir.mkdirs();

                File outputFile = new File(downloadDir, fileName);
                try (InputStream in = connection.getInputStream();
                     FileOutputStream out = new FileOutputStream(outputFile)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) { return null; }
}