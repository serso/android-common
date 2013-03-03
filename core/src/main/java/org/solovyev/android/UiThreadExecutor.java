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

import android.os.Handler;
import javax.annotation.Nonnull;
import org.solovyev.common.threads.DelayedExecutor;

import java.util.concurrent.TimeUnit;

/**
 * User: serso
 * Date: 12/1/12
 * Time: 4:11 PM
 */

/**
 * Implementation of {@link org.solovyev.common.threads.DelayedExecutor} for Android OS,
 * execution is done on the main application thread (UI thread)
 */
public class UiThreadExecutor implements DelayedExecutor {

    @Nonnull
    private final Handler uiHandler;

    public UiThreadExecutor() {
        this.uiHandler = AThreads.newUiHandler();
    }

    @Override
    public void execute(@Nonnull Runnable command, long delay, @Nonnull TimeUnit delayUnit) {
        this.uiHandler.postDelayed(command, delayUnit.toMillis(delay));
    }

    @Override
    public void execute(@Nonnull Runnable command) {
        this.uiHandler.post(command);
    }
}
