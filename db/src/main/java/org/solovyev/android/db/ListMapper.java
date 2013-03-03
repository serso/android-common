/*
 * Copyright 2013 serso aka se.solovyev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Contact details
 *
 * Email: se.solovyev@gmail.com
 * Site:  http://se.solovyev.org
 */

package org.solovyev.android.db;

import android.database.Cursor;
import javax.annotation.Nonnull;
import org.solovyev.common.Converter;

import java.util.ArrayList;
import java.util.List;

/**
 * User: serso
 * Date: 6/3/12
 * Time: 4:43 PM
 */
public class ListMapper<T> implements Converter<Cursor, List<T>> {

    @Nonnull
    private final Converter<Cursor, ? extends T> elementMapper;

    public ListMapper(@Nonnull Converter<Cursor, ? extends T> elementMapper) {
        this.elementMapper = elementMapper;
    }

    @Nonnull
    @Override
    public List<T> convert(@Nonnull Cursor cursor) {
        final List<T> result = new ArrayList<T>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                result.add(elementMapper.convert(cursor));
                cursor.moveToNext();
            }
        }

        return result;
    }
}

