package org.solovyev.android.sherlock;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * User: serso
 * Date: 8/7/12
 * Time: 11:55 AM
 */
public class FragmentItemImpl implements FragmentItem {

    @NotNull
    private final SherlockFragmentActivity activity;

    // Fragment

    @NotNull
    private final String tag;

    @NotNull
    private final Class<? extends Fragment> fragmentClass;

    @Nullable
    private Bundle fragmentArgs;

    @Nullable
    private Fragment fragment;

    /**
     * Constructor used each time a new tab is created.
     *
     * @param activity      The host Activity, used to instantiate the fragment
     * @param tag           The identifier tag for the fragment
     * @param fragmentClass The fragment's Class, used to instantiate the fragment
     * @param fragmentArgs  arguments to be passed to fragment
     */

    public FragmentItemImpl(@NotNull SherlockFragmentActivity activity,
                            @NotNull String tag,
                            @NotNull Class<? extends Fragment> fragmentClass,
                            @Nullable Bundle fragmentArgs) {
        this.activity = activity;
        this.tag = tag;
        this.fragmentClass = fragmentClass;
        this.fragmentArgs = fragmentArgs;

        final FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
        this.fragment = supportFragmentManager.findFragmentByTag(tag);
    }


    @Override
    public void onSelected(@NotNull FragmentTransaction ft) {
        fragment = activity.getSupportFragmentManager().findFragmentByTag(this.tag);

        // Check if the fragment is already initialized
        if (fragment == null) {
            // If not, instantiate and add it to the activity
            fragment = Fragment.instantiate(activity, fragmentClass.getName(), fragmentArgs);
            ft.add(android.R.id.content, fragment, tag);
        } else {
            if (fragment.isDetached()) {
                // If it exists, simply attach it in order to show it
                ft.attach(fragment);
            }
        }
    }

    @Override
    public void onUnselected(@NotNull FragmentTransaction ft) {
        if (fragment != null) {
            ft.detach(fragment);
        }
    }
}
