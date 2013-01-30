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
 * ---------------------------------------------------------------------
 * Contact details
 *
 * Email: se.solovyev@gmail.com
 * Site:  http://se.solovyev.org
 */

package org.solovyev.android.prefs;

import android.content.SharedPreferences;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * User: serso
 * Date: 12/25/11
 * Time: 12:23 PM
 */

/**
 * Base class for {@link Preference} implementation. Contains preference key and default value
 * @param <T> type of preference
 */
public abstract class AbstractPreference<T> implements Preference<T> {

	@NotNull
	private final String key;

	private final T defaultValue;

	protected AbstractPreference(@NotNull String key, @Nullable T defaultValue) {
		this.key = key;
		this.defaultValue = defaultValue;
	}

	@NotNull
	public String getKey() {
		return key;
	}

	public T getDefaultValue() {
		return defaultValue;
	}

	@Override
	public final T getPreference(@NotNull SharedPreferences preferences) {
		if ( isSet(preferences) ) {
			return getPersistedValue(preferences);
		} else {
			return this.defaultValue;
		}
	}

    @Override
    public T getPreferenceNoError(@NotNull SharedPreferences preferences) {
        if (isSet(preferences)) {
            try {
                return getPersistedValue(preferences);
            } catch (RuntimeException e) {
                return this.defaultValue;
            }
        } else {
            return this.defaultValue;
        }
    }

    @Override
	public void putDefault(@NotNull SharedPreferences preferences) {
		putPreference(preferences, this.defaultValue);
	}

	@Override
	public void putPreference(@NotNull SharedPreferences preferences, @Nullable T value) {
		if (value != null) {
			final SharedPreferences.Editor editor = preferences.edit();
			putPersistedValue(editor, value);
			editor.commit();
		}
	}

	@Override
	public boolean isSet(@NotNull SharedPreferences preferences) {
		return preferences.contains(this.key);
	}

    /*
    **********************************************************************
    *
    *                           ABSTRACT METHODS
    *
    **********************************************************************
    */

    /**
     * @param preferences preferences container
     * @return preference value from preferences with key defined by {@link #getKey()} method
     */
    @Nullable
    protected abstract T getPersistedValue(@NotNull SharedPreferences preferences);

    /**
     * Method saved preference to preferences container editor
     * @param editor editor in which value must be saved
     * @param value value to be saved
     */
    protected abstract void putPersistedValue(@NotNull SharedPreferences.Editor editor, @NotNull T value);

}
