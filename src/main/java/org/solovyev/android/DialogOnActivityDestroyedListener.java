package org.solovyev.android;

import android.app.Activity;
import android.app.AlertDialog;
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 5/5/12
 * Time: 9:52 PM
 */
public class DialogOnActivityDestroyedListener implements OnActivityDestroyedListener {

    @NotNull
    private AlertDialog dialog;

    public DialogOnActivityDestroyedListener(@NotNull AlertDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void onActivityDestroyed(@NotNull Activity activity) {
        dialog.dismiss();
    }
}
