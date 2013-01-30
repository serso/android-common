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

package org.solovyev.android.prefs;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.android.view.Picker;
import org.solovyev.common.text.Mapper;

/**
 * User: serso
 * Date: 8/10/12
 * Time: 1:58 AM
 */
public abstract class AbstractPickerDialogPreference<T> extends AbstractDialogPreference<T> implements Picker.OnChangedListener<T> {


    protected AbstractPickerDialogPreference(Context context,
                                             AttributeSet attrs,
                                             @Nullable String defaultStringValue,
                                             boolean needValueText,
                                             @NotNull Mapper<T> mapper) {
        super(context, attrs, defaultStringValue, needValueText, mapper);
    }

    @Override
    protected LinearLayout.LayoutParams getParams() {
        final LinearLayout.LayoutParams result = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        result.gravity = Gravity.CENTER;

        return result;
    }

    @NotNull
    @Override
    protected View createPreferenceView(@NotNull Context context) {
        final Picker<T> result = new Picker<T>(context);

        result.setOnChangeListener(this);

        return result;
    }

    @Override
    protected void initPreferenceView(@NotNull View v, @Nullable T value) {
        if (value != null) {
            ((Picker<T>) v).setRange(createRange(value));
        }
    }

    @NotNull
    protected abstract Picker.Range<T> createRange(@NotNull T selected);

    @Override
    public void onChanged(@NotNull Picker picker, @NotNull T o) {
        persistValue(o);
    }
}
