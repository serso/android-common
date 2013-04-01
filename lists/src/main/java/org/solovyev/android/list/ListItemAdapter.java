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

import android.app.ListActivity;
import android.content.Context;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import javax.annotation.Nonnull;

import java.util.List;

/**
 * User: serso
 * Date: 4/14/12
 * Time: 8:02 PM
 */
public class ListItemAdapter<LI extends ListItem> extends ListAdapter<LI> {

    protected ListItemAdapter(@Nonnull Context context,
                              @Nonnull List<? extends LI> listItems) {
        super(context, 0, 0, castList(listItems));
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    private static <LI> List<LI> castList(List<? extends LI> listItems) {
        return (List<LI>)listItems;
    }

    @Nonnull
    public static <LI extends ListItem> ListItemAdapter<LI> createAndAttach(@Nonnull final ListActivity listActivity,
                                                                                 @Nonnull List<? extends LI> listItems) {
        final ListItemAdapter<LI> result = newInstance(listActivity, listItems);

        attach(listActivity, result);

        return result;
    }

    public static <LI extends ListItem> void attach(@Nonnull ListActivity listActivity, @Nonnull ListItemAdapter<? extends LI> adapter) {
        listActivity.setListAdapter(adapter);

        fillListView(listActivity.getListView(), adapter, listActivity);
    }

    @Nonnull
    public static <LI extends ListItem> ListItemAdapter<LI> createAndAttach(@Nonnull final ListFragment listFragment,
                                                                                 @Nonnull List<? extends LI> listItems) {
        final ListItemAdapter<LI> result = newInstance(listFragment.getActivity(), listItems);

        attach(listFragment, result);

        return result;
    }

    public static <LI extends ListItem> void attach(@Nonnull ListFragment listFragment, @Nonnull ListItemAdapter<? extends LI> adapter) {
        listFragment.setListAdapter(adapter);

        fillListView(listFragment.getListView(), adapter, listFragment.getActivity());
    }

    @Nonnull
    public static <LI extends ListItem> ListItemAdapter<LI> createAndAttach(@Nonnull final ListView listView,
                                                                                 @Nonnull List<? extends LI> listItems,
                                                                                 @Nonnull Context context) {
        final ListItemAdapter<LI> result = newInstance(context, listItems);

        attach(listView, result, context);

        return result;
    }

    public static <LI extends ListItem> void attach(@Nonnull ListView listView, @Nonnull ListItemAdapter<? extends LI> adapter, @Nonnull Context context) {
        listView.setAdapter(adapter);

        fillListView(listView, adapter, context);
    }

    @Nonnull
    public static <LI extends ListItem> ListItemAdapter<LI> createAndAttach(@Nonnull final android.app.ListFragment listFragment,
                                                                                 @Nonnull List<? extends LI> listItems) {
        final ListItemAdapter<LI> result = newInstance(listFragment.getActivity(), listItems);

        attach(listFragment, result);

        return result;
    }

    public static <LI extends ListItem> void attach(@Nonnull android.app.ListFragment listFragment, @Nonnull ListItemAdapter<? extends LI> adapter) {
        listFragment.setListAdapter(adapter);

        fillListView(listFragment.getListView(), adapter, listFragment.getActivity());
    }

    private static <LI extends ListItem> void fillListView(@Nonnull final ListView lv, @Nonnull final ListItemAdapter<? extends LI> adapter, @Nonnull final Context context) {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ListItem listItem = (ListItem) lv.getItemAtPosition(position);
                ListItem.OnClickAction onClickAction = listItem.getOnClickAction();
                if ( onClickAction != null ) {
                    onClickAction.onClick(context, adapter, lv);
                }
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final ListItem listItem = (ListItem) lv.getItemAtPosition(position);
                ListItem.OnClickAction onLongClickAction = listItem.getOnLongClickAction();
                if ( onLongClickAction != null ) {
                    onLongClickAction.onClick(context, adapter, lv);
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    @Nonnull
    public static <LI extends ListItem> ListItemAdapter<LI> newInstance(@Nonnull Context context,
                                                                                @Nonnull List<? extends LI> listItems) {
        return new ListItemAdapter<LI>(context, listItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ListItem listItem = getItem(position);

        if (convertView == null) {
            return listItem.build(getContext());
        } else {
            return listItem.updateView(getContext(), convertView);
        }
    }
}
