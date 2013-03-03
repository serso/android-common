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

package org.solovyev.android.sherlock;

import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.actionbarsherlock.app.*;
import com.actionbarsherlock.view.MenuInflater;
import javax.annotation.Nonnull;
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

    @Nonnull
    public static ActionBar getSupportActionBar(@Nonnull Activity activity) {
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

    public static ActionBar getSupportActionBar(@Nonnull Fragment fragment) {
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


    @Nonnull
    public static ListAdapter<String> newSherlockDefaultAdapter(@Nonnull SherlockFragmentActivity activity,
                                                                @Nonnull List<String> items) {
        final ListAdapter<String> result = new ListAdapter<String>(activity, com.actionbarsherlock.R.layout.sherlock_spinner_item, items);
        result.setDropDownViewResource(com.actionbarsherlock.R.layout.sherlock_spinner_dropdown_item);
        return result;
    }

    @Nonnull
    public static MenuInflater getSupportMenuInflater(@Nonnull Activity activity) {
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

    public static void showDialog(@Nonnull DialogFragment dialogFragment,
                                  @Nonnull String fragmentTag,
                                  @Nonnull FragmentManager fm) {
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
