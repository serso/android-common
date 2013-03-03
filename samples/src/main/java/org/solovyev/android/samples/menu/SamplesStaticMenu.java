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

package org.solovyev.android.samples.menu;

import android.app.Activity;
import android.content.Context;
import android.view.MenuItem;
import android.widget.Toast;
import javax.annotation.Nonnull;
import org.solovyev.android.Activities;
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
        public void onClick(@Nonnull MenuItem data, @Nonnull Context context) {
            Toast.makeText(context, context.getString(R.string.show_text_text), Toast.LENGTH_SHORT).show();
        }
    },

    restart_activity(R.string.restart_activity) {
        @Override
        public void onClick(@Nonnull MenuItem data, @Nonnull Context context) {
            Activities.restartActivity((Activity) context);
        }
    };

    private int captionResId;

    private SamplesStaticMenu(int captionResId) {
        this.captionResId = captionResId;
    }


    @Nonnull
    @Override
    public String getCaption(@Nonnull Context context) {
        return context.getString(captionResId);
    }
}
