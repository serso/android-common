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

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * User: serso
 * Date: 2/2/13
 * Time: 5:55 PM
 */
public final class AThreads {


    private AThreads() {
        throw new AssertionError();
    }

    public static void tryRunOnUiThread(@Nullable final Activity activity, @NotNull final Runnable runnable) {
        if (activity != null && !activity.isFinishing()) {
            if (isUiThread()) {
                runnable.run();
            } else {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // some time may pass and activity might be closing
                        if (!activity.isFinishing()) {
                            runnable.run();
                        }
                    }
                });
            }
        }
    }

    public static boolean isUiThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    @NotNull
    public static Handler newUiHandler() {
        if ( !isUiThread() ) {
            throw new AssertionError("Must be called on UI thread!");
        }

        return new Handler();
    }
}
