/*
 * Copyright 2013 serso aka se.solovyev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Contact details
 *
 * Email: se.solovyev@gmail.com
 * Site:  http://se.solovyev.org
 */

package org.solovyev.android;

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