package org.solovyev.android.sherlock.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.android.sherlock.FragmentItem;
import org.solovyev.android.sherlock.FragmentItemImpl;

/**
 * User: serso
 * Date: 8/4/12
 * Time: 12:42 PM
 */
public class ActionBarFragmentTabListener implements ActionBar.TabListener {

    private final FragmentItem fragmentItem;

    /**
     * Constructor used each time a new tab is created.
     *
     * @param activity      The host Activity, used to instantiate the fragment
     * @param tag           The identifier tag for the fragment
     * @param fragmentClass The fragment's Class, used to instantiate the fragment
     * @param fragmentArgs  arguments to be passed to fragment
     */

    public ActionBarFragmentTabListener(@NotNull SherlockFragmentActivity activity,
                                        @NotNull String tag,
                                        @NotNull Class<? extends Fragment> fragmentClass,
                                        @Nullable Bundle fragmentArgs) {
        this.fragmentItem = new FragmentItemImpl(activity, tag, fragmentClass, fragmentArgs);
    }


    /* The following are each of the ActionBar.TabListener callbacks */

    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        this.fragmentItem.onSelected(ft);
    }

    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        this.fragmentItem.onUnselected(ft);
    }

    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }
}
