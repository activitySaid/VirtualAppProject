package com.example.virtualapp.virtual;

import android.content.Context;
import com.example.virtualapp.virtual.VirtualCore;
import com.example.virtualapp.virtual.InstallResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class AppImporter {
    private final VirtualCore core = VirtualCore.get();

    public InstallResult importAppFromFile(String path, int userId) {
        return new File(path).exists() ? core.installPackage(path, userId) : InstallResult.makeFailure("File not found");
    }

    public InstallResult importAppFromUrl(String urlStr, int userId) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setUseCaches(false);
            
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                File temp = File.createTempFile("app_", ".apk");
                try (InputStream in = conn.getInputStream(); FileOutputStream out = new FileOutputStream(temp)) {
                    byte[] b = new byte[4096];
                    int len;
                    while ((len = in.read(b)) > 0) out.write(b, 0, len);
                }
                InstallResult result = core.installPackage(temp.getAbsolutePath(), userId);
                temp.delete();
                return result;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return InstallResult.makeFailure("Download failed");
    }

    public void importMultipleApps(List<String> paths, int userId) {
        for (String p : paths) {
            try { core.installPackage(p, userId); } catch (Exception e) { e.printStackTrace(); }
        }
    }
}