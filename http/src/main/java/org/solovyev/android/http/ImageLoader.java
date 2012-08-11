package org.solovyev.android.http;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.android.core.FileCache;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader {

    private static final String TAG = ImageLoader.class.getSimpleName();

    @NotNull
    private final MemoryCache memoryCache = new MemoryCache();

    @NotNull
    private final FileCache fileCache;

    @NotNull
    private final Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());

    @NotNull
    private final ExecutorService executorService;

    public ImageLoader(@NotNull Context context, @NotNull String cacheFileName) {
        fileCache = new FileCache(context, cacheFileName);
        executorService = Executors.newFixedThreadPool(5);
    }

    public void loadImage(@NotNull String url, @NotNull ImageView imageView, @Nullable Integer defaultImageId) {
        imageViews.put(imageView, url);

        final Bitmap bitmapFromMemory = memoryCache.get(url);
        if (bitmapFromMemory != null) {
            // bitmap found in memory => set
            imageView.setImageBitmap(bitmapFromMemory);
        } else {
            // add to loading queue
            queuePhoto(url, imageView, defaultImageId);

            if (defaultImageId != null) {
                // but now just set default image
                imageView.setImageResource(defaultImageId);
            }
        }
    }

    public void loadImage(@NotNull String url) {
        final Bitmap bitmapFromMemory = memoryCache.get(url);
        if (bitmapFromMemory != null) {
            // bitmap found in memory
        } else {
            // add to loading queue
            queuePhoto(url, null, null);
        }
    }

    private void queuePhoto(@NotNull String url, @Nullable ImageView imageView, @Nullable Integer defaultImageId) {
        executorService.submit(new PhotosLoader(new PhotoToLoad(url, imageView, defaultImageId)));
    }

    @Nullable
    private Bitmap getBitmap(@NotNull String url) {
        final File cachedBitmapFile = fileCache.getFile(createFilename(url));

        // from SD cache
        Bitmap result = decodeFile(cachedBitmapFile);
        if (result == null) {
            //from web
            try {
                final URL imageUrl = new URL(url);

                final HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
                connection.setConnectTimeout(30000);
                connection.setReadTimeout(30000);
                connection.setInstanceFollowRedirects(true);


                final InputStream is = connection.getInputStream();
                OutputStream os = null;
                try {
                    os = new FileOutputStream(cachedBitmapFile);

                    Utils.copyStream(is, os);

                } finally {
                    if (os != null) {
                        os.close();
                    }
                }

                result = decodeFile(cachedBitmapFile);
            } catch (MalformedURLException e) {
                Log.e(TAG, e.getMessage(), e);
            } catch (FileNotFoundException e) {
                Log.e(TAG, e.getMessage(), e);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }

        return result;
    }

    @NotNull
    private String createFilename(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    // decodes image and scales it to reduce memory consumption
    @Nullable
    private static Bitmap decodeFile(@NotNull File file) {
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(file), null, o);

            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 70;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(file), null, o2);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    //Task for the queue
    private static class PhotoToLoad {

        @NotNull
        public final String url;

        @Nullable
        public final ImageView imageView;

        @Nullable
        private final Integer defaultImageId;

        public PhotoToLoad(@NotNull String url, @Nullable ImageView imageView, @Nullable Integer defaultImageId) {
            this.url = url;
            this.imageView = imageView;
            this.defaultImageId = defaultImageId;
        }
    }

    private class PhotosLoader implements Runnable {

        @NotNull
        private final PhotoToLoad photoToLoad;

        private PhotosLoader(@NotNull PhotoToLoad photoToLoad) {
            this.photoToLoad = photoToLoad;
        }

        @Override
        public void run() {
            if (!isAlreadyProcessed(photoToLoad)) {

                final Bitmap bmp = getBitmap(photoToLoad.url);
                if (bmp != null) {
                    memoryCache.put(photoToLoad.url, bmp);
                }

                if (!isAlreadyProcessed(photoToLoad)) {
                    final ImageView imageView = photoToLoad.imageView;
                    if (imageView != null) {
                        final BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
                        final Activity a = (Activity) imageView.getContext();
                        a.runOnUiThread(bd);
                    }
                }

            }
        }
    }

    private boolean isAlreadyProcessed(@NotNull PhotoToLoad photoToLoad) {
        if (photoToLoad.imageView != null) {
            final String url = imageViews.get(photoToLoad.imageView);
            if (url == null || !url.equals(photoToLoad.url)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    //Used to display bitmap in the UI thread
    private class BitmapDisplayer implements Runnable {

        @Nullable
        private final Bitmap bitmap;

        @NotNull
        private final PhotoToLoad photoToLoad;

        public BitmapDisplayer(@Nullable Bitmap bitmap, @NotNull PhotoToLoad photoToLoad) {
            this.bitmap = bitmap;
            this.photoToLoad = photoToLoad;
        }

        public void run() {
            if (!isAlreadyProcessed(photoToLoad)) {

                final ImageView imageView = photoToLoad.imageView;
                if (imageView != null) {
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    } else if (photoToLoad.defaultImageId != null) {
                        imageView.setImageResource(photoToLoad.defaultImageId);
                    }
                }

            }
        }
    }

    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }

}
