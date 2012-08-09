package org.solovyev.android;

import org.jetbrains.annotations.Nullable;

/**
 * User: serso
 * Date: 8/7/12
 * Time: 6:26 PM
 */
public interface IFilter<T> {

    boolean isFiltered(@Nullable T t);
}
