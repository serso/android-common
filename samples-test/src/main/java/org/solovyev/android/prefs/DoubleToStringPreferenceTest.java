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

import javax.annotation.Nonnull;

public class DoubleToStringPreferenceTest extends NumberToStringPreferenceTest<Double> {

	@Override
	public void testPreferences() throws Exception {
		super.testPreferences();

		runPreferenceTest(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
	}

	@Nonnull
	@Override
	protected Class<Double> getNumberClass() {
		return Double.class;
	}

	@Nonnull
	@Override
	protected Double createDefaultValue() {
		return 324d;
	}

	@Override
	protected Double createValue() {
		return -0d;
	}
}
