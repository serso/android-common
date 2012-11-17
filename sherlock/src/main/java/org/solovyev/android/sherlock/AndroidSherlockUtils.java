package org.solovyev.android.sherlock;

import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.actionbarsherlock.app.*;
import com.actionbarsherlock.view.MenuInflater;
import org.jetbrains.annotations.NotNull;
import org.solovyev.android.list.ListAdapter;

import java.util.List;

/**
 * User: serso
 * Date: 8/13/12
 * Time: 2:04 AM
 */
public final class AndroidSherlockUtils {

    private AndroidSherlockUtils() {
        throw new AssertionError("Not intended for instantiation!");
    }

    @NotNull
    public static ActionBar getSupportActionBar(@NotNull Activity activity) {
        if (activity instanceof SherlockActivity) {
            return ((SherlockActivity) activity).getSupportActionBar();
        }

        if (activity instanceof SherlockFragmentActivity) {
            return ((SherlockFragmentActivity) activity).getSupportActionBar();
        }

        if (activity instanceof SherlockListActivity) {
            return ((SherlockListActivity) activity).getSupportActionBar();
        }

        if (activity instanceof SherlockPreferenceActivity) {
            return ((SherlockPreferenceActivity) activity).getSupportActionBar();
        }

        throw new IllegalArgumentException(activity.getClass() + " is not supported!");

    }

    public static ActionBar getSupportActionBar(@NotNull Fragment fragment) {
        if (fragment instanceof SherlockFragment) {
            return ((SherlockFragment) fragment).getSherlockActivity().getSupportActionBar();
        }

        if (fragment instanceof SherlockListFragment) {
            return ((SherlockListFragment) fragment).getSherlockActivity().getSupportActionBar();
        }

        if (fragment instanceof SherlockDialogFragment) {
            return ((SherlockDialogFragment) fragment).getSherlockActivity().getSupportActionBar();
        }

        throw new IllegalArgumentException(fragment.getClass() + " is not supported!");
    }


    @NotNull
    public static ListAdapter<String> newSherlockDefaultAdapter(@NotNull SherlockFragmentActivity activity,
                                                                @NotNull List<String> items) {
        final ListAdapter<String> result = new ListAdapter<String>(activity, com.actionbarsherlock.R.layout.sherlock_spinner_item, items);
        result.setDropDownViewResource(com.actionbarsherlock.R.layout.sherlock_spinner_dropdown_item);
        return result;
    }

    @NotNull
    public static MenuInflater getSupportMenuInflater(@NotNull Activity activity) {
        if (activity instanceof SherlockActivity) {
            return ((SherlockActivity) activity).getSupportMenuInflater();
        }

        if (activity instanceof SherlockFragmentActivity) {
            return ((SherlockFragmentActivity) activity).getSupportMenuInflater();
        }

        if (activity instanceof SherlockListActivity) {
            return ((SherlockListActivity) activity).getSupportMenuInflater();
        }

        if (activity instanceof SherlockPreferenceActivity) {
            return ((SherlockPreferenceActivity) activity).getSupportMenuInflater();
        }

        throw new IllegalArgumentException(activity.getClass() + " is not supported!");
    }

    public static void showDialog(@NotNull DialogFragment dialogFragment,
                                  @NotNull String fragmentTag,
                                  @NotNull FragmentManager fm) {
        final FragmentTransaction ft = fm.beginTransaction();

        Fragment prev = fm.findFragmentByTag(fragmentTag);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        dialogFragment.show(ft, fragmentTag);
    }
}
