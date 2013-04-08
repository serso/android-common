package org.solovyev.android.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import org.solovyev.tasks.NoSuchTaskException;
import org.solovyev.tasks.TaskFinishedException;
import org.solovyev.tasks.TaskService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
* User: serso
* Date: 4/3/13
* Time: 11:01 PM
*/

/**
 * Task overlay dialog - masks the whole screen and prevents user to do anything until task has been finished
 *
 * @param <V> type of the task result
 */
public final class TaskOverlayDialog<V> implements ContextCallback<Activity, V> {

    @Nonnull
    private final ProgressDialog progressDialog;

    private volatile boolean finished = false;

    private TaskOverlayDialog(@Nonnull ProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
    }

    @Nonnull
    private static <V> TaskOverlayDialog<V> newInstance(@Nonnull Activity activity, int titleResId, int messageResId) {
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(titleResId);
        progressDialog.setMessage(activity.getText(messageResId));
        return new TaskOverlayDialog<V>(progressDialog);
    }

    /**
     * Method tries to attach overlay dialog to the task identified by <var>taskName</var>.
     * If task has been finished or not started - nothing is done and null is returned.
     *
     * @param taskService task service
     * @param activity activity which should be masked
     * @param taskName name of the task which result is waited
     * @param titleResId dialog title
     * @param messageResId dialog message
     *
     * @return not null dialog if task is running, null otherwise
     */
    @Nullable
    public static TaskOverlayDialog<?> attachToTask(@Nonnull TaskService taskService, @Nonnull Activity activity, @Nonnull String taskName, int titleResId, int messageResId) {
        TaskOverlayDialog<Object> taskOverlayDialog = newInstance(activity, titleResId, messageResId);
        try {
            taskService.tryAddTaskListener(taskName, Tasks.toFutureCallback(activity, taskOverlayDialog));
            // attached to task => can show dialog
            taskOverlayDialog.show();
        } catch (NoSuchTaskException e) {
            taskOverlayDialog = null;
        } catch (TaskFinishedException e) {
            taskOverlayDialog = null;
        }

        return taskOverlayDialog;
    }


    @Override
    public void onSuccess(@Nonnull Activity context, V result) {
        dismiss();
    }

    public synchronized void dismiss() {
        finished = true;
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public synchronized void show() {
        if (!finished) {
            progressDialog.show();
        }
    }

    @Override
    public void onFailure(@Nonnull Activity context, Throwable t) {
        dismiss();
    }
}
