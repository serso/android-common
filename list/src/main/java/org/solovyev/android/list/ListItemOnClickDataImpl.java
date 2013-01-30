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

package org.solovyev.android.list;

import android.widget.ListView;
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 5/5/12
 * Time: 8:17 PM
 */
public class ListItemOnClickDataImpl<T> implements ListItemOnClickData<T> {

    @NotNull
    private T dataObject;

    @NotNull
    private ListAdapter<? extends ListItem> adapter;

    @NotNull
    private ListView listView;

    public ListItemOnClickDataImpl(@NotNull T dataObject, @NotNull ListAdapter<? extends ListItem> adapter, @NotNull ListView listView) {
        this.dataObject = dataObject;
        this.adapter = adapter;
        this.listView = listView;
    }

    @NotNull
    @Override
    public T getDataObject() {
        return this.dataObject;
    }

    @NotNull
    @Override
    public ListAdapter<? extends ListItem> getAdapter() {
        return this.adapter;
    }

    @NotNull
    @Override
    public ListView getListView() {
        return this.listView;
    }
}
