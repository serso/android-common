package org.solovyev.android;

import android.app.Activity;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * User: serso
 * Date: 5/5/12
 * Time: 9:35 PM
 */
public class ActivityDestroyerController {

    @NotNull
    private static final ActivityDestroyerController instance = new ActivityDestroyerController();

    // NOTE: weak hash map used to prevent memory leaks of activities
    @NotNull
    private final Map<Activity, List<OnActivityDestroyedListener>> listenersMap = new WeakHashMap<Activity, List<OnActivityDestroyedListener>>();

    private ActivityDestroyerController() {
    }

    @NotNull
    public static ActivityDestroyerController getInstance() {
        return instance;
    }

    public synchronized boolean put(@NotNull Activity activity, @NotNull OnActivityDestroyedListener listener) {
        List<OnActivityDestroyedListener> listeners = listenersMap.get(activity);
        if ( listeners == null ) {
            listeners = new ArrayList<OnActivityDestroyedListener>();
            listenersMap.put(activity,  listeners);
        }
        return listeners.add(listener);
    }

    public synchronized void fireActivityDestroyed(@NotNull Activity activity) {
        final Collection<OnActivityDestroyedListener> listeners = listenersMap.remove(activity);
        for (OnActivityDestroyedListener listener : listeners) {
            listener.onActivityDestroyed(activity);
        }
    }
}
