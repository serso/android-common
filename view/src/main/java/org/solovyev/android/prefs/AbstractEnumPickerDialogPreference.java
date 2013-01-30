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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.android.Labeled;
import org.solovyev.android.LabeledFormatter;
import org.solovyev.android.view.ListRange;
import org.solovyev.android.view.Picker;
import org.solovyev.common.text.EnumMapper;

import java.util.Arrays;

/**
 * User: serso
 * Date: 8/10/12
 * Time: 2:02 AM
 */
public abstract class AbstractEnumPickerDialogPreference<T extends Enum & Labeled> extends AbstractPickerDialogPreference<T> {

    @NotNull
    private final Class<T> enumClass;

    protected AbstractEnumPickerDialogPreference(Context context,
                                                 AttributeSet attrs,
                                                 @Nullable String defaultStringValue,
                                                 boolean needValueText,
                                                 @NotNull Class<T> enumClass) {
        super(context, attrs, defaultStringValue, needValueText, new EnumMapper<T>(enumClass));
        this.enumClass = enumClass;
    }

    @NotNull
    @Override
    protected Picker.Range<T> createRange(@NotNull T selected) {
        return new ListRange<T>(Arrays.asList(enumClass.getEnumConstants()), selected, new LabeledFormatter<T>(getContext()));
    }
}
