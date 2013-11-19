package org.solovyev.android.list;

import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * User: serso
 * Date: 6/3/13
 * Time: 2:03 PM
 */
public class ListAdapterTest extends ActivityInstrumentationTestCase2<ListAdapterActivity> {

	public ListAdapterTest() {
		super(ListAdapterActivity.class);
	}

	@SmallTest
	public void testSaveState() throws Exception {
		final ListAdapterActivity activity = getActivity();
		ListItemAdapter<? extends ListItem> adapter = activity.getAdapter();
		adapter.filter("test");
		assertEquals("test", adapter.getFilterText());
		Bundle outState = new Bundle();
		adapter.saveState(outState);
		ListItemAdapter<? extends ListItem> newAdapter = new ListItemAdapter<ListItem>(activity, new ArrayList<ListItem>());
		newAdapter.restoreState(outState);
		assertEquals("test", newAdapter.getFilterText());
	}

	@UiThreadTest
	public void testList() throws Exception {
		final Random random = new Random(new Date().getTime());
		final ListAdapterActivity activity = getActivity();
		final ListItemAdapter<? extends ListItem> adapter = activity.getAdapter();
		for (int i = 0; i < 100; i++) {
			int oldSize = adapter.getCount();
			int size = random.nextInt(10);

			activity.addItems(size);
			Thread.sleep(10);

			assertEquals(oldSize + size, adapter.getCount());
			checkOrder(adapter);
			adapter.filter(String.valueOf(random.nextInt(9)), new Filter.FilterListener() {
				@Override
				public void onFilterComplete(int count) {
					checkOrder(adapter);
				}
			});
			activity.removeItems(size);
			activity.addItemsViaRunnable(size);
			Thread.sleep(10);

			assertEquals(oldSize + size, adapter.getCount());
			checkOrder(adapter);
			adapter.filter(String.valueOf(random.nextInt(9)), new Filter.FilterListener() {
				@Override
				public void onFilterComplete(int count) {
					checkOrder(adapter);
				}
			});
			activity.removeItems(size);
			Thread.sleep(10);
		}
	}

	private void checkOrder(final ListItemAdapter<? extends ListItem> adapter) {
		adapter.doWork(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < adapter.getCount(); i++) {
					if ( i > 0 ) {
						final ListItem prevItem = adapter.getItem(i-1);
						final ListItem item = adapter.getItem(i);
						assertTrue(prevItem.toString().compareTo(item.toString()) <= 0);
					}
				}
			}
		});
	}

}
