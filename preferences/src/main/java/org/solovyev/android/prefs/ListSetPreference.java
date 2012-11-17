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
