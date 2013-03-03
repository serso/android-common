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
public class ListItemArrayAdapter<LI extends ListItem> extends ListAdapter<LI> {

    protected ListItemArrayAdapter(@Nonnull Context context,
                                @Nonnull List<? extends LI> listItems) {
        super(context, 0, 0, castList(listItems));
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    private static <LI> List<LI> castList(List<? extends LI> listItems) {
        return (List<LI>)listItems;
    }

    @Nonnull
    public static <LI extends ListItem> ListItemArrayAdapter<LI> createAndAttach(@Nonnull final ListActivity listActivity,
                                                                                 @Nonnull List<LI> listItems) {
        final ListItemArrayAdapter<LI> result = new ListItemArrayAdapter<LI>(listActivity, listItems);

        listActivity.setListAdapter(result);

        fillListView(listActivity.getListView(), listActivity, result);

        return result;
    }

    @Nonnull
    public static <LI extends ListItem> ListItemArrayAdapter<LI> createAndAttach(@Nonnull final ListFragment listFragment,
                                                                                 @Nonnull List<LI> listItems) {
        final ListItemArrayAdapter<LI> result = new ListItemArrayAdapter<LI>(listFragment.getActivity(), listItems);

        listFragment.setListAdapter(result);

        fillListView(listFragment.getListView(), listFragment.getActivity(), result);

        return result;
    }

    @Nonnull
    public static <LI extends ListItem> ListItemArrayAdapter<LI> createAndAttach(@Nonnull final android.app.ListFragment listFragment,
                                                                                 @Nonnull List<LI> listItems) {
        final ListItemArrayAdapter<LI> result = new ListItemArrayAdapter<LI>(listFragment.getActivity(), listItems);

        listFragment.setListAdapter(result);

        fillListView(listFragment.getListView(), listFragment.getActivity(), result);

        return result;
    }

    private static <LI extends ListItem> void fillListView(final ListView lv, final Context context, final ListItemArrayAdapter<LI> result) {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ListItem listItem = (ListItem) lv.getItemAtPosition(position);
                ListItem.OnClickAction onClickAction = listItem.getOnClickAction();
                if ( onClickAction != null ) {
                    onClickAction.onClick(context, result, lv);
                }
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final ListItem listItem = (ListItem) lv.getItemAtPosition(position);
                ListItem.OnClickAction onLongClickAction = listItem.getOnLongClickAction();
                if ( onLongClickAction != null ) {
                    onLongClickAction.onClick(context, result, lv);
                    return true;
                } else {
                    return false;
                }
            }
        });
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
