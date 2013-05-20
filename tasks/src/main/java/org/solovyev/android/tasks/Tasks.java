package org.solovyev.android.tasks;

import android.app.Activity;
import android.content.Context;
import com.google.common.util.concurrent.FutureCallback;
import org.solovyev.android.Threads;
import org.solovyev.tasks.NamedTask;
import org.solovyev.tasks.Task;

import javax.annotation.Nonnull;

/**
 * User: serso
 * Date: 4/8/13
 * Time: 9:50 PM
 */

/**
 * Android tasks
 */
public final class Tasks extends org.solovyev.tasks.Tasks {

	private Tasks() {
		super();
	}

	/**
	 * The only difference from {@link Tasks#toFutureCallback(A, ContextCallback <A,V>)} is that all {@link ContextCallback}
	 * method calls will be done on UI thread (main application thread)
	 *
	 * @see Tasks#toFutureCallback(A, ContextCallback <A,V>)
	 */
	@Nonnull
	public static <A extends Activity, V> FutureCallback<V> toUiThreadFutureCallback(@Nonnull A activity, @Nonnull ContextCallback<A, V> callback) {
		return FutureCallbackAdapter.newUiThreadAdapter(activity, callback);
	}

	/**
	 * Method convert specified context <var>callback</var> to {@link com.google.common.util.concurrent.FutureCallback}.
	 *
	 * @param context  context to be used in {@link ContextCallback} methods
	 * @param callback context callback
	 * @param <C>      type of context
	 * @param <V>      type of result
	 * @return {@link com.google.common.util.concurrent.FutureCallback} wrapper for specified <var>callback</var>
	 */
	@Nonnull
	public static <C extends Context, V> FutureCallback<V> toFutureCallback(@Nonnull C context, @Nonnull ContextCallback<C, V> callback) {
		return FutureCallbackAdapter.newAdapter(context, callback);
	}

	/**
	 * Method converts specified context <var>task</var> to {@link Task}
	 *
	 * @param context context to be used in {@link ContextCallback} methods
	 * @param task    context task
	 * @param <C>     type of context
	 * @param <V>     type of result
	 * @return {@link Task} wrapper for specified <var>task</var>
	 */
	@Nonnull
	public static <C extends Context, V> Task<V> toTask(@Nonnull C context, @Nonnull ContextTask<C, V> task) {
		return TaskAdapter.newAdapter(context, task);
	}

	@Nonnull
	public static <C extends Context, V> NamedTask<V> toTask(@Nonnull C context, @Nonnull NamedContextTask<C, V> task) {
		return NamedTaskAdapter.newAdapter(context, task);
	}

	/**
	 * The only difference from {@link Tasks#toTask(C, ContextTask<C,V>)} is that all {@link ContextCallback}
	 * method calls will be done on UI thread (main application thread)
	 *
	 * @see Tasks#toTask(C, ContextTask<C,V>)
	 */
	@Nonnull
	public static <A extends Activity, V> Task<V> toUiThreadTask(@Nonnull A activity, @Nonnull ContextTask<A, V> task) {
		return TaskAdapter.newUiThreadTaskAdapter(activity, task);
	}

	@Nonnull
	public static <A extends Activity, V> NamedTask<V> toUiThreadTask(@Nonnull A activity, @Nonnull NamedContextTask<A, V> task) {
		return NamedTaskAdapter.newUiThreadAdapter(activity, task);
	}

    /*
	**********************************************************************
    *
    *                           STATIC
    *
    **********************************************************************
    */

	/**
	 * {@link ContextCallback} to {@link com.google.common.util.concurrent.FutureCallback} adapter
	 */
	private static final class FutureCallbackAdapter<C extends Context, V> extends ContextAwareFutureCallback<V, C> {

		@Nonnull
		private final ContextCallback<C, V> callback;

		private final boolean onUiThread;

		private FutureCallbackAdapter(@Nonnull C context, @Nonnull ContextCallback<C, V> callback, boolean onUiThread) {
			super(context);
			this.callback = callback;
			this.onUiThread = onUiThread;
		}

