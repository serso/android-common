package org.solovyev.android;

import android.app.Application;
import org.jetbrains.annotations.NotNull;
import org.solovyev.common.listeners.JEvent;
import org.solovyev.common.listeners.JEventListener;
import org.solovyev.common.listeners.JEventListeners;
import org.solovyev.common.listeners.Listeners;
import org.solovyev.common.threads.DelayedExecutor;

/**
 * User: serso
 * Date: 12/1/12
 * Time: 3:58 PM
 */
public final class App {

    /*
    **********************************************************************
    *
    *                           FIELDS
    *
    **********************************************************************
    */

    @NotNull
    private static volatile Application application;

    @NotNull
    private static volatile DelayedExecutor uiThreadExecutor;

    @NotNull
    private static volatile JEventListeners<JEventListener<? extends JEvent>, JEvent> eventBus;

    private static volatile boolean initialized;

    private App() {
        throw new AssertionError();
    }

    public static void init(@NotNull Application application) {
        if (!initialized) {
            App.application = application;
            App.uiThreadExecutor = new UiThreadExecutor();
            App.eventBus = Listeners.newEventBus();

            App.initialized = true;
        }
    }

    private static void checkInit(){
        if (!initialized) {
            throw new IllegalStateException("App should be initialized!");
        }
    }

    @NotNull
    public static <A extends Application> A getApplication() {
        checkInit();
        return (A) application;
    }

    @NotNull
    public static DelayedExecutor getUiThreadExecutor() {
        checkInit();
        return uiThreadExecutor;
    }

    @NotNull
    public static JEventListeners<JEventListener<? extends JEvent>, JEvent> getEventBus() {
        checkInit();
        return eventBus;
    }
}
