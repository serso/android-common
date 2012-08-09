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
 * Time: 12:21 PM
 */

/**
 * Class for working with android preferences: can save and load preferences, convert them to custom java objects
 * and use default value;
 *
 * @param <T> type of java object preference
 */
public interface Preference<T> {

    /**
     * Method returns key of preference used in android: the key with which current preference is saved in persistence
     *
     * @return android preference key
     */
	@NotNull
	String getKey();

    /**
     * @return default preference value, may be null
     */
	T getDefaultValue();

    /**
     * NOTE: this method can throw runtime exceptions if errors occurred while extracting preferences values
     *
     * @param preferences application preferences
     * @return value from preference, default value if no value in preference was found
     */
	T getPreference(@NotNull SharedPreferences preferences);

    /**
     * NOTE: this method SHOULD not throw any runtime exceptions BUT return default value if any error occurred
     *
     * @param preferences application preferences
     * @return value from preference, default value if no value in preference was found or error occurred
     */
    T getPreferenceNoError(@NotNull SharedPreferences preferences);

    /**
     * Method puts (saves) preference represented by <code>value</code> in <code>preferences</code> container
     * @param preferences preferences container
     * @param value value to be saved
     */
	void putPreference(@NotNull SharedPreferences preferences, @Nullable T value);

    /**
     * Method saves default value in <code>preferences</code> container.
     * Should behave exactly as <code>p.putPreference(preferences, p.getDefaultValue())</code>
     * @param preferences preferences container
     */
	void putDefault(@NotNull SharedPreferences preferences);

    /**
     * @param preferences preferences container
     * @return true if any value is saved in preferences container, false - otherwise
     */
	boolean isSet(@NotNull SharedPreferences preferences);

}
