package org.solovyev.android.prefs;

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
public class LocalePreference extends StringPreference<Locale> {

    public LocalePreference(@NotNull String id, @Nullable Locale defaultValue, @NotNull Mapper<Locale> localeMapper) {
        super(id, defaultValue, localeMapper);
    }

    public LocalePreference(@NotNull String id, @Nullable Locale defaultValue) {
        super(id, defaultValue, DefaultLocaleMapper.getInstance());
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
