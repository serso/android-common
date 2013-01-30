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
import org.solovyev.android.view.DoubleRange;
import org.solovyev.android.view.NumberRange;
import org.solovyev.common.interval.Interval;
import org.solovyev.common.text.Formatter;
import org.solovyev.common.text.NumberIntervalMapper;

import java.text.DecimalFormat;

/**
 * User: serso
 * Date: 8/10/12
 * Time: 1:02 AM
 */
public class DoublePickerDialogPreference extends NumberPickerDialogPreference<Double> {

    @NotNull
    private static final Formatter<Double> simpleFormatter = new DoubleFormatter(2);

    @NotNull
    @Override
    protected Double getDefaultStep() {
        return 1d;
    }

    @NotNull
    @Override
    protected NumberRange<Double> createRange(@NotNull Interval<Double> boundaries, @NotNull Double step, @NotNull Double selected) {
        return DoubleRange.newInstance(boundaries.getLeftLimit(), boundaries.getRightLimit(), step, selected, simpleFormatter);
    }

    public DoublePickerDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs, NumberIntervalMapper.of(Double.class));
    }

    private static class DoubleFormatter implements Formatter<Double> {

        @NotNull
        private final DecimalFormat df;

        private DoubleFormatter(int numberOfDecimalDigits) {
            final StringBuilder sb = new StringBuilder("#.#");
            for (int i = 1; i < numberOfDecimalDigits; i++) {
                sb.append("#");
            }

            this.df = new DecimalFormat(sb.toString());
        }

        @Override
        public String formatValue(@Nullable Double value) throws IllegalArgumentException {

            if (value == null) {
                return "null";
            } else {
                synchronized (df) {
                    return df.format(value);
                }
            }
        }
    }
}
