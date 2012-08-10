package org.solovyev.android.samples;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import org.jetbrains.annotations.NotNull;
import org.solovyev.android.list.ListAdapter;
import org.solovyev.android.list.ListItem;
import org.solovyev.android.samples.menu.SamplesMenuActivity;
import org.solovyev.android.samples.prefs.SamplesPreferencesActivity;
import org.solovyev.android.view.TextViewBuilder;

/**
 * User: serso
 * Date: 8/8/12
 * Time: 2:06 AM
 */
public enum SampleType implements ListItem<View> {
    preferences(R.string.preferences, SamplesPreferencesActivity.class),
    menu(R.string.menu, SamplesMenuActivity.class);

    private final int captionResId;

    @NotNull
    private final Class<? extends Activity> sampleActivity;

    private SampleType(int captionResId, @NotNull Class<? extends Activity> sampleActivity) {
        this.captionResId = captionResId;
        this.sampleActivity = sampleActivity;
    }

    @Override
    public OnClickAction getOnClickAction() {
        return new OnClickAction() {
            @Override
            public void onClick(@NotNull Context context, @NotNull ListAdapter<ListItem<? extends View>> adapter, @NotNull ListView listView) {
                context.startActivity(new Intent(context.getApplicationContext(), sampleActivity));
            }
        };
    }

    @Override
    public OnClickAction getOnLongClickAction() {
        // do nothing
        return null;
    }

    @NotNull
    @Override
    public View updateView(@NotNull Context context, @NotNull View view) {
        if ( getTag().equals(view.getTag()) ) {
            fillView((TextView) view);
            return view;
        } else {
            return build(context);
        }
    }

    @NotNull
    @Override
    public View build(@NotNull Context context) {
        final TextView textView = TextViewBuilder.newInstance(R.layout.sample_list_item, getTag()).build(context);

        fillView(textView);

        return textView;
    }

    private void fillView(@NotNull TextView textView) {
        textView.setText(captionResId);
    }

    @NotNull
    private String getTag() {
        return "sample_type";
    }
}
