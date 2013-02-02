package org.solovyev.android.listeners;

import android.os.Handler;
import org.jetbrains.annotations.NotNull;
import org.solovyev.android.AndroidUtils;
import org.solovyev.common.listeners.AbstractJEventListener;
import org.solovyev.common.listeners.JEvent;

/**
 * User: serso
 * Date: 2/2/13
 * Time: 2:09 PM
 */
public abstract class AbstractUiThreadJEventListener<E extends JEvent> extends AbstractJEventListener<E> {

    @NotNull
    private final Handler uiHandler;

    protected AbstractUiThreadJEventListener(@NotNull Class<E> eventType) {
        super(eventType);
        uiHandler = AndroidUtils.newUiHandler();
    }

    @Override
    public void onEvent(@NotNull final E event) {
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
    protected abstract void handleEvent(@NotNull E event);
}
