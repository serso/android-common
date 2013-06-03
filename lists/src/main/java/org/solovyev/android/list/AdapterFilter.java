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

package org.solovyev.android.list;

import android.widget.Filter;
import org.solovyev.common.JPredicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: serso
 * Date: 6/6/12
 * Time: 1:08 AM
 */
public abstract class AdapterFilter<T> extends Filter {

	@Nonnull
	private final Helper<T> helper;

	public AdapterFilter(@Nonnull Helper<T> helper) {
		this.helper = helper;
	}

	@Nonnull
	@Override
	protected FilterResults performFiltering(@Nullable CharSequence prefix) {
		final FilterResults results = new FilterResults();

		final List<T> allElements;

		// elements to be shown on list view
		final List<T> filteredElements;
		if ((prefix == null || prefix.length() == 0) && !doFilterOnEmptyString()) {
			// no constraint => show all elements
			allElements = getAllElements();
			filteredElements = allElements;
		} else {
			// filter
			final JPredicate<T> filter = getFilter(prefix);
			synchronized (helper.getLock()) {
				allElements = new ArrayList<T>(getAllElements());
			}

			filteredElements = new ArrayList<T>(allElements.size());
			for (T element : allElements) {
				if (filter.apply(element)) {
					filteredElements.add(element);
				}
			}
		}

		results.values = filteredElements;
		results.allElements = allElements;
		results.count = filteredElements.size();

		return results;
	}

	@Nonnull
	private List<T> getAllElements() {
		List<T> result = helper.getAllElements();
		if (result == null) {
			result= helper.getShownElements();
		}
		return result;
	}

	protected boolean doFilterOnEmptyString() {
		return false;
	}

	protected abstract JPredicate<T> getFilter(@Nullable CharSequence prefix);

	@Override
	protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
		boolean changed = false;

		synchronized (helper.getLock()) {

			if (results.values != helper.getShownElements()) {
				helper.setShownElements((List<T>) results.values);
				changed = true;
			}

			if (((FilterResults) results).allElements != helper.getAllElements()) {
				helper.setAllElements((List<T>) ((FilterResults) results).allElements);
				changed = true;
			}

		}

		if (changed) {
			helper.notifyDataSetChanged();
		}

	}

	public static interface Helper<T> {

		@Nonnull
		Object getLock();

		@Nonnull
		List<T> getShownElements();

		void setShownElements(@Nonnull List<T> shownElements);

		@Nullable
		List<T> getAllElements();

		void setAllElements(@Nonnull List<T> allElements);

		void notifyDataSetChanged();
	}

	private static class FilterResults<T> extends Filter.FilterResults {

		@Nullable
		private List<T> allElements;
	}
}