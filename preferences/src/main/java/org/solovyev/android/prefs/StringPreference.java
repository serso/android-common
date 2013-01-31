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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.common.text.EnumMapper;
import org.solovyev.common.text.Mapper;
import org.solovyev.common.text.StringMapper;

/**
 * User: serso
 * Date: 12/25/11
 * Time: 12:37 PM
 */

/**
 * {@link Preference} implementation which uses {@link String} way of storing object in persistence.
 * This class provides methods for mapping real java objects to String and vice versa.
 * @param <T>
 */
public final class StringPreference<T> extends AbstractPreference<T> {

	@NotNull
	private final Mapper<T> mapper;

	public StringPreference(@NotNull String key, @Nullable T defaultValue, @NotNull Mapper<T> mapper) {
		super(key, defaultValue);
		this.mapper = mapper;
	}

	@NotNull
	public static StringPreference<String> of(@NotNull String key, @Nullable String defaultValue) {
		return new StringPreference<String>(key, defaultValue, StringMapper.getInstance());
	}

	@NotNull
	public static <T> StringPreference<T> ofTypedValue(@NotNull String key, @Nullable String defaultValue, @NotNull Mapper<T> mapper) {
		return new StringPreference<T>(key, mapper.parseValue(defaultValue), mapper);
	}

	@NotNull
	public static <T extends Enum> StringPreference<T> ofEnum(@NotNull String key, @Nullable T defaultValue, @NotNull Class<T> enumType) {
		return new StringPreference<T>(key, defaultValue, EnumMapper.of(enumType));
	}

	@Override
	protected T getPersistedValue(@NotNull SharedPreferences preferences) {
		return mapper.parseValue(preferences.getString(getKey(), null));
	}

	@Override
	protected void putPersistedValue(@NotNull SharedPreferences.Editor editor, @NotNull T value) {
		editor.putString(getKey(), mapper.formatValue(value));
	}
}
