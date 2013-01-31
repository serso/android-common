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
import org.solovyev.common.interval.Interval;
import org.solovyev.common.text.Mapper;
import org.solovyev.common.text.NumberIntervalMapper;

/**
 * User: serso
 * Date: 8/8/12
 * Time: 11:53 PM
 */
public final class NumberIntervalPreference<N extends Number & Comparable<N>> extends AbstractPreference<Interval<N>> {

    @NotNull
    private final Mapper<Interval<N>> mapper;

    private NumberIntervalPreference(@NotNull String key, @Nullable Interval<N> defaultValue, @NotNull Class<N> clazz) {
        super(key, defaultValue);
        this.mapper = NumberIntervalMapper.of(clazz);

    }

    @NotNull
    public static <N extends Number & Comparable<N>> NumberIntervalPreference<N> of(@NotNull String key, @Nullable Interval<N> defaultValue, @NotNull Class<N> clazz) {
        return new NumberIntervalPreference<N>(key, defaultValue, clazz);
    }

    @Override
    protected Interval<N> getPersistedValue(@NotNull SharedPreferences preferences) {
        final String result = preferences.getString(getKey(), null);
        if ( result == null ) {
            return null;
        } else {
            return mapper.parseValue(result);
        }
    }

    @Override
    protected void putPersistedValue(@NotNull SharedPreferences.Editor editor, @NotNull Interval<N> value) {
        editor.putString(getKey(), mapper.formatValue(value));
    }

}
