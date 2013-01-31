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
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class HashSetPreferenceTest extends AbstractPreferenceTest<Set<String>> {

    @NotNull
    @Override
    protected Preference<Set<String>> createPreference(@NotNull String key, @Nullable Set<String> defaultValue) {
        return HashSetPreference.ofStrings(key, defaultValue);
    }

    @NotNull
    @Override
    protected Set<String> createDefaultValue() {
        return new HashSet<String>(Arrays.asList("1", "2", "3", "4"));
    }

    @Override
    protected Set<String> createValue() {
        return new HashSet<String>(Arrays.asList("5", "4", "3", "2", "1"));
    }
}
