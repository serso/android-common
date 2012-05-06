package org.solovyev.android;

import android.app.Activity;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * User: serso
 * Date: 5/5/12
 * Time: 9:35 PM
 */
public class ActivityDestroyerController {

    @NotNull
    private static final ActivityDestroyerController instance = new ActivityDestroyerController();

    @NotNull
    private final Multimap<Activity, OnActivityDestroyedListener> listenersMap = ArrayListMultimap.create();

    private ActivityDestroyerController() {
    }

    @NotNull
    public static ActivityDestroyerController getInstance() {
        return instance;
    }

    public synchronized boolean put(@NotNull Activity activity, @NotNull OnActivityDestroyedListener listener) {
        return listenersMap.put(activity, listener);
    }

    public synchronized void fireActivityDestroyed(@NotNull Activity activity) {
        final Collection<OnActivityDestroyedListener> listeners = listenersMap.removeAll(activity);
        for (OnActivityDestroyedListener listener : listeners) {
            listener.onActivityDestroyed(activity);
        }
    }
}
