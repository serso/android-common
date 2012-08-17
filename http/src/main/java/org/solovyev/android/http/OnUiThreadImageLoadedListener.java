package org.solovyev.android.http;

import android.app.Activity;
import android.graphics.Bitmap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;

/**
 * User: serso
 * Date: 8/17/12
 * Time: 12:13 PM
 */
public abstract class OnUiThreadImageLoadedListener implements OnImageLoadedListener {

    @NotNull
    private final WeakReference<Activity> activityRef;

    public OnUiThreadImageLoadedListener(@NotNull Activity activity) {
        this.activityRef = new WeakReference<Activity>(activity);
    }

    @Override
    public void onImageLoaded(@Nullable final Bitmap image) {
        final Activity activity = this.activityRef.get();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onImageLoadedOnUiThread(image);
                }
            });
        }
    }

    protected abstract void onImageLoadedOnUiThread(@Nullable Bitmap image);

    @Override
    public void setDefaultImage() {
        final Activity activity = this.activityRef.get();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setDefaultImageOnUiThread();
                }
            });
        }
    }

    protected abstract void setDefaultImageOnUiThread();
}
