package org.solovyev.android.samples.tasks;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import org.solovyev.android.App;
import org.solovyev.android.samples.Locator;
import org.solovyev.android.samples.R;
import org.solovyev.android.tasks.NamedContextTask;
import org.solovyev.android.tasks.TaskOverlayDialog;
import org.solovyev.android.tasks.TaskOverlayDialogs;
import org.solovyev.android.tasks.Tasks;
import org.solovyev.tasks.TaskService;

import javax.annotation.Nonnull;

/**
 * User: serso
 * Date: 4/8/13
 * Time: 10:35 PM
 */
public class SamplesTaskActivity extends Activity {

    public static final String SLEEP_TASK_NAME = "sleep-task";

    @Nonnull
    private final TaskOverlayDialogs taskOverlayDialogs = new TaskOverlayDialogs();

    // minor flaw - even if we use WeakReference there is no guarantee that activity is garbage collected before call of org.solovyev.android.samples.tasks.SamplesTaskActivity.SleepTask.onSuccess method =>
    // we need to check if activity has been destroyed manually
    private volatile boolean destroyed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.acl_tasks_layout);

        final View startTaskButton = findViewById(R.id.start_task_button);
        startTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // here we start the task
                startTask();
            }
        });

        // don't forget to reattach result listener and dialogs
        final TaskService taskService = ((Locator) App.getLocator()).getTaskService();
        // note: here i use SleepTask as a callback but you can easily use simple callback (decouple callable and result callback)
        taskService.addTaskListener(SLEEP_TASK_NAME, Tasks.toUiThreadFutureCallback(this, new SleepTask()));
        tryShowDialog();

    }

    private void startTask() {
        // start the task
        final TaskService taskService = ((Locator) App.getLocator()).getTaskService();
        taskService.run(Tasks.toUiThreadTask(this, new SleepTask()));
        // attach dialog
        tryShowDialog();
    }

    private void tryShowDialog() {
        final TaskService taskService = ((Locator) App.getLocator()).getTaskService();
        taskOverlayDialogs.addTaskOverlayDialog(TaskOverlayDialog.attachToTask(taskService, this, SLEEP_TASK_NAME, R.string.acl_sleeping_title, R.string.acl_sleeping_message));
    }

    @Override
    protected void onDestroy() {
        // we must dismiss all dialogs to prevent memory leak
        taskOverlayDialogs.dismissAll();
        destroyed = true;
        super.onDestroy();
    }

    // NOTE: static class
    private static final class SleepTask implements NamedContextTask<SamplesTaskActivity, Integer> {

        @Nonnull
        @Override
        public String getName() {
            return SLEEP_TASK_NAME;
        }

        @Override
        public Integer call() throws Exception {
            Thread.sleep(10000);
            return 42;
        }

        @Override
        public void onSuccess(@Nonnull SamplesTaskActivity activity, Integer result) {
            if (!activity.destroyed) {
                Log.d(SLEEP_TASK_NAME, "OnSuccess, activity: " + activity);
                final Button startTaskButton = (Button) activity.findViewById(R.id.start_task_button);
                startTaskButton.setText(R.string.acl_start_task_again);
            }
        }

        @Override
        public void onFailure(@Nonnull SamplesTaskActivity activity, Throwable t) {
            if (!activity.destroyed) {
                Toast.makeText(activity, "Error: " + t, Toast.LENGTH_LONG).show();
            }
        }
    }
}