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

    private LocalePreference(@NotNull String id, @Nullable Locale defaultValue, @NotNull Mapper<Locale> localeMapper) {
        this.stringPreference = new StringPreference<Locale>(id, defaultValue, localeMapper);
    }

    private LocalePreference(@NotNull String id, @Nullable Locale defaultValue) {
        this.stringPreference = new StringPreference<Locale>(id, defaultValue, DefaultLocaleMapper.getInstance());
    }

    @NotNull
    public static LocalePreference of(@NotNull String id, @Nullable Locale defaultValue, @NotNull Mapper<Locale> localeMapper) {
        return new LocalePreference(id, defaultValue, localeMapper);
    }

    @NotNull
    public static LocalePreference of(@NotNull String id, @Nullable Locale defaultValue) {
        return new LocalePreference(id, defaultValue);
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

    @Override
    public boolean tryPutDefault(@NotNull SharedPreferences preferences) {
        return stringPreference.tryPutDefault(preferences);
    }

    @Override
    public boolean isSameKey(@NotNull String key) {
        return stringPreference.isSameKey(key);
    }

    /*
    **********************************************************************
    *
    *                           STATIC
    *
    **********************************************************************
    */

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
