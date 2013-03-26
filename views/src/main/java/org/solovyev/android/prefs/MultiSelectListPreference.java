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


import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.preference.ListPreference;
import android.preference.Preference;
import android.util.AttributeSet;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.solovyev.android.view.R;
import org.solovyev.common.text.StringCollections;
import org.solovyev.common.text.StringMapper;
import org.solovyev.common.text.Strings;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link Preference} that displays a list of entries as
 * a dialog and allows multiple selections
 * <p>
 * This preference will store a string into the SharedPreferences. This string will be the values selected
 * from the {@link #setEntryValues(CharSequence[])} array.
 * </p>
 */
public class MultiSelectListPreference<T> extends ListPreference {

	@Nonnull
	private static final String DEFAULT_SEPARATOR = ";";

	@Nonnull
	private final org.solovyev.common.text.Mapper<List<String>> mapper;

	private boolean[] checkedIndices;

	/*
	**********************************************************************
	*
	*                           CONSTRUCTORS
	*
	**********************************************************************
	*/

	public MultiSelectListPreference(Context context) {
		this(context, null);
	}

	public MultiSelectListPreference(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);

		String separator = DEFAULT_SEPARATOR;

		final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MultiSelectListPreference);
		for (int i = 0; i < a.getIndexCount(); i++) {
			int attr = a.getIndex(i);

			final String attrValue = a.getString(attr);

			if (!Strings.isEmpty(attrValue)) {
				switch (attr) {
					case R.styleable.MultiSelectListPreference_separator:
						separator = attrValue;
						break;
				}
			}
		}

		this.mapper = new Mapper(separator);

		this.checkedIndices = new boolean[getEntries().length];
	}

	@Override
	public void setEntries(@Nonnull CharSequence[] entries) {
		super.setEntries(entries);

		checkedIndices = new boolean[entries.length];
	}

	@Override
	protected void onPrepareDialogBuilder(@Nonnull Builder builder) {
		final CharSequence[] entries = getEntries();
		final CharSequence[] entryValues = getEntryValues();

		if (entries == null || entryValues == null || entries.length != entryValues.length) {
			throw new IllegalStateException("ListPreference requires an entries array and an entryValues array which are both the same length");
		}

		restoreCheckedEntries();

		builder.setMultiChoiceItems(entries, checkedIndices,
				new DialogInterface.OnMultiChoiceClickListener() {
					public void onClick(DialogInterface dialog, int which, boolean value) {
						checkedIndices[which] = value;
					}
				});
	}


	private void restoreCheckedEntries() {
		final CharSequence[] entryValues = getEntryValues();

		final List<String> values = mapper.parseValue(getValue());
		if (values != null) {
			for (String value : values) {
				for (int i = 0; i < entryValues.length; i++) {
					final CharSequence entry = entryValues[i];
					if (entry.equals(value)) {
						checkedIndices[i] = true;
						break;
					}
				}
			}
		}
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		final CharSequence[] entryValues = getEntryValues();
		if (positiveResult && entryValues != null) {

			final List<String> checkedValues = new ArrayList<String>();
			for (int i = 0; i < entryValues.length; i++) {
				if (checkedIndices[i]) {
					checkedValues.add(entryValues[i].toString());
				}
			}


			final String value = mapper.formatValue(checkedValues);
			if (callChangeListener(value)) {
				setValue(value);
			}
		}
	}

	public static class Mapper implements org.solovyev.common.text.Mapper<List<String>> {

		@Nonnull
		private final String separator;

		public Mapper(@Nonnull String separator) {
			this.separator = separator;
		}

		@Override
		public String formatValue(@Nullable List<String> value) throws IllegalArgumentException {
			return StringCollections.formatValue(value, separator, StringMapper.getInstance());
		}

		@Override
		public List<String> parseValue(@Nullable String value) throws IllegalArgumentException {
			return StringCollections.split(value, separator, StringMapper.getInstance());
		}
	}

    @Nonnull
    public static <T> org.solovyev.common.text.Mapper<List<T>> newListMapper(@Nonnull org.solovyev.common.text.Mapper<T> nestedMapper) {
        return new ListMapper<T>(DEFAULT_SEPARATOR, nestedMapper);
    }

    @Nonnull
    public static <T> org.solovyev.common.text.Mapper<List<T>> newListMapper(@Nonnull org.solovyev.common.text.Mapper<T> nestedMapper,
                                                                             @Nonnull String separator) {
        return new ListMapper<T>(separator, nestedMapper);
    }


	private static class ListMapper<T> implements org.solovyev.common.text.Mapper<List<T>> {

		@Nonnull
		private final String separator;

		@Nonnull
		private final org.solovyev.common.text.Mapper<T> nestedMapper;

		public ListMapper(@Nonnull String separator, @Nonnull org.solovyev.common.text.Mapper<T> nestedMapper) {
			this.separator = separator;
			this.nestedMapper = nestedMapper;
		}

		@Override
		public String formatValue(@Nullable List<T> value) throws IllegalArgumentException {
			return StringCollections.formatValue(value, separator, nestedMapper);
		}

		@Override
		public List<T> parseValue(@Nullable String value) throws IllegalArgumentException {
			return StringCollections.split(value, separator, nestedMapper);
		}
	}
}

