package org.solovyev.android.list;

import android.widget.Filter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * User: serso
 * Date: 6/6/12
 * Time: 1:08 AM
 */
public abstract class AdapterFilter<T> extends Filter {

    @NotNull
    private final Helper<T> helper;

    public AdapterFilter(@NotNull Helper<T> helper) {
        this.helper = helper;
    }

    @NotNull
    @Override
    protected FilterResults performFiltering(@Nullable CharSequence prefix) {
        final FilterResults results = new FilterResults();

        List<T> allElements = helper.getAllElements();
        if (allElements == null) {
            // backup all list of elements
            synchronized (helper.getLock()) {
                allElements = new ArrayList<T>(helper.getShownElements());
                helper.setAllElements(allElements);
            }
        }

        // elements to be shown on list view
        final List<T> filteredElements;
        if (prefix == null || prefix.length() == 0) {
            // no constraint => show all elements
            synchronized (helper.getLock()) {
                filteredElements = new ArrayList<T>(allElements);
            }
        } else {
            // filter
            final org.solovyev.android.IFilter<T> filter = getFilter(prefix);
            synchronized (helper.getLock()) {

                filteredElements = new ArrayList<T>(allElements.size());
                for (T element : allElements) {
                    if ( !filter.isFiltered(element) ) {
                        filteredElements.add(element);
                    }
                }
            }
        }

        results.values = filteredElements;
        results.count = filteredElements.size();

        return results;
    }

    protected abstract org.solovyev.android.IFilter<T> getFilter(@NotNull CharSequence prefix);

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        //noinspection unchecked
        helper.setShownElements((List<T>) results.values);
        helper.notifyDataSetChanged();

    }

    public static interface Helper<T> {

        @NotNull
        Object getLock();

        @NotNull
        List<T> getShownElements();

        void setShownElements(@NotNull List<T> shownElements);

        @Nullable
        List<T> getAllElements();

        void setAllElements(@NotNull List<T> allElements);

        void notifyDataSetChanged();
    }
}