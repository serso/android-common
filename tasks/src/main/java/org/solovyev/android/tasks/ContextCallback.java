package org.solovyev.android.tasks;

import android.content.Context;

import javax.annotation.Nonnull;

/**
 * User: serso
 * Date: 4/3/13
 * Time: 10:10 PM
 */

/**
 * Callback to be used for notifying activity (or fragments or views in it) about task result.
 * After implementation of this interface see {@link Tasks#toUiThreadFutureCallback(C, ContextCallback < C ,V>)} for conversion it to
 * {@link com.google.common.util.concurrent.FutureCallback}.
 *
 * IMPLEMENTATION NOTE: please fo not implement this class as inner anonymous class of activity (fragment/view) as it will have a reference to activity (fragment/view)
 * which will cause memory leak. As a rule ALWAYS use it in the static context.
 *
 * @param <C> type of context
 * @param <V> type of result
 */
public interface ContextCallback<C extends Context, V> {

    void onSuccess(@Nonnull C context, V result);

    void onFailure(@Nonnull C context, Throwable t);
}
