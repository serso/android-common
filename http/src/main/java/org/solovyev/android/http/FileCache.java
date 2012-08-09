package org.solovyev.android.http;

import android.content.Context;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class FileCache {

    @NotNull
    private final File cacheDir;

    public FileCache(@NotNull Context context, @NotNull String cacheFileName) {
        // find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), cacheFileName);
        } else {
            cacheDir = context.getCacheDir();
        }

        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
    }

    @NotNull
    public File getFile(@NotNull String url) {
        try {
            final String filename = URLEncoder.encode(url, "UTF-8");

            return new File(cacheDir, filename);
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    public void clear() {
        final File[] files = cacheDir.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
    }

}