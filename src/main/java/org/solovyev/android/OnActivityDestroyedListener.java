package org.solovyev.android;

import android.app.Activity;
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 5/5/12
 * Time: 9:36 PM
 */
public interface OnActivityDestroyedListener {

    void onActivityDestroyed(@NotNull Activity activity);
}
