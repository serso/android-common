/*
 * Copyright 2013 serso aka se.solovyev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Contact details
 *
 * Email: se.solovyev@gmail.com
 * Site:  http://se.solovyev.org
 */

package org.solovyev.android.tasks;

import com.google.common.util.concurrent.FutureCallback;
import org.solovyev.tasks.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Class that holds temporary task listeners which can be removed all at once.
 * Useful for listeners from UI - listeners usually needed only while UI is shown, frequent creation/destruction needs adding/removing listners which
 * can be easily done via this class.
 */
public final class TaskListeners {

    @Nonnull
    private final Map<String, List<FutureCallback<?>>> listeners = new HashMap<String, List<FutureCallback<?>>>();

    @Nonnull
    private final TaskService taskService;

    public TaskListeners(@Nonnull TaskService taskService) {
        this.taskService = taskService;
    }

    @Nullable
    public <T> FutureCallback<T> run(@Nonnull NamedTask<T> task) {
        return tryAddListener(task.getName(), taskService.run(task));
    }

    @Nullable
    private <T> FutureCallback<T> tryAddListener(@Nonnull String taskName, @Nullable FutureCallback<T> listener) {
        if (listener != null) {
            List<FutureCallback<?>> listenersByTask = listeners.get(taskName);
            if (listenersByTask == null) {
                listenersByTask = new ArrayList<FutureCallback<?>>();
                listeners.put(taskName, listenersByTask);
            }
            listenersByTask.add(listener);
        }
        return listener;
    }

    @Nullable
    public <T> FutureCallback<T> run(@Nonnull String taskName, @Nonnull Task<T> task) {
        return tryAddListener(taskName, taskService.run(taskName, task));
    }

    public <T> void run(@Nonnull String taskName, @Nonnull Callable<T> task) {
        taskService.run(taskName, task);
    }

    @Nullable
    public <T> FutureCallback<T> run(@Nonnull String taskName, @Nonnull Callable<T> task, @Nullable FutureCallback<T> taskListener) {
        return tryAddListener(taskName, taskService.run(taskName, task, taskListener));
    }

    @Nullable
    public <T> FutureCallback<T> tryRun(@Nonnull NamedTask<T> task) throws TaskIsAlreadyRunningException {
        return tryAddListener(task.getName(), taskService.tryRun(task));
    }

    @Nullable
    public <T> FutureCallback<T> tryRun(@Nonnull String taskName, @Nonnull Task<T> task) throws TaskIsAlreadyRunningException {
        return tryAddListener(taskName, taskService.tryRun(taskName, task));
    }

    public <T> void tryRun(@Nonnull String taskName, @Nonnull Callable<T> task) throws TaskIsAlreadyRunningException {
        taskService.tryRun(taskName, task);
    }

    @Nullable
    public <T> FutureCallback<T> tryRun(@Nonnull String taskName, @Nonnull Callable<T> task, @Nullable FutureCallback<T> taskListener) throws TaskIsAlreadyRunningException {
        return tryAddListener(taskName, taskService.tryRun(taskName, task, taskListener));
    }

    @Nonnull
    public <T> FutureCallback<T> tryAddTaskListener(@Nonnull String taskName, @Nonnull FutureCallback<T> taskListener) throws NoSuchTaskException, TaskFinishedException {
        return tryAddListener(taskName, taskService.tryAddTaskListener(taskName, taskListener));
    }

    @Nullable
    public <T> FutureCallback<T> addTaskListener(@Nonnull String taskName, @Nonnull FutureCallback<T> taskListener) {
        return tryAddListener(taskName, taskService.addTaskListener(taskName, taskListener));
    }

    public void removeAllTaskListeners() {
        for (String taskName : listeners.keySet()) {
            removeAllTaskListeners(taskName);
        }
        listeners.clear();
    }

    public void removeAllTaskListeners(@Nonnull String taskName) {
        final List<FutureCallback<?>> listenersByTask = listeners.get(taskName);
        if (listenersByTask != null) {
            for (FutureCallback<?> listener : listenersByTask) {
                taskService.removeTaskListener(taskName, listener);
            }
        }
    }
}
