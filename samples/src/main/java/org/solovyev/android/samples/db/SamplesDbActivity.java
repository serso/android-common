package org.solovyev.android.samples.db;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.android.async.CommonAsyncTask;
import org.solovyev.android.list.ListItem;
import org.solovyev.android.list.ListItemArrayAdapter;
import org.solovyev.android.samples.R;
import org.solovyev.android.samples.SamplesApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * User: serso
 * Date: 8/10/12
 * Time: 4:28 PM
 */
public class SamplesDbActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.db_layout);

        ListItemArrayAdapter.createAndAttach(getListView(), this, new ArrayList<ListItem<? extends View>>());

        new CommonAsyncTask<Void, Void, List<ListItem<? extends View>>>(this) {

            @Override
            protected List<ListItem<? extends View>> doWork(@NotNull List<Void> voids) {
                final List<ListItem<? extends View>> result = new ArrayList<ListItem<? extends View>>();

                for (DbItem dbItem : getDbItemService().getAllDbItems(getContext())) {
                    result.add(new DbItemListItem(dbItem));
                }

                return result;
            }

            @Override
            protected void onSuccessPostExecute(@Nullable List<ListItem<? extends View>> result) {
                ((ListItemArrayAdapter) getListView().getAdapter()).addAll(result);
            }

            @Override
            protected void onFailurePostExecute(@NotNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }.execute((Void) null);
    }

    @NotNull
    private DbItemService getDbItemService() {
        return SamplesApplication.getLocator().getDbItemService();
    }
}
