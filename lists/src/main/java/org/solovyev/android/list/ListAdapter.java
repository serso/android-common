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

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static java.util.Collections.unmodifiableList;

/**
 * User: serso
 * Date: 6/6/12
 * Time: 12:53 AM
 */
public class ListAdapter<T> extends BaseAdapter implements Filterable {

	/*
	**********************************************************************
	*
	*                           CONSTANTS
	*
	**********************************************************************
	*/


	@Nonnull
	private static final String FILTER_TEXT = "filter_text";

	/*
	**********************************************************************
	*
	*                           FIELDS
	*
	**********************************************************************
	*/

	/**
	 * Contains the list of objects that represent the data of this ListAdapter.
	 * The content of this list is referred to as "the array" in the documentation.
	 */
	@Nonnull
	private List<T> shownElements;

	// if null => then shown elements = all elements
	@Nullable
	private List<T> allElements;

	/**
	 * Lock used to modify the content of {@link #shownElements}. Any write operation
	 * performed on the array should be synchronized on this lock. This lock is also
	 * used by the filter (see {@link #getFilter()} to make a synchronized copy of
	 * the original array of data.
	 */
	private final Object lock = new Object();

	/**
	 * The resource indicating what views to inflate to display the content of this
	 * array adapter.
	 */
	private int resources;

	/**
	 * The resource indicating what views to inflate to display the content of this
	 * array adapter in a drop down widget.
	 */
	private int dropDownResource;

	/**
	 * If the inflated resource is not a TextView, fieldId is used to find
	 * a TextView inside the inflated views hierarchy. This field must contain the
	 * identifier that matches the one defined in the resource file.
	 */
	private final int fieldId; // = 0 by default

	/**
	 * Indicates whether or not {@link #notifyDataSetChanged()} must be called whenever
	 * {@link #shownElements} is modified.
	 */
	private boolean notifyOnChange = true;

	@Nullable
	private CharSequence filterText;

	@Nonnull
	private final Context context;

	@Nullable
	private Filter filter;

	@Nonnull
	private final LayoutInflater inflater;

	@Nonnull
	private final ListAdapter<T>.AdapterHelper adapterHelper;

	/**
	 * Constructor
	 *
	 * @param context            The current context.
	 * @param textViewResourceId The resource ID for a layout file containing a TextView to use when
	 *                           instantiating views.
	 */
	public ListAdapter(@Nonnull Context context, int textViewResourceId) {
		this(context, textViewResourceId, 0, new ArrayList<T>());
	}

	/**
	 * Constructor
	 *
	 * @param context            The current context.
	 * @param resource           The resource ID for a layout file containing a layout to use when
	 *                           instantiating views.
	 * @param textViewResourceId The id of the TextView within the layout resource to be populated
	 */
	public ListAdapter(Context context, int resource, int textViewResourceId) {
		this(context, resource, textViewResourceId, new ArrayList<T>());
	}

	/**
	 * Constructor
	 *
	 * @param context            The current context.
	 * @param textViewResourceId The resource ID for a layout file containing a TextView to use when
	 *                           instantiating views.
	 * @param elements           The objects to represent in the ListView.
	 */
	public ListAdapter(@Nonnull Context context, int textViewResourceId, @Nonnull T[] elements) {
		this(context, textViewResourceId, 0, Arrays.asList(elements));
	}

	/**
	 * Constructor
	 *
	 * @param context            The current context.
	 * @param resource           The resource ID for a layout file containing a layout to use when
	 *                           instantiating views.
	 * @param textViewResourceId The id of the TextView within the layout resource to be populated
	 * @param elements           The objects to represent in the ListView.
	 */
	public ListAdapter(@Nonnull Context context, int resource, int textViewResourceId, @Nonnull T[] elements) {
		this(context, resource, textViewResourceId, Arrays.asList(elements));
	}

	/**
	 * Constructor
	 *
	 * @param context            The current context.
	 * @param textViewResourceId The resource ID for a layout file containing a TextView to use when
	 *                           instantiating views.
	 * @param elements           The objects to represent in the ListView.
	 */
	public ListAdapter(@Nonnull Context context, int textViewResourceId, @Nonnull List<T> elements) {
		this(context, textViewResourceId, 0, elements);
	}

