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
public class NumberIntervalPreference<N extends Number & Comparable<N>> extends AbstractPreference<Interval<N>> {

    @NotNull
    private final Mapper<Interval<N>> mapper;

    public NumberIntervalPreference(@NotNull String key, @Nullable Interval<N> defaultValue, @NotNull Class<N> clazz) {
        super(key, defaultValue);
        this.mapper = NumberIntervalMapper.of(clazz);

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
