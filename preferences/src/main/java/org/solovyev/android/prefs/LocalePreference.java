package org.solovyev.android.prefs;

import android.content.SharedPreferences;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.common.text.Mapper;

import java.util.Locale;
import java.util.StringTokenizer;

/**
 * User: serso
 * Date: 4/15/12
 * Time: 7:09 PM
 */
public class LocalePreference implements Preference<Locale> {

    @NotNull
    private final StringPreference<Locale> stringPreference;

    public LocalePreference(@NotNull String id, @Nullable Locale defaultValue, @NotNull Mapper<Locale> localeMapper) {
        this.stringPreference = new StringPreference<Locale>(id, defaultValue, localeMapper);
    }

    public LocalePreference(@NotNull String id, @Nullable Locale defaultValue) {
        this.stringPreference = new StringPreference<Locale>(id, defaultValue, DefaultLocaleMapper.getInstance());
    }

    @Override
    @NotNull
    public String getKey() {
        return stringPreference.getKey();
    }

    @Override
    public Locale getDefaultValue() {
        return stringPreference.getDefaultValue();
    }

    @Override
    public Locale getPreference(@NotNull SharedPreferences preferences) {
        return stringPreference.getPreference(preferences);
    }

    @Override
    public Locale getPreferenceNoError(@NotNull SharedPreferences preferences) {
        return stringPreference.getPreferenceNoError(preferences);
    }

    @Override
    public void putDefault(@NotNull SharedPreferences preferences) {
        stringPreference.putDefault(preferences);
    }

    @Override
    public void putPreference(@NotNull SharedPreferences preferences, @Nullable Locale value) {
        stringPreference.putPreference(preferences, value);
    }

    @Override
    public boolean isSet(@NotNull SharedPreferences preferences) {
        return stringPreference.isSet(preferences);
    }

    private static final class DefaultLocaleMapper implements Mapper<Locale> {

        @NotNull
        private static final String delimiter = ";";

        @NotNull
        private static Mapper<Locale> instance = new DefaultLocaleMapper();

        private DefaultLocaleMapper() {
        }

        @NotNull
        public static Mapper<Locale> getInstance() {
            return instance;
        }

        @Override
        public String formatValue(@Nullable Locale locale) throws IllegalArgumentException {
            assert locale != null;
            return locale.getLanguage() + delimiter + locale.getCountry() + delimiter + locale.getVariant();
        }

        @Override
        public Locale parseValue(@Nullable String s) throws IllegalArgumentException {
            final StringTokenizer st = new StringTokenizer(s, delimiter, false);

            final String language = st.nextToken();

            final String country;
            if (st.hasMoreTokens()) {
                country = st.nextToken();
            } else {
                country = "";
            }

            final String variant;
            if (st.hasMoreTokens()) {
                variant = st.nextToken();
            } else {
                variant = "";
            }

            return new Locale(language, country, variant);
        }
    }
}
