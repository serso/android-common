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

package org.solovyev.android.prefs;

import android.content.SharedPreferences;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.common.text.ListMapper;
import org.solovyev.common.text.Mapper;
import org.solovyev.common.text.StringMapper;

import java.util.Collection;
import java.util.List;

public class CollectionToStringPreference<C extends Collection<T>, T> extends AbstractPreference<C> {

    /*
    **********************************************************************
    *
    *                           FIELDS
    *
    **********************************************************************
    */
    @NotNull
    private final Mapper<C> mapper;

    /*
    **********************************************************************
    *
    *                           CONSTRUCTORS
    *
    **********************************************************************
    */

    private CollectionToStringPreference(@NotNull String key, @Nullable C defaultValue, @NotNull Mapper<C> mapper) {
        super(key, defaultValue);
        this.mapper = mapper;
    }

    @NotNull
    public static <T> CollectionToStringPreference<List<T>, T> forList(@NotNull String key, @Nullable List<T> defaultValue, @NotNull Mapper<List<T>> mapper) {
        return new CollectionToStringPreference<List<T>, T>(key, defaultValue, mapper);
    }

    @NotNull
    public static <T> CollectionToStringPreference<List<T>, T> forTypedList(@NotNull String key, @Nullable List<T> defaultValue, @NotNull Mapper<T> mapper) {
        return new CollectionToStringPreference<List<T>, T>(key, defaultValue, ListMapper.newInstance(mapper));
    }

    @NotNull
    public static CollectionToStringPreference<List<String>, String> forStringList(@NotNull String key, @Nullable List<String> defaultValue) {
        return new CollectionToStringPreference<List<String>, String>(key, defaultValue, ListMapper.newInstance(StringMapper.getInstance()));
    }

    /*
    **********************************************************************
    *
    *                           METHODS
    *
    **********************************************************************
    */

    @Override
    protected C getPersistedValue(@NotNull SharedPreferences preferences) {
        return mapper.parseValue(preferences.getString(getKey(), null));
    }

    @Override
    protected void putPersistedValue(@NotNull SharedPreferences.Editor editor, @NotNull C values) {
        editor.putString(getKey(), mapper.formatValue(values));
    }
}
