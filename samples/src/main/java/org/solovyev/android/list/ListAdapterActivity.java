package org.solovyev.android.list;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * User: serso
 * Date: 6/3/13
 * Time: 2:04 PM
 */
public class ListAdapterActivity extends ListActivity {

	private ListItemAdapter<TestListItem> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		adapter = new Adapter(this, generateItems(100));
		ListItemAdapter.attach(this, adapter);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		adapter.saveState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle state) {
		adapter.restoreState(state);
		super.onRestoreInstanceState(state);
	}

	void addItems(int size) {
		adapter.addAll(generateItems(size));
	}

	void addItemsViaRunnable(final int size) {
		adapter.doWork(new Runnable() {
			@Override
			public void run() {
				for (TestListItem listItem : generateItems(size)) {
					adapter.add(listItem);
				}
			}
		});
	}

	@Nonnull
	private List<TestListItem> generateItems(int size) {
		final List<TestListItem> result = new ArrayList<TestListItem>();

		for (  int i = 0; i < size; i++ ) {
			result.add(new TestListItem());
		}

		return result;
	}

	public void removeItems(int size) {
		final Random random = new Random(new Date().getTime());
		int i = size;
		while (i > 0) {
			final int position = random.nextInt(adapter.getCount());
			adapter.removeAt(position);
			i--;
		}
	}

	private static final class TestListItem implements ListItem {

		@Nonnull
		private static final AtomicLong counter = new AtomicLong();

		private final long index = counter.getAndIncrement();

		public TestListItem() {
		}

		@Nullable
		@Override
		public OnClickAction getOnClickAction() {
			return null;
		}

		@Nullable
		@Override
		public OnClickAction getOnLongClickAction() {
			return null;
		}

		@Nonnull
		@Override
		public View updateView(@Nonnull Context context, @Nonnull View view) {
			return build(context);
		}

		@Override
		public String toString() {
			return String.valueOf(index);
		}

		@Nonnull
		@Override
		public View build(@Nonnull Context context) {
			return new View(context);
		}
	}

	public ListItemAdapter<TestListItem> getAdapter() {
		return adapter;
	}

	private static final class Adapter extends ListItemAdapter<TestListItem> {

		protected Adapter(@Nonnull Context context, @Nonnull List<? extends TestListItem> listItems) {
			super(context, listItems);
		}

		@Nullable
		@Override
		protected Comparator<? super TestListItem> getComparator() {
			return new Comparator<TestListItem>() {
				@Override
				public int compare(TestListItem lhs, TestListItem rhs) {
					return lhs.toString().compareTo(rhs.toString());
				}
			};
		}
	}
}
