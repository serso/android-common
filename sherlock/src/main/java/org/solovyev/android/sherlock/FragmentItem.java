package org.solovyev.android.sherlock;

import android.support.v4.app.FragmentTransaction;
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 8/7/12
 * Time: 11:57 AM
 */
public interface FragmentItem {

    public void onSelected(@NotNull FragmentTransaction ft);

    public void onUnselected(@NotNull FragmentTransaction ft);
}
