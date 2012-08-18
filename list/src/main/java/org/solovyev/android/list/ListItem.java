package org.solovyev.android.list;

import android.content.Context;
import android.view.View;
import android.widget.ListView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.android.view.UpdatableViewBuilder;

/**
 * User: serso
 * Date: 4/14/12
 * Time: 4:28 PM
 */

/**
 * One row of list in Adapter. Can build, update view and provide functionality on clicking and on long clicking.
 */
public interface ListItem extends UpdatableViewBuilder<View> {

    static final String TAG = "ListItem";

    @Nullable
    OnClickAction getOnClickAction();

    @Nullable
    OnClickAction getOnLongClickAction();

    public static interface OnClickAction {

        void onClick(@NotNull Context context, @NotNull ListAdapter<? extends ListItem> adapter, @NotNull ListView listView);
    }
}
