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

package org.solovyev.android.samples.db;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import javax.annotation.Nonnull;
import org.solovyev.android.list.ListItem;
import org.solovyev.android.samples.R;
import org.solovyev.android.view.TextViewBuilder;

/**
 * User: serso
 * Date: 8/10/12
 * Time: 5:06 PM
 */
public class DbItemListItem implements ListItem {

    @Nonnull
    private final DbItem dbItem;

    public DbItemListItem(@Nonnull DbItem dbItem) {
        this.dbItem = dbItem;
    }

    @Override
    public OnClickAction getOnClickAction() {
        return null;
    }

    @Override
    public OnClickAction getOnLongClickAction() {
        return null;
    }

    @Nonnull
    @Override
    public View updateView(@Nonnull Context context, @Nonnull View view) {
        if (this.getTag().equals(view.getTag())) {
            fillView(context, (TextView) view);
            return view;
        } else {
            return build(context);
        }
    }

    @Nonnull
    @Override
    public View build(@Nonnull Context context) {
        final TextView view = TextViewBuilder.newInstance(R.layout.acl_db_list_item, getTag()).build(context);

        fillView(context, view);

        return view;
    }

    @Nonnull
    private String getTag() {
        return "db_list_item";
    }

    private void fillView(@Nonnull Context context, @Nonnull TextView view) {
        view.setText(dbItem.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DbItemListItem)) return false;

        DbItemListItem that = (DbItemListItem) o;

        if (!dbItem.equals(that.dbItem)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return dbItem.hashCode();
    }
}
