/*
 * Copyright (c) 2009-2011. Created by serso aka se.solovyev.
 * For more information, please, contact se.solovyev@gmail.com
 * or visit http://se.solovyev.org
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
