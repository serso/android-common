package org.solovyev.android.prefs;

import android.content.SharedPreferences;
import org.jetbrains.annotations.NotNull;
import org.solovyev.common.text.Mapper;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class CollectionSetPreference<C extends Collection<T>, T> extends AbstractPreference<C> {

	@NotNull
	private final Mapper<T> mapper;

	public CollectionSetPreference(@NotNull String id, @NotNull C defaultValue, @NotNull Mapper<T> mapper) {
		super(id, defaultValue);
		this.mapper = mapper;
	}

	@Override
	protected C getPersistedValue(@NotNull SharedPreferences preferences) {
		final Set<String> stringValues = preferences.getStringSet(getKey(), null);

		final C result = createCollection(stringValues.size());
		for (String stringValue : stringValues) {
			result.add(mapper.parseValue(stringValue));
		}

		return result;
	}

	@NotNull
	protected abstract C createCollection(int size);

	@Override
	protected void putPersistedValue(@NotNull SharedPreferences.Editor editor, @NotNull C values) {

		final Set<String> result = new HashSet<String>(10);
		for (T value : values) {
			result.add(mapper.formatValue(value));
		}

		editor.putStringSet(getKey(), result);
	}
}
