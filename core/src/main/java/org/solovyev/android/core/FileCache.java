package org.solovyev.android.core;

import android.content.Context;
import android.os.Build;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class FileCache {

    @NotNull
    private final File cacheDir;

    public FileCache(@NotNull Context context, @NotNull String cacheFileName) {
        // find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            cacheDir = new File(createCachePath(context), cacheFileName);
        } else {
            cacheDir = context.getCacheDir();
        }

        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
    }

    @NotNull
    private String createCachePath(@NotNull Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            return context.getExternalCacheDir().getPath();
        } else {
            return android.os.Environment.getExternalStorageDirectory().getPath() + "/Android/data/" + context.getApplicationContext().getPackageName() + "/cache";
        }
    }

    @NotNull
    public File getFile(@NotNull String filename) {
        return new File(cacheDir, filename);
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