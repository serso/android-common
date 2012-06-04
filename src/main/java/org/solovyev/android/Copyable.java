package org.solovyev.android;

import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 5/28/12
 * Time: 11:21 PM
 */
public interface Copyable<C> {

    @NotNull
    C copy();
}
