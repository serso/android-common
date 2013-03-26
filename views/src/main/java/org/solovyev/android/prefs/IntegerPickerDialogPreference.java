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
import org.solovyev.android.view.IntegerRange;
import org.solovyev.android.view.NumberRange;
import org.solovyev.common.interval.Interval;
import org.solovyev.common.text.NumberIntervalMapper;

/**
 * User: serso
 * Date: 9/26/11
 * Time: 10:31 PM
 */
public class IntegerPickerDialogPreference extends NumberPickerDialogPreference<Integer>{

    public IntegerPickerDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs, NumberIntervalMapper.of(Integer.class));
    }

    @Nonnull
    @Override
    protected Integer getDefaultStep() {
        return 1;
    }

    @Nonnull
    @Override
    protected NumberRange<Integer> createRange(@Nonnull Interval<Integer> boundaries, @Nonnull Integer step, @Nonnull Integer selected) {
        return IntegerRange.newInstance(boundaries.getLeftLimit(), boundaries.getRightLimit(), step, selected);
    }
}
