package org.solovyev.common;

import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 6/9/12
 * Time: 8:40 PM
 */
public interface JCloneable<T> extends Cloneable {

    @SuppressWarnings("CloneDoesntDeclareCloneNotSupportedException")
    @NotNull
    T clone();
}
