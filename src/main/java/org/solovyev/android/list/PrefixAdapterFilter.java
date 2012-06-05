package org.solovyev.android.list;

import com.google.common.base.Predicate;
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 6/6/12
 * Time: 1:09 AM
 */
public class PrefixAdapterFilter<T> extends AdapterFilter<T> {

    @Override
    protected Predicate<T> getFilter(@NotNull CharSequence prefix) {
        return new PrefixFilter<T>(prefix.toString().toLowerCase());
    }

    public PrefixAdapterFilter(@NotNull Helper<T> helper) {
        super(helper);
    }
}