	/**
	 * Constructor
	 *
	 * @param context            The current context.
	 * @param resource           The resource ID for a layout file containing a layout to use when
	 *                           instantiating views.
	 * @param textViewResourceId The id of the TextView within the layout resource to be populated
	 * @param elements           The objects to represent in the ListView.
	 */
	public ListAdapter(@Nonnull Context context, int resource, int textViewResourceId, @Nonnull List<T> elements) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.resources = this.dropDownResource = resource;
		this.shownElements = elements;
		this.fieldId = textViewResourceId;
		this.adapterHelper = new AdapterHelper();
	}

	/**
	 * Adds the specified object at the end of the array.
	 *
	 * @param object The object to add at the end of the array.
	 */
	public void add(T object) {
		boolean changed;

		synchronized (lock) {
			if (allElements != null) {
				changed = allElements.add(object);
			} else {
				changed = shownElements.add(object);
			}

			resort(changed);
		}

		tryNotifyDataSetChanged(changed);
	}

	private void resort(boolean changed) {
		assert Thread.holdsLock(lock);

		if (changed) {
			final Comparator<? super T> comparator = getComparator();
			if (comparator != null) {
				sort(comparator, false);
			}
		}
	}

	private void tryNotifyDataSetChanged(boolean changed) {
		if (changed) {
			refilter();

			if (notifyOnChange) {
				notifyDataSetChanged();
			}
		}
	}

	/**
	 * Adds the specified Collection at the end of the array.
	 *
	 * @param collection The Collection to add at the end of the array.
	 */
	public void addAll(Collection<? extends T> collection) {
		boolean changed;

		synchronized (lock) {
			if (allElements != null) {
				changed = allElements.addAll(collection);
			} else {
				changed = shownElements.addAll(collection);
			}

			resort(changed);
		}

		tryNotifyDataSetChanged(changed);
	}

	/**
	 * Adds the specified items at the end of the array.
	 *
	 * @param items The items to add at the end of the array.
	 */
	public void addAll(T... items) {
		boolean changed;

		synchronized (lock) {
			if (allElements != null) {
				changed = Collections.addAll(allElements, items);
			} else {
				changed = Collections.addAll(shownElements, items);
			}

			resort(changed);
		}

		tryNotifyDataSetChanged(changed);
	}

	/**
	 * Inserts the specified object at the specified index in the array.
	 *
	 * @param object The object to insert into the array.
	 * @param index  The index at which the object must be inserted.
	 */
	public void insert(T object, int index) {
		synchronized (lock) {
			if (allElements != null) {
				allElements.add(index, object);
			} else {
				shownElements.add(index, object);
			}

			resort(true);
		}

		tryNotifyDataSetChanged(true);
	}

	/**
	 * Removes the specified object from the array.
	 *
	 * @param object The object to remove.
	 */
	public void remove(T object) {
		boolean changed;

		synchronized (lock) {
			if (allElements != null) {
				changed = allElements.remove(object);
			} else {
				changed = shownElements.remove(object);
			}

			resort(changed);
		}

		tryNotifyDataSetChanged(changed);
	}

	public void removeAt(int position) {
		boolean changed;

		synchronized (lock) {
			if (allElements != null) {
				changed = allElements.remove(position) != null;
			} else {
				changed = shownElements.remove(position) != null;
			}

			resort(changed);
		}

		tryNotifyDataSetChanged(changed);
	}


	/**
	 * Remove all elements from the list.
	 */
	public void clear() {
		boolean changed;

		synchronized (lock) {
			if (allElements != null) {
				changed = !allElements.isEmpty();
				allElements.clear();
			} else {
				changed = !shownElements.isEmpty();
				shownElements.clear();
			}

			resort(changed);
		}

		tryNotifyDataSetChanged(changed);
	}

	/**
	 * Sorts the content of this adapter using the specified comparator.
	 *
	 * @param comparator The comparator used to sort the objects contained
	 *                   in this adapter.
	 */
	public void sort(Comparator<? super T> comparator) {
		sort(comparator, true);
	}

	private void sort(Comparator<? super T> comparator, boolean notify) {
		synchronized (lock) {
			Collections.sort(shownElements, comparator);

			// must sort all elements!!!
			if (allElements != null) {
				Collections.sort(allElements, comparator);
			}
		}

		if (notify) {
			tryNotifyDataSetChanged(true);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		notifyOnChange = true;
	}

	/**
	 * Control whether methods that change the list ({@link #add},
	 * {@link #insert}, {@link #remove}, {@link #clear}) automatically call
	 * {@link #notifyDataSetChanged}.  If set to false, caller must
	 * manually call notifyDataSetChanged() to have the changes
	 * reflected in the attached view.
	 * <p/>
	 * The default is true, and calling notifyDataSetChanged()
	 * resets the flag to true.
	 *
	 * @param notifyOnChange if true, modifications to the list will
	 *                       automatically call {@link
	 *                       #notifyDataSetChanged}
	 */
	public void setNotifyOnChange(boolean notifyOnChange) {
		this.notifyOnChange = notifyOnChange;
	}

	/**
	 * Returns the context associated with this array adapter. The context is used
	 * to create views from the resource passed to the constructor.
	 *
	 * @return The Context associated with this adapter.
	 */
	@Nonnull
	public Context getContext() {
		return context;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getCount() {
		synchronized (lock) {
			return shownElements.size();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public T getItem(int position) {
		synchronized (lock) {
			return shownElements.get(position);
		}
	}

	/**
	 * Returns the position of the specified item in the array.
	 *
	 * @param item The item to retrieve the position of.
	 * @return The position of the specified item.
	 */
	public int getPosition(T item) {
		synchronized (lock) {
			return shownElements.indexOf(item);
		}
	}

	public boolean containsInShown(@Nonnull T element) {
		synchronized (lock) {
			return this.shownElements.contains(element);
		}
	}

	public boolean containsInAll(@Nonnull T element) {
		return getAllElements().contains(element);
	}

	/**
	 * {@inheritDoc}
	 */
	public long getItemId(int position) {
		return position;
	}

	/**
	 * {@inheritDoc}
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position, convertView, parent, resources);
	}

	public void doWork(@Nonnull Runnable runnable) {
		final boolean notifyOnChange = isNotifyOnChange();
		try {
			setNotifyOnChange(false);
			runnable.run();
		} finally {
			setNotifyOnChange(notifyOnChange);
			if (notifyOnChange) {
				notifyDataSetChanged();
			}
		}
	}

	private View createViewFromResource(int position,
										@Nullable View convertView,
										ViewGroup parent,
										int resource) {
		final View view;
		if (convertView != null) {
			view = convertView;
		} else {
			view = inflater.inflate(resource, parent, false);
		}


		TextView text;
		try {
			if (fieldId == 0) {
				//  If no custom field is assigned, assume the whole resource is a TextView
				text = (TextView) view;
			} else {
				//  Otherwise, find the TextView field within the layout
				text = (TextView) view.findViewById(fieldId);
			}
		} catch (ClassCastException e) {
			Log.e("ListAdapter", "You must supply a resource ID for a TextView");
			throw new IllegalStateException(
					"ListAdapter requires the resource ID to be a TextView", e);
		}

		T item = getItem(position);
		if (item instanceof CharSequence) {
			text.setText((CharSequence) item);
		} else {
			text.setText(item.toString());
		}

		return view;
	}

	/**
	 * <p>Sets the layout resource to create the drop down views.</p>
	 *
	 * @param resource the layout resource defining the drop down views
	 * @see #getDropDownView(int, android.view.View, android.view.ViewGroup)
	 */
	public void setDropDownViewResource(int resource) {
		this.dropDownResource = resource;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position, convertView, parent, dropDownResource);
	}

	/**
	 * Creates a new ListAdapter from external resources. The content of the array is
	 * obtained through {@link android.content.res.Resources#getTextArray(int)}.
	 *
	 * @param context        The application's environment.
	 * @param textArrayResId The identifier of the array to use as the data source.
	 * @param textViewResId  The identifier of the layout used to create views.
	 * @return An ListAdapter<CharSequence>.
	 */
	public static ListAdapter<CharSequence> createFromResource(Context context,
															   int textArrayResId, int textViewResId) {
		CharSequence[] strings = context.getResources().getTextArray(textArrayResId);
		return new ListAdapter<CharSequence>(context, textViewResId, strings);
	}

	/**
	 * {@inheritDoc}
	 */
	public final Filter getFilter() {
		if (filter == null) {
			filter = createFilter();
		}
		return filter;
	}

	@Nonnull
	protected Filter createFilter() {
		return new PrefixAdapterFilter<T>(adapterHelper);
	}

	@Nonnull
	protected List<T> getShownElements() {
		synchronized (lock) {
			return unmodifiableList(shownElements);
		}
	}

	@Nonnull
	protected List<T> getAllElements() {
		synchronized (lock) {
			return unmodifiableList(allElements == null ? shownElements : allElements);
		}
	}

	@Nullable
	protected Comparator<? super T> getComparator() {
		return null;
	}


	public void saveState(@Nonnull Bundle outState) {
		if (filterText != null) {
			outState.putString(FILTER_TEXT, filterText.toString());
		}
	}

	public void restoreState(@Nonnull Bundle savedInstanceState) {
		filterText = savedInstanceState.getString(FILTER_TEXT);
	}

	@Nonnull
	protected AdapterHelper getAdapterHelper() {
		return adapterHelper;
	}

	public boolean isNotifyOnChange() {
		return notifyOnChange;
	}

	public void filter(@Nullable CharSequence filterText) {
		this.filterText = filterText;
		this.getFilter().filter(filterText);
	}

	public void filter(@Nullable CharSequence filterText, @Nullable Filter.FilterListener listener) {
		this.filterText = filterText;
		this.getFilter().filter(filterText, listener);
	}


	public void refilter() {
		this.getFilter().filter(filterText);
	}

	@Nullable
	public CharSequence getFilterText() {
		return filterText;
	}

	public class AdapterHelper implements AdapterFilter.Helper<T> {

		@Nonnull
		@Override
		public Object getLock() {
			return lock;
		}

		@Nonnull
		@Override
		public List<T> getShownElements() {
			return shownElements;
		}

		@Override
		public void setShownElements(@Nonnull List<T> shownElements) {
			ListAdapter.this.shownElements = shownElements;
		}

		@Nullable
		@Override
		public List<T> getAllElements() {
			return allElements;
		}

		@Override
		public void setAllElements(@Nonnull List<T> allElements) {
			ListAdapter.this.allElements = allElements;
		}

		@Override
		public void notifyDataSetChanged() {
			if (notifyOnChange) {
				ListAdapter.this.notifyDataSetChanged();
			}
		}
	}
}
