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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import javax.annotation.Nonnull;
import org.solovyev.android.list.ListAdapter;
import org.solovyev.android.list.ListItem;
import org.solovyev.android.samples.db.SamplesDbActivity;
import org.solovyev.android.samples.http.SamplesHttpActivity;
import org.solovyev.android.samples.keyboard.SamplesKeyboardActivity;
import org.solovyev.android.samples.menu.SamplesMenuActivity;
import org.solovyev.android.samples.prefs.SamplesPreferencesActivity;
import org.solovyev.android.samples.view.SamplesViewActivity;
import org.solovyev.android.view.TextViewBuilder;

/**
 * User: serso
 * Date: 8/8/12
 * Time: 2:06 AM
 */
public enum SampleType implements ListItem {
    preferences(R.string.preferences, SamplesPreferencesActivity.class),
    http(R.string.http, SamplesHttpActivity.class),
    db(R.string.db, SamplesDbActivity.class),
    view(R.string.view, SamplesViewActivity.class),
    keyboard(R.string.keyboard, SamplesKeyboardActivity.class),
    menu(R.string.menu, SamplesMenuActivity.class);

    private final int captionResId;

    @Nonnull
    private final Class<? extends Activity> sampleActivity;

    private SampleType(int captionResId, @Nonnull Class<? extends Activity> sampleActivity) {
        this.captionResId = captionResId;
        this.sampleActivity = sampleActivity;
    }

    @Override
    public OnClickAction getOnClickAction() {
        return new OnClickAction() {
            @Override
            public void onClick(@Nonnull Context context, @Nonnull ListAdapter<? extends ListItem> adapter, @Nonnull ListView listView) {
                context.startActivity(new Intent(context.getApplicationContext(), sampleActivity));
            }
        };
    }

    @Override
    public OnClickAction getOnLongClickAction() {
        // do nothing
        return null;
    }

    @Nonnull
    @Override
    public View updateView(@Nonnull Context context, @Nonnull View view) {
        if ( getTag().equals(view.getTag()) ) {
            fillView((TextView) view);
            return view;
        } else {
            return build(context);
        }
    }

    @Nonnull
    @Override
    public View build(@Nonnull Context context) {
        final TextView textView = TextViewBuilder.newInstance(R.layout.acl_sample_list_item, getTag()).build(context);

        fillView(textView);

        return textView;
    }

    private void fillView(@Nonnull TextView textView) {
        textView.setText(captionResId);
    }

    @Nonnull
    private String getTag() {
        return "sample_type";
    }
}
