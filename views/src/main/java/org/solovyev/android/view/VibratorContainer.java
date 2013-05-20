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

package org.solovyev.android.view;

import android.content.SharedPreferences;
import android.os.Vibrator;
import org.solovyev.android.prefs.BooleanPreference;
import org.solovyev.android.prefs.NumberToStringPreference;
import org.solovyev.android.prefs.Preference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * User: serso
 * Date: 10/26/11
 * Time: 11:40 PM
 */
public class VibratorContainer implements SharedPreferences.OnSharedPreferenceChangeListener {

	public static class Preferences {
		public static final Preference<Boolean> hapticFeedbackEnabled = BooleanPreference.of("org.solovyev.android.calculator.CalculatorModel_haptic_feedback", false);
		public static final Preference<Long> hapticFeedbackDuration = NumberToStringPreference.of("org.solovyev.android.calculator.CalculatorActivity_calc_haptic_feedback_duration_key", 60L, Long.class);
	}

	private final float vibrationTimeScale;

	@Nullable
	private final Vibrator vibrator;

	private long time = 0;

	public VibratorContainer(@Nullable Vibrator vibrator, @Nonnull SharedPreferences preferences, float vibrationTimeScale) {
		this.vibrator = vibrator;
		this.vibrationTimeScale = vibrationTimeScale;

		preferences.registerOnSharedPreferenceChangeListener(this);
		onSharedPreferenceChanged(preferences, null);

	}

	public void vibrate() {
		if (time > 0 && vibrator != null) {
			vibrator.vibrate(time);
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences preferences, @Nullable String key) {
		if (Preferences.hapticFeedbackEnabled.getPreference(preferences)) {
			//noinspection ConstantConditions
			this.time = getScaledValue(Preferences.hapticFeedbackDuration.getPreference(preferences));
		} else {
			this.time = 0;
		}
	}

	private long getScaledValue(long vibrationTime) {
		return (long) (vibrationTime * vibrationTimeScale);
	}
}
