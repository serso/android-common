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

package org.solovyev.android.listeners;

import android.os.Handler;
import javax.annotation.Nonnull;
import org.solovyev.android.AThreads;
import org.solovyev.common.listeners.AbstractJEventListener;
import org.solovyev.common.listeners.JEvent;

/**
 * User: serso
 * Date: 2/2/13
 * Time: 2:09 PM
 */
public abstract class AbstractUiThreadJEventListener<E extends JEvent> extends AbstractJEventListener<E> {

    @Nonnull
    private final Handler uiHandler;

    protected AbstractUiThreadJEventListener(@Nonnull Class<E> eventType) {
        super(eventType);
        uiHandler = AThreads.newUiHandler();
    }

    @Override
    public void onEvent(@Nonnull final E event) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                handleEvent(event);
            }
        });
    }

    /**
     * Called on UI thread
     * @param event event to be processed
     */
    protected abstract void handleEvent(@Nonnull E event);
}
