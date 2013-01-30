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
import org.solovyev.common.text.EnumMapper;
import org.solovyev.common.text.Mapper;
import org.solovyev.common.text.StringMapper;

import java.util.ArrayList;
import java.util.List;

public class ListSetPreference<T> extends CollectionSetPreference<List<T>, T> {

	@NotNull
	public static ListSetPreference<String> newInstance(@NotNull String id, @NotNull List<String> defaultValue) {
		return new ListSetPreference<String>(id, defaultValue, StringMapper.getInstance());
	}

	@NotNull
	public static <T> ListSetPreference<T> newInstance(@NotNull String id, @NotNull List<T> defaultValue, @NotNull Mapper<T> parser) {
		return new ListSetPreference<T>(id, defaultValue, parser);
	}

	@NotNull
	public static <T extends Enum> ListSetPreference<T> newInstance(@NotNull String id, @NotNull List<T> defaultValue, @NotNull Class<T> enumType) {
		return new ListSetPreference<T>(id, defaultValue, new EnumMapper<T>(enumType));
	}

	public ListSetPreference(@NotNull String id, @NotNull List<T> defaultValue, @NotNull Mapper<T> mapper) {
		super(id, defaultValue, mapper);
	}

	@NotNull
	@Override
	protected List<T> createCollection(int size) {
		return new ArrayList<T>(size);
	}
}
