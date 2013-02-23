package org.solovyev.android;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * User: serso
 * Date: 2/23/13
 * Time: 12:33 PM
 */
public final class Fragments {

    private Fragments() {
        throw new AssertionError();
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

    public static void showDialog(@NotNull android.support.v4.app.DialogFragment dialogFragment,
                                  @NotNull String fragmentTag,
                                  @NotNull android.support.v4.app.FragmentManager fm) {
        final android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();

        android.support.v4.app.Fragment prev = fm.findFragmentByTag(fragmentTag);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        dialogFragment.show(ft, fragmentTag);
    }

    public static void createFragment(@NotNull FragmentActivity activity,
                                      @NotNull Class<? extends android.support.v4.app.Fragment> fragmentClass,
                                      int parentViewId,
                                      @NotNull String tag) {
        createFragment(activity, fragmentClass, parentViewId, tag, null);
    }

    public static void createFragment(@NotNull FragmentActivity activity,
                                      @NotNull Class<? extends android.support.v4.app.Fragment> fragmentClass,
                                      int parentViewId,
                                      @NotNull String tag,
                                      @Nullable Bundle args) {
        final android.support.v4.app.FragmentManager fm = activity.getSupportFragmentManager();

        android.support.v4.app.Fragment messagesFragment = fm.findFragmentByTag(tag);

        final android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        try {
            if (messagesFragment == null) {
                messagesFragment = android.support.v4.app.Fragment.instantiate(activity, fragmentClass.getName(), args);
                ft.add(parentViewId, messagesFragment, tag);
            } else {
                if (messagesFragment.isDetached()) {
                    ft.attach(messagesFragment);
                }
            }
        } finally {
            ft.commit();
        }
    }
}
