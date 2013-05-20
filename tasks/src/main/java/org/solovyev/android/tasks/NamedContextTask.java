package org.solovyev.android.tasks;

import android.content.Context;

import javax.annotation.Nonnull;

/**
 * User: serso
 * Date: 4/8/13
 * Time: 9:17 PM
 */
public interface NamedContextTask<C extends Context, V> extends ContextTask<C, V> {

	@Nonnull
	String getName();
}
