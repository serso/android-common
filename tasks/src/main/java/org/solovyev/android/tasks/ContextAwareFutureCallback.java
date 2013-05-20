package org.solovyev.android.tasks;

import android.content.Context;
import com.google.common.util.concurrent.FutureCallback;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.ref.WeakReference;

/**
 * User: serso
 * Date: 4/3/13
 * Time: 10:02 PM
 */

/**
 * Future callback with the link to the context.
 * IMPLEMENTATION NOTE: please fo not implement this class as inner anonymous class of activity (fragment/view) as it will have a reference to activity (fragment/view)
 * which will cause memory leak. As a rule ALWAYS use it in the static context and access context via {@link ContextAwareFutureCallback#getContext()} method.
 *
 * @param <V> type of the task result
 * @param <C> type of context
 * @see ContextCallback
 */
abstract class ContextAwareFutureCallback<V, C extends Context> implements FutureCallback<V> {

	@Nonnull
	private final WeakReference<C> contextRef;

	ContextAwareFutureCallback(@Nonnull C context) {
		this.contextRef = new WeakReference<C>(context);
	}

	@Nullable
	protected C getContext() {
		return contextRef.get();
	}
}
