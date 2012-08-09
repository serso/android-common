package org.solovyev.android.view;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * User: serso
 * Date: 8/10/12
 * Time: 1:55 AM
 */
public class ListRange<T> implements Picker.Range {

    @NotNull
    private final List<T> elements;

    private int startPosition;

    public ListRange(@NotNull List<T> elements, @Nullable T selected) {
        this.elements = elements;
        this.startPosition = elements.indexOf(selected);
        if ( this.startPosition < 0 ) {
            this.startPosition = 0;
        }
    }

    @Override
    public int getStartPosition() {
        return this.startPosition;
    }

    @Override
    public int getCount() {
        return this.elements.size();
    }

    @NotNull
    @Override
    public String getStringValueAt(int position) {
        return this.elements.get(position).toString();
    }

    @NotNull
    @Override
    public Object getValueAt(int position) {
        return this.elements.get(position);
    }
}
