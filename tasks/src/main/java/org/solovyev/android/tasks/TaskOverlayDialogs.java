package org.solovyev.android.tasks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
* User: serso
* Date: 4/3/13
* Time: 11:01 PM
*/

/**
 * Class to be used in activity to dismiss all overlay dialogs
 */
public final class TaskOverlayDialogs {

    @Nonnull
    private final List<TaskOverlayDialog<?>> taskOverlayDialogs = new ArrayList<TaskOverlayDialog<?>>();

    public void addTaskOverlayDialog(@Nullable TaskOverlayDialog<?> t) {
        if (t != null) {
            taskOverlayDialogs.add(t);
        }
    }

    public void dismissAll() {
        for (TaskOverlayDialog<?> taskOverlayDialog : taskOverlayDialogs) {
            taskOverlayDialog.dismiss();
        }
        taskOverlayDialogs.clear();
    }
}
