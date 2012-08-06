package org.solovyev.android.menu;

import android.app.Activity;
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 4/23/12
 * Time: 1:56 PM
 */
public interface ActivityMenu<M, MI> {

    boolean onCreateOptionsMenu(@NotNull Activity activity, @NotNull M menu);

    boolean onPrepareOptionsMenu(@NotNull Activity activity, @NotNull M menu);

    boolean onOptionsItemSelected(@NotNull Activity activity, @NotNull MI item);
}
