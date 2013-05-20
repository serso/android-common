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

package org.solovyev.android.samples.menu;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import org.solovyev.android.Fragments;
import org.solovyev.android.list.ListItemAdapter;
import org.solovyev.android.menu.*;
import org.solovyev.android.samples.R;
import org.solovyev.common.JPredicate;
import org.solovyev.common.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * User: serso
 * Date: 8/10/12
 * Time: 1:11 PM
 */
public class SamplesMenuActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.acl_menu_layout);

		Fragments.createFragment(this, SamplesListFragment.class, R.id.acl_main_linearlayout, "menu-list");
	}

    /*
	**********************************************************************
    *
    *                           STATIC
    *
    **********************************************************************
    */

	public static final class SamplesListFragment extends ListFragment {

		private ActivityMenu<Menu, MenuItem> menu;

		private boolean ascSort = true;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			setHasOptionsMenu(true);
		}

		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			super.onViewCreated(view, savedInstanceState);

			final List<MenuListItem> listItems = new ArrayList<MenuListItem>();
			listItems.add(new MenuListItem(R.string.menu_01, 1));
			listItems.add(new MenuListItem(R.string.menu_02, 2));
			listItems.add(new MenuListItem(R.string.menu_03, 3));
			ListItemAdapter.createAndAttach(this, listItems);
		}

		@Override
		public ListItemAdapter<MenuListItem> getListAdapter() {
			return (ListItemAdapter<MenuListItem>) super.getListAdapter();
		}

        /*
        **********************************************************************
        *
        *                           MENU
        *
        **********************************************************************
        */

		@Override
		public void onCreateOptionsMenu(@Nonnull Menu menu, @Nonnull MenuInflater inflater) {
			if (this.menu == null) {
				// create menu
				final List<LabeledMenuItem<MenuItem>> menuItems = new ArrayList<LabeledMenuItem<MenuItem>>();

				Collections.addAll(menuItems, SamplesStaticMenu.values());
				menuItems.add(new ReverseSortMenuItem());
				menuItems.add(new AscSortMenuItem());

				this.menu = ListActivityMenu.fromList(menuItems, AndroidMenuHelper.getInstance(), new MenuFilter());
			}

			this.menu.onCreateOptionsMenu(this.getActivity(), menu);
		}

		private class MenuFilter implements JPredicate<AMenuItem<MenuItem>> {

			@Override
			public boolean apply(@Nullable AMenuItem<MenuItem> menuItem) {
				if (menuItem instanceof AscSortMenuItem) {
					return !ascSort;
				} else {
					return false;
				}
			}
		}

		private static class ListItemComparator implements Comparator<MenuListItem> {

			private final boolean sortAsc;

			private ListItemComparator(boolean sortAsc) {
				this.sortAsc = sortAsc;
			}

			@Override
			public int compare(MenuListItem lhs, MenuListItem rhs) {
				int result = Objects.compare(lhs.getSortOrder(), rhs.getSortOrder());
				if (sortAsc) {
					return result;
				} else {
					return -result;
				}
			}
		}

		private class ReverseSortMenuItem extends AbstractLabeledMenuItem<MenuItem> {

			public ReverseSortMenuItem() {
				super(R.string.reverse_sort);
			}

			@Override
			public void onClick(@Nonnull MenuItem data, @Nonnull Context context) {
				final SamplesListFragment a = SamplesListFragment.this;

				a.ascSort = !a.ascSort;
				getListAdapter().sort(new ListItemComparator(a.ascSort));
			}
		}

		/**
		 * this menu item is shown only in ascending mode {@link MenuFilter}
		 */
		private class AscSortMenuItem extends AbstractLabeledMenuItem<MenuItem> {

			public AscSortMenuItem() {
				super(R.string.asc_menu_item);
			}

			@Override
			public void onClick(@Nonnull MenuItem data, @Nonnull Context context) {
				Toast.makeText(context, getString(R.string.asc_sort_menu_item_text), Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public boolean onOptionsItemSelected(@Nonnull MenuItem item) {
			return menu.onOptionsItemSelected(this.getActivity(), item);
		}

		@Override
		public void onPrepareOptionsMenu(@Nonnull Menu menu) {
			this.menu.onPrepareOptionsMenu(this.getActivity(), menu);
		}
	}
}
