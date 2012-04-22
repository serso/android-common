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
public class LongToStringPreference extends AbstractPreference<Long> {

    private static final Mapper<Long> mapper = new NumberMapper<Long>(Long.class);

    public LongToStringPreference(@NotNull String key, @Nullable Long defaultValue) {
        super(key, defaultValue);
    }

    @Override
    protected Long getPersistedValue(@NotNull SharedPreferences preferences) {
        return mapper.parseValue(preferences.getString(getKey(), "0"));
    }

    @Override
    protected void putPersistedValue(@NotNull SharedPreferences.Editor editor, @NotNull Long value) {
        editor.putString(getKey(), mapper.formatValue(value));
    }

}
