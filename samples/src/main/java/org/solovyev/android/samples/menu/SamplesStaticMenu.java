package org.solovyev.android.samples.menu;

import android.app.Activity;
import android.content.Context;
import android.view.MenuItem;
import android.widget.Toast;
import org.jetbrains.annotations.NotNull;
import org.solovyev.android.AndroidUtils;
import org.solovyev.android.menu.LabeledMenuItem;
import org.solovyev.android.samples.R;

/**
 * User: serso
 * Date: 8/10/12
 * Time: 1:18 PM
 */

/**
 * Context independent menu
 */
public enum SamplesStaticMenu implements LabeledMenuItem<MenuItem>  {
    
    show_text(R.string.show_text) {
        @Override
        public void onClick(@NotNull MenuItem data, @NotNull Context context) {
            Toast.makeText(context, context.getString(R.string.show_text_text), Toast.LENGTH_SHORT).show();
        }
    },

    restart_activity(R.string.restart_activity) {
        @Override
        public void onClick(@NotNull MenuItem data, @NotNull Context context) {
            AndroidUtils.restartActivity((Activity) context);
        }
    };

    private int captionResId;

    private SamplesStaticMenu(int captionResId) {
        this.captionResId = captionResId;
    }


    @NotNull
    @Override
    public String getCaption(@NotNull Context context) {
        return context.getString(captionResId);
    }
}
