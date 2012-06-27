package org.solovyev.android.list;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * User: serso
 * Date: 6/6/12
 * Time: 12:53 AM
 */
public class ListAdapter<T> extends BaseAdapter {
    /**
     * Contains the list of objects that represent the data of this ListAdapter.
     * The content of this list is referred to as "the array" in the documentation.
     */
    @NotNull
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
     * If the inflated resource is not a TextView, {@link #fieldId} is used to find
     * a TextView inside the inflated views hierarchy. This field must contain the
     * identifier that matches the one defined in the resource file.
     */
    private final int fieldId; // = 0 by default

    /**
     * Indicates whether or not {@link #notifyDataSetChanged()} must be called whenever
     * {@link #shownElements} is modified.
     */
    private boolean notifyOnChange = true;


    @NotNull
    private final Context context;

    @Nullable
    private Filter filter;

    @NotNull
    private final LayoutInflater inflater;

    @NotNull
    private final ListAdapter<T>.AdapterHelper adapterHelper;

    /**
     * Constructor
     *
     * @param context            The current context.
     * @param textViewResourceId The resource ID for a layout file containing a TextView to use when
     *                           instantiating views.
     */
    public ListAdapter(@NotNull Context context, int textViewResourceId) {
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
    public ListAdapter(@NotNull Context context, int textViewResourceId, @NotNull T[] elements) {
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
    public ListAdapter(@NotNull Context context, int resource, int textViewResourceId, @NotNull T[] elements) {
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
    public ListAdapter(@NotNull Context context, int textViewResourceId, @NotNull List<T> elements) {
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
    public ListAdapter(@NotNull Context context, int resource, int textViewResourceId, @NotNull List<T> elements) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        if (allElements != null) {
            synchronized (lock) {
                allElements.add(object);
                if (notifyOnChange) notifyDataSetChanged();
            }
        } else {
            shownElements.add(object);
            if (notifyOnChange) notifyDataSetChanged();
        }
    }

    /**
     * Adds the specified Collection at the end of the array.
     *
     * @param collection The Collection to add at the end of the array.
     */
    public void addAll(Collection<? extends T> collection) {
        if (allElements != null) {
            synchronized (lock) {
                allElements.addAll(collection);
                if (notifyOnChange) notifyDataSetChanged();
            }
        } else {
            shownElements.addAll(collection);
            if (notifyOnChange) notifyDataSetChanged();
        }
    }

    /**
     * Adds the specified items at the end of the array.
     *
     * @param items The items to add at the end of the array.
     */
    public void addAll(T... items) {
        if (allElements != null) {
            synchronized (lock) {
                Collections.addAll(allElements, items);
                if (notifyOnChange) notifyDataSetChanged();
            }
        } else {
            Collections.addAll(shownElements, items);
            if (notifyOnChange) notifyDataSetChanged();
        }
    }

    /**
     * Inserts the specified object at the specified index in the array.
     *
     * @param object The object to insert into the array.
     * @param index  The index at which the object must be inserted.
     */
    public void insert(T object, int index) {
        if (allElements != null) {
            synchronized (lock) {
                allElements.add(index, object);
                if (notifyOnChange) notifyDataSetChanged();
            }
        } else {
            shownElements.add(index, object);
            if (notifyOnChange) notifyDataSetChanged();
        }
    }

    /**
     * Removes the specified object from the array.
     *
     * @param object The object to remove.
     */
    public void remove(T object) {
        if (allElements != null) {
            synchronized (lock) {
                allElements.remove(object);
            }
        } else {
            shownElements.remove(object);
        }
        if (notifyOnChange) notifyDataSetChanged();
    }

    /**
     * Remove all elements from the list.
     */
    public void clear() {
        if (allElements != null) {
            synchronized (lock) {
                allElements.clear();
            }
        } else {
            shownElements.clear();
        }
        if (notifyOnChange) notifyDataSetChanged();
    }

    /**
     * Sorts the content of this adapter using the specified comparator.
     *
     * @param comparator The comparator used to sort the objects contained
     *                   in this adapter.
     */
    public void sort(Comparator<? super T> comparator) {
        Collections.sort(shownElements, comparator);

        // must to sort all elements!!!
        if (allElements != null) {
            Collections.sort(allElements, comparator);
        }
        if (notifyOnChange) notifyDataSetChanged();
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
    @NotNull
    public Context getContext() {
        return context;
    }

    /**
     * {@inheritDoc}
     */
    public int getCount() {
        return shownElements.size();
    }

    /**
     * {@inheritDoc}
     */
    public T getItem(int position) {
        return shownElements.get(position);
    }

    /**
     * Returns the position of the specified item in the array.
     *
     * @param item The item to retrieve the position of.
     * @return The position of the specified item.
     */
    public int getPosition(T item) {
        return shownElements.indexOf(item);
    }

    public boolean containsInShown(@NotNull T element) {
        return this.shownElements.contains(element);
    }

    public boolean containsInAll(@NotNull T element) {
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

    public void doWork(@NotNull Runnable runnable) {
        final boolean notifyOnChange = isNotifyOnChange();
        try {
            setNotifyOnChange(false);
            runnable.run();
        } finally {
            setNotifyOnChange(notifyOnChange);
            if ( notifyOnChange ) {
                notifyDataSetChanged();
            }
        }
    }

    private View createViewFromResource(int position, View convertView, ViewGroup parent,
                                        int resource) {
        View view;
        TextView text;

        if (convertView == null) {
            view = inflater.inflate(resource, parent, false);
        } else {
            view = convertView;
        }

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
    public Filter getFilter() {
        if (filter == null) {
            filter = new PrefixAdapterFilter<T>(adapterHelper);
        }
        return filter;
    }

    @NotNull
    protected List<T> getShownElements() {
        return Collections.unmodifiableList(shownElements);
    }

    @NotNull
    protected List<T> getAllElements() {
        return Collections.unmodifiableList(allElements == null ? shownElements : allElements);
    }

    @NotNull
    protected AdapterHelper getAdapterHelper() {
        return adapterHelper;
    }

    public boolean isNotifyOnChange() {
        return notifyOnChange;
    }

    private class AdapterHelper implements AdapterFilter.Helper<T> {

        @NotNull
        @Override
        public Object getLock() {
            return lock;
        }

        @NotNull
        @Override
        public List<T> getShownElements() {
            return shownElements;
        }

        @Override
        public void setShownElements(@NotNull List<T> shownElements) {
            ListAdapter.this.shownElements = shownElements;
        }

        @Nullable
        @Override
        public List<T> getAllElements() {
            return allElements;
        }

        @Override
        public void setAllElements(@NotNull List<T> allElements) {
            ListAdapter.this.allElements = allElements;
        }

        @Override
        public void notifyDataSetChanged() {
            ListAdapter.this.notifyDataSetChanged();
        }
    }

    /**
     * <p>An array filter constrains the content of the array adapter with
     * a prefix. Each item that does not start with the supplied prefix
     * is removed from the list.</p>
     */
/*    private class ArrayFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (allElements == null) {
                synchronized (lock) {
                    allElements = new ArrayList<T>(shownElements);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    ArrayList<T> list = new ArrayList<T>(allElements);
                    results.values = list;
                    results.count = list.size();
                }
            } else {
                String prefixString = prefix.toString().toLowerCase();

                final List<T> values = allElements;
                final int count = values.size();

                final ArrayList<T> newValues = new ArrayList<T>(count);

                for (int i = 0; i < count; i++) {
                    final T value = values.get(i);
                    final String valueText = value.toString().toLowerCase();

                    // First match against the whole, non-splitted value
                    if (valueText.startsWith(prefixString)) {
                        newValues.add(value);
                    } else {
                        final String[] words = valueText.split(" ");
                        final int wordCount = words.length;

                        for (int k = 0; k < wordCount; k++) {
                            if (words[k].startsWith(prefixString)) {
                                newValues.add(value);
                                break;
                            }
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            shownElements = (List<T>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }*/
}
