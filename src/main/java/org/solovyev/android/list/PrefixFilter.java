package org.solovyev.android.list;

import com.google.common.base.Predicate;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

/**
 * User: serso
 * Date: 6/6/12
 * Time: 1:09 AM
 */
public class PrefixFilter<T> implements Predicate<T> {

    @NotNull
    private String prefix;

    public PrefixFilter(@NotNull String prefix) {
        this.prefix = prefix;
    }

    @Override
    public boolean apply(@Nullable T input) {
        if (input != null) {
            final String valueText = input.toString().toLowerCase();

            // First match against the whole, non-splitted value
            if (valueText.startsWith(prefix)) {
                return true;
            } else {
                final String[] words = valueText.split(" ");

                for (String word : words) {
                    if (word.startsWith(prefix)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (o == this) {
            return true;
        }

        if (o instanceof PrefixFilter) {
            final PrefixFilter that = (PrefixFilter) o;
            if (this.prefix.equals(that.prefix)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        return prefix.hashCode();
    }
}