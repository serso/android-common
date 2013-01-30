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
