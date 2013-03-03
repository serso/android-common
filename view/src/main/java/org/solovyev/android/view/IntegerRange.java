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
 * Date: 8/10/12
 * Time: 12:08 AM
 */
public class IntegerRange extends NumberRange<Integer> {

    public IntegerRange(@Nonnull Integer min,
                        @Nonnull Integer max,
                        @Nonnull Integer step,
                        int startPosition,
                        @Nullable Formatter<Integer> formatter) {
        super(min, max, step, startPosition, formatter);
    }

    @Nonnull
    public static NumberRange<Integer> newInstance(@Nonnull Integer min, @Nonnull Integer max, @Nonnull Integer step, @Nonnull Integer selected) {
        if (selected < min || selected > max) {
            throw new IllegalArgumentException("Selected value: " + selected + " should be >= " + min + " and <= " + max + "!");
        }

        int startPosition = 0;
        for ( int i = min; i < selected; i += step ) {
            startPosition += 1;
        }

        return new IntegerRange(min, max, step, startPosition, null);
    }

    @Override
    protected int getCount(@Nonnull Integer min, @Nonnull Integer max, @Nonnull Integer step) {
        // (4 - 0)/1 + 1= 5
        // (4 - 0)/2 + 1 = 3
        // (4 - 1)/2 + 1 = 2
        return (max - min) / step + 1;
    }

    @Nonnull
    @Override
    protected Integer getValueAt(int position, @Nonnull Integer min, @Nonnull Integer max, @Nonnull Integer step) {
        return min + position * step;
    }
}
