package org.solovyev.android.samples.prefs;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import org.jetbrains.annotations.NotNull;
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

    private void updateAnswer(@NotNull String key, @NotNull String answer) {
        final android.preference.Preference preference = findPreference(key);
        preference.setSummary(getString(R.string.answer) + ": " + answer);
    }
}
