package org.solovyev.android.sherlock;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import org.jetbrains.annotations.NotNull;
import org.solovyev.android.list.ListAdapter;

import java.util.List;

/**
 * User: serso
 * Date: 8/7/12
 * Time: 12:01 PM
 */
public class ActionBarNavigationFragmentListener<T extends Fragment> implements ActionBar.OnNavigationListener {

    @NotNull
    private final SherlockFragmentActivity activity;

    @NotNull
    private List<? extends FragmentItem> items;

    private int selected = -1;

    @NotNull
    private ListAdapter<String> adapter;

    public ActionBarNavigationFragmentListener(@NotNull SherlockFragmentActivity activity,
                                               @NotNull List<? extends FragmentItem> items,
                                               @NotNull List<String> itemLabels) {
        assert  items.size() == itemLabels.size();
        this.activity = activity;
        this.items = items;
        this.adapter = AndroidSherlockUtils.newSherlockDefaultAdapter(activity, itemLabels);
    }

    @NotNull
    public synchronized ListAdapter getAdapter() {
        return adapter;
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        final FragmentManager fragmentManager =  this.activity.getSupportFragmentManager();

        final FragmentTransaction ft = fragmentManager.beginTransaction();
        try {
            if (selected != itemPosition) {
                if (selected >= 0 && selected < items.size()) {
                    items.get(selected).onUnselected(ft);
                }

                if (itemPosition < items.size()) {
                    items.get(itemPosition).onSelected(ft);
                    selected = itemPosition;
                }
            }
        } finally {
            if (ft != null && !ft.isEmpty()) {
                ft.commit();
            }
        }

        return true;
    }
}