		private static <A extends Activity, V> FutureCallbackAdapter<A, V> newUiThreadAdapter(@Nonnull A activity, @Nonnull ContextCallback<A, V> callback) {
			return new FutureCallbackAdapter<A, V>(activity, callback, true);
		}

		private static <C extends Context, V> FutureCallbackAdapter<C, V> newAdapter(@Nonnull C context, @Nonnull ContextCallback<C, V> callback) {
			return new FutureCallbackAdapter<C, V>(context, callback, false);
		}

		@Override
		public void onSuccess(final V result) {
			final C context = getContext();
			if (context != null) {
				if (onUiThread) {
					final Activity activity = (Activity) context;
					Threads.tryRunOnUiThread(activity, new Runnable() {
						@Override
						public void run() {
							callback.onSuccess(context, result);
						}
					});
				} else {
					callback.onSuccess(context, result);
				}
			}
		}

		@Override
		public void onFailure(final Throwable e) {
			final C context = getContext();
			if (context != null) {
				if (onUiThread) {
					Threads.tryRunOnUiThread((Activity) context, new Runnable() {
						@Override
						public void run() {
							callback.onFailure(context, e);
						}
					});
				} else {
					callback.onFailure(context, e);
				}
			}
		}
	}

	/**
	 * {@link ContextTask} to {@link Task} adapter
	 */
	private static final class TaskAdapter<C extends Context, V> implements Task<V> {

		@Nonnull
		private final ContextTask<C, V> task;

		@Nonnull
		private final FutureCallback<V> callback;

		private TaskAdapter(@Nonnull ContextTask<C, V> task, @Nonnull FutureCallback<V> callback) {
			this.task = task;
			this.callback = callback;
		}

		@Nonnull
		private static <A extends Activity, V> Task<V> newUiThreadTaskAdapter(@Nonnull A activity, @Nonnull ContextTask<A, V> task) {
			return new TaskAdapter<A, V>(task, toUiThreadFutureCallback(activity, task));
		}

		@Nonnull
		private static <C extends Context, V> Task<V> newAdapter(@Nonnull C context, @Nonnull ContextTask<C, V> task) {
			return new TaskAdapter<C, V>(task, toFutureCallback(context, task));
		}

		@Override
		public V call() throws Exception {
			return task.call();
		}

		@Override
		public void onSuccess(V result) {
			callback.onSuccess(result);
		}

		@Override
		public void onFailure(Throwable t) {
			callback.onFailure(t);
		}
	}

	private static final class NamedTaskAdapter<C extends Context, V> implements NamedTask<V> {

		@Nonnull
		private final NamedContextTask<C, V> namedTask;

		@Nonnull
		private final Task<V> task;

		private NamedTaskAdapter(@Nonnull NamedContextTask<C, V> namedTask, @Nonnull Task<V> task) {
			this.task = task;
			this.namedTask = namedTask;
		}

		@Nonnull
		private static <C extends Context, V> NamedTaskAdapter<C, V> newAdapter(@Nonnull C context, @Nonnull NamedContextTask<C, V> namedTask) {
			return new NamedTaskAdapter<C, V>(namedTask, TaskAdapter.newAdapter(context, namedTask));
		}

		@Nonnull
		private static <A extends Activity, V> NamedTaskAdapter<A, V> newUiThreadAdapter(@Nonnull A activity, @Nonnull NamedContextTask<A, V> namedTask) {
			return new NamedTaskAdapter<A, V>(namedTask, TaskAdapter.newUiThreadTaskAdapter(activity, namedTask));
		}

		@Nonnull
		@Override
		public String getName() {
			return namedTask.getName();
		}

		@Override
		public V call() throws Exception {
			return namedTask.call();
		}

		@Override
		public void onSuccess(V result) {
			task.onSuccess(result);
		}

		@Override
		public void onFailure(Throwable t) {
			task.onFailure(t);
		}
	}

}
