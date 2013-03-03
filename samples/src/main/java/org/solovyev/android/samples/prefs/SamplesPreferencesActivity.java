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

package org.solovyev.android.samples.prefs;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import javax.annotation.Nonnull;
import org.solovyev.android.samples.R;

/**
 * User: serso
 * Date: 8/8/12
 * Time: 2:09 AM
 */
public class SamplesPreferencesActivity
        extends PreferenceActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        updateAnswer(Preferences.toBeOrNotTobe.getKey(), getString(Preferences.toBeOrNotTobe.getPreference(prefs).getCaptionResId()));
        updateAnswer(Preferences.integerNumber.getKey(), String.valueOf(Preferences.integerNumber.getPreference(prefs)));
        updateAnswer(Preferences.doubleNumber.getKey(), String.valueOf(Preferences.doubleNumber.getPreference(prefs)));
        updateAnswer(Preferences.country.getKey(), String.valueOf(Preferences.country.getPreference(prefs)));
        updateAnswer(Preferences.floatInterval.getKey(), String.valueOf(Preferences.floatInterval.getPreference(prefs)));
        updateAnswer(Preferences.integerInterval.getKey(), String.valueOf(Preferences.integerInterval.getPreference(prefs)));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if (Preferences.toBeOrNotTobe.getKey().equals(key)) {
            final String answer = getString(Preferences.toBeOrNotTobe.getPreference(prefs).getCaptionResId());
            updateAnswer(key, answer);
        }

        if (Preferences.integerNumber.getKey().equals(key)) {
            final String answer = String.valueOf(Preferences.integerNumber.getPreference(prefs));
            updateAnswer(key, answer);
        }

        if (Preferences.doubleNumber.getKey().equals(key)) {
            final String answer = String.valueOf(Preferences.doubleNumber.getPreference(prefs));
            updateAnswer(key, answer);
        }

        if (Preferences.country.getKey().equals(key)) {
            final String answer = String.valueOf(Preferences.country.getPreference(prefs));
            updateAnswer(key, answer);
        }

        if (Preferences.floatInterval.getKey().equals(key)) {
            final String answer = String.valueOf(Preferences.floatInterval.getPreference(prefs));
            updateAnswer(key, answer);
        }

        if (Preferences.integerInterval.getKey().equals(key)) {
            final String answer = String.valueOf(Preferences.integerInterval.getPreference(prefs));
            updateAnswer(key, answer);
        }
    }

    private void updateAnswer(@Nonnull String key, @Nonnull String answer) {
        final android.preference.Preference preference = findPreference(key);
        preference.setSummary(getString(R.string.answer) + ": " + answer);
    }
}
