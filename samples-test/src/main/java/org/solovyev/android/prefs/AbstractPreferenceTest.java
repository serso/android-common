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
import android.preference.PreferenceManager;
import android.test.AndroidTestCase;
import junit.framework.Assert;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractPreferenceTest<T> extends AndroidTestCase {

	public void testPreferences() throws Exception {
		runPreferenceTest(createDefaultValue(), createValue());
	}

	protected void runPreferenceTest(@Nullable T defaultValue, @Nullable T value) {
		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
		preferences.edit().clear().commit();

		final Preference<T> preference = createPreference("test", defaultValue);

		Assert.assertTrue(preference.isSameKey("test"));
		Assert.assertEquals(preference.getKey(), "test");

		Assert.assertEquals(false, preference.isSet(preferences));
		Assert.assertEquals(defaultValue, preference.getPreference(preferences));

		preference.tryPutDefault(preferences);
		Assert.assertEquals(true, preference.isSet(preferences));
		Assert.assertEquals(defaultValue, preference.getPreference(preferences));

		preference.putPreference(preferences, value);
		Assert.assertEquals(true, preference.isSet(preferences));
		Assert.assertEquals(value, preference.getPreference(preferences));
		Assert.assertEquals(defaultValue, preference.getDefaultValue());

		preferences.edit().clear().commit();

		Assert.assertEquals(false, preference.isSet(preferences));
		Assert.assertEquals(defaultValue, preference.getPreference(preferences));
	}

	@Nonnull
	protected abstract Preference<T> createPreference(@Nonnull String key, @Nullable T defaultValue);

	@Nonnull
	protected abstract T createDefaultValue();

	protected abstract T createValue();
}
