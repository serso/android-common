package org.solovyev.android.samples;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;
import org.solovyev.android.list.ListItem;
import org.solovyev.android.list.ListItemArrayAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: serso
 * Date: 8/8/12
 * Time: 1:42 AM
 */
public class SamplesActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.samples_list);

        final ListView lv = getListView();

        final List<SampleType> listItems = new ArrayList<SampleType>();
        Collections.addAll(listItems, SampleType.values());
        ListItemArrayAdapter.createAndAttach(lv, this, listItems);
    }


}
