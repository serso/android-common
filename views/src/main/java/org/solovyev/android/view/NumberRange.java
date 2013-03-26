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

package org.solovyev.android.view;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.solovyev.common.text.Formatter;

/**
 * User: serso
 * Date: 8/9/12
 * Time: 11:44 PM
 */
public abstract class NumberRange<N extends Number & Comparable<N>> implements Picker.Range<N> {

    @Nullable
    private Formatter<N> formatter;

    @Nonnull
    private final N min;

    @Nonnull
    private final N max;

    @Nonnull
    private final N step;

    private final int startPosition;

    private int count = -1;

    public NumberRange(@Nonnull N min,
                       @Nonnull N max,
                       @Nonnull N step,
                       int startPosition,
                       @Nullable Formatter<N> formatter) {
        assert min.compareTo(max) <= 0;

        this.min = min;
        this.max = max;
        this.step = step;
        this.startPosition = startPosition;
        this.formatter = formatter;
    }

    @Override
    public int getStartPosition() {
        if ( this.startPosition < getCount() ) {
            return this.startPosition;
        } else {
            return getCount() - 1;
        }
    }

    @Override
    public int getCount() {
        if (count == -1) {
            count = getCount(min, max, step);
        }
        return count;
    }

    protected abstract int getCount(@Nonnull N min, @Nonnull N max, @Nonnull N step);

    @Nonnull
    @Override
    public String getStringValueAt(int position) {
        int count = getCount();
        if (position < 0 || position >= count) {
            throw new IllegalArgumentException("Position " + position + " must be >= 0 and < " + count + "!");
        }

        final N number = getValueAt(position, min, max, step);
        return formatter == null ? number.toString() : formatter.formatValue(number);
    }

    @Nonnull
    @Override
    public N getValueAt(int position) {
        return getValueAt(position, min, max, step);
    }

    @Nonnull
    protected abstract N getValueAt(int position, @Nonnull N min, @Nonnull N max, @Nonnull N step);
}
