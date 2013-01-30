package org.solovyev.android.prefs;

import android.content.SharedPreferences;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.common.text.Mapper;
import org.solovyev.common.text.NumberMapper;

/**
 * User: serso
 * Date: 4/23/12
 * Time: 1:07 AM
 */
public class NumberToStringPreference<N extends Number> extends AbstractPreference<N> {

    @NotNull
    private final Mapper<N> mapper;

    public NumberToStringPreference(@NotNull String key, @Nullable N defaultValue, @NotNull Class<N> clazz) {
        super(key, defaultValue);

        this.mapper = NumberMapper.of(clazz);
    }

    @Override
    protected N getPersistedValue(@NotNull SharedPreferences preferences) {
        return mapper.parseValue(preferences.getString(getKey(), "0"));
    }

    @Override
    protected void putPersistedValue(@NotNull SharedPreferences.Editor editor, @NotNull N value) {
        editor.putString(getKey(), mapper.formatValue(value));
    }

}
