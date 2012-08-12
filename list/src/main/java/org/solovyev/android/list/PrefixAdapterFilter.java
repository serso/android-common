package org.solovyev.android.list;

import org.jetbrains.annotations.NotNull;
import org.solovyev.common.IPredicate;

/**
 * User: serso
 * Date: 6/6/12
 * Time: 1:09 AM
 */
public class PrefixAdapterFilter<T> extends AdapterFilter<T> {

    @Override
    protected IPredicate<T> getFilter(@NotNull CharSequence prefix) {
        return new PrefixFilter<T>(prefix.toString().toLowerCase());
    }

    public PrefixAdapterFilter(@NotNull Helper<T> helper) {
        super(helper);
    }
}

