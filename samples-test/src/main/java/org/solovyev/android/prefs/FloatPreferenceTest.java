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

import org.jetbrains.annotations.NotNull;

public class FloatPreferenceTest extends AbstractPreferenceTest<Float> {

    @NotNull
    @Override
    protected Preference<Float> createPreference(@NotNull String key, @NotNull Float defaultValue) {
        return FloatPreference.of(key, defaultValue);
    }

    @NotNull
    @Override
    protected Float createDefaultValue() {
        return 23423f;
    }

    @Override
    protected Float createValue() {
        return -0f;
    }
}
