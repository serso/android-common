package org.solovyev.android.menu;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 4/23/12
 * Time: 1:56 PM
 */
public interface ActivityMenu {

    public boolean onCreateOptionsMenu(@NotNull Activity activity, @NotNull Menu menu);

   	public boolean onOptionsItemSelected(@NotNull Activity activity, @NotNull MenuItem item);
}
