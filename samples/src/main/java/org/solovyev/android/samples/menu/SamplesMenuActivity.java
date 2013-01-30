package org.solovyev.android.samples.menu;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.android.list.ListItemArrayAdapter;
import org.solovyev.android.menu.*;
import org.solovyev.android.samples.R;
import org.solovyev.common.Objects;
import org.solovyev.common.JPredicate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * User: serso
 * Date: 8/10/12
 * Time: 1:11 PM
 */
public class SamplesMenuActivity extends ListActivity {

    private ActivityMenu<Menu, MenuItem> menu;

    private boolean ascSort = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final List<MenuListItem> listItems = new ArrayList<MenuListItem>();
        listItems.add(new MenuListItem(R.string.menu_01, 1));
        listItems.add(new MenuListItem(R.string.menu_02, 2));
        listItems.add(new MenuListItem(R.string.menu_03, 3));
        ListItemArrayAdapter.createAndAttach(getListView(), this, listItems);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ListItemArrayAdapter<MenuListItem> getListAdapter() {
        return (ListItemArrayAdapter<MenuListItem>) super.getListAdapter();
    }

    /*
    **********************************************************************
    *
    *                           MENU
    *
    **********************************************************************
    */

    @Override
    public boolean onCreateOptionsMenu(@NotNull Menu menu) {
        if (this.menu == null) {
            // create menu
            final List<LabeledMenuItem<MenuItem>> menuItems = new ArrayList<LabeledMenuItem<MenuItem>>();

            Collections.addAll(menuItems, SamplesStaticMenu.values());
            menuItems.add(new ReverseSortMenuItem());
            menuItems.add(new AscSortMenuItem());

            this.menu = ListActivityMenu.fromList(menuItems, AndroidMenuHelper.getInstance(), new MenuFilter());
        }
        return this.menu.onCreateOptionsMenu(this, menu);
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
        public void onClick(@NotNull MenuItem data, @NotNull Context context) {
            final SamplesMenuActivity a = SamplesMenuActivity.this;

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
        public void onClick(@NotNull MenuItem data, @NotNull Context context) {
            Toast.makeText(context, getString(R.string.asc_sort_menu_item_text), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        return menu.onOptionsItemSelected(this, item);
    }

    @Override
    public boolean onPrepareOptionsMenu(@NotNull Menu menu) {
        return this.menu.onPrepareOptionsMenu(this, menu);
    }
}
