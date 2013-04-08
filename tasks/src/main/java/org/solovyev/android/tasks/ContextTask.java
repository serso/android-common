package org.solovyev.android.tasks;

import android.content.Context;

import java.util.concurrent.Callable;

/**
 * User: serso
 * Date: 4/8/13
 * Time: 9:17 PM
 */
public interface ContextTask<C extends Context, V> extends Callable<V>, ContextCallback<C, V> {
}
