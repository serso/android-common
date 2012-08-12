package org.solovyev.android.db;

import android.database.Cursor;
import org.jetbrains.annotations.NotNull;
import org.solovyev.common.Converter;

import java.util.ArrayList;
import java.util.List;

/**
 * User: serso
 * Date: 6/3/12
 * Time: 4:43 PM
 */
public class ListMapper<T> implements Converter<Cursor, List<T>> {

    @NotNull
    private final Converter<Cursor, ? extends T> elementMapper;

    public ListMapper(@NotNull Converter<Cursor, ? extends T> elementMapper) {
        this.elementMapper = elementMapper;
    }

    @NotNull
    @Override
    public List<T> convert(@NotNull Cursor cursor) {
        final List<T> result = new ArrayList<T>();

        if (cursor.getCount() > 0) {
            while (!cursor.isLast()) {
                cursor.moveToNext();
                result.add(elementMapper.convert(cursor));
            }
        }

        return result;
    }
}

