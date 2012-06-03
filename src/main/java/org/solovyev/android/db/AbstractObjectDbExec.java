package org.solovyev.android.db;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * User: serso
 * Date: 4/29/12
 * Time: 1:09 PM
 */
public abstract class AbstractObjectDbExec<T> implements DbExec {

    @Nullable
    private T object;

    protected AbstractObjectDbExec(@Nullable T object) {
        this.object = object;
    }

    @Nullable
    protected T getObject() {
        return object;
    }

    @NotNull
    protected T getNotNullObject() {
        assert object != null;
        return object;
    }
}
