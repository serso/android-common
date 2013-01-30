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
 * ---------------------------------------------------------------------
 * Contact details
 *
 * Email: se.solovyev@gmail.com
 * Site:  http://se.solovyev.org
 */

package org.solovyev.android.http;

import android.graphics.Bitmap;
import android.util.Log;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class MemoryCache {

    private static final String TAG = "MemoryCache";

    // Last argument true for LRU ordering
    private Map<String, Bitmap> cache = Collections.synchronizedMap(new LinkedHashMap<String, Bitmap>(10, 1.5f, true));

    // current allocated size
    private long size = 0;

    // max memory in bytes
    private long limit = 1000000;

    public MemoryCache() {
        // use 25% of available heap size
        setLimit(Runtime.getRuntime().maxMemory() / 4);
    }

    public void setLimit(long limit) {
        this.limit = limit;
        Log.i(TAG, "MemoryCache will use up to " + this.limit / 1024. / 1024. + "MB");
    }


    public Bitmap get(@NotNull String key) {
        return cache.get(key);
    }

    public void put(@NotNull String key, @NotNull Bitmap value) {
        remove(key);

        cache.put(key, value);
        size += getSizeInBytes(value);

        checkSize();
    }

    private void remove(@NotNull String key) {
        final Bitmap value = cache.get(key);
        if (value != null) {
            size -= getSizeInBytes(value);
        }
    }

    private void checkSize() {
        Log.i(TAG, "Cache memory usage = " + size + " size = " + cache.size());
        if (size > limit) {
            Log.i(TAG, "Cache cleaning started!");

            // least recently accessed item will be the first one iterated
            for (final Iterator<Entry<String, Bitmap>> it = cache.entrySet().iterator(); it.hasNext() && size > limit; ) {
                final Entry<String, Bitmap> entry = it.next();
                size -= getSizeInBytes(entry.getValue());
                it.remove();
            }

            Log.i(TAG, "New cache size " + cache.size());
        }
    }

    public void clear() {
        cache.clear();
    }

    private static long getSizeInBytes(@NotNull Bitmap bitmap) {
        return bitmap.getRowBytes() * bitmap.getHeight();
    }
}