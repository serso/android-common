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
import javax.annotation.Nonnull;
import org.solovyev.android.view.NumberRange;
import org.solovyev.android.view.Picker;
import org.solovyev.common.interval.Interval;
import org.solovyev.common.text.NumberIntervalMapper;

/**
 * User: serso
 * Date: 8/10/12
 * Time: 12:57 AM
 */
public abstract class NumberPickerDialogPreference<N extends Number & Comparable<N>> extends AbstractPickerDialogPreference<N> {

    @Nonnull
    private final Interval<N> boundaries;

    @Nonnull
    private final N step;

    protected NumberPickerDialogPreference(Context context,
                                           AttributeSet attrs,
                                           @Nonnull NumberIntervalMapper<N> mapper) {
        super(context, attrs, null, false, mapper.getMapper());

        //noinspection ConstantConditions
        boundaries = mapper.parseValue(attrs.getAttributeValue(AbstractDialogPreference.localNameSpace, "boundaries"));

        final String stringStep = attrs.getAttributeValue(AbstractDialogPreference.localNameSpace, "step");
        if (stringStep == null) {
            step = getDefaultStep();
        } else {
            step = mapper.getMapper().parseValue(stringStep);
        }
    }

    @Nonnull
    protected abstract N getDefaultStep();

    @Nonnull
    @Override
    protected Picker.Range<N> createRange(@Nonnull N selected) {
        return createRange(boundaries, step, selected);
    }

    @Nonnull
    protected abstract NumberRange<N> createRange(@Nonnull Interval<N> boundaries, @Nonnull N step, @Nonnull N selected);

}
