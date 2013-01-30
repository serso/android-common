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
