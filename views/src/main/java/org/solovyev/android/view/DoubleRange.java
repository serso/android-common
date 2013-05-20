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

import org.solovyev.common.text.Formatter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * User: serso
 * Date: 8/10/12
 * Time: 12:51 AM
 */
public class DoubleRange extends NumberRange<Double> {

	public DoubleRange(@Nonnull Double min,
					   @Nonnull Double max,
					   @Nonnull Double step,
					   int startPosition,
					   @Nullable Formatter<Double> formatter) {
		super(min, max, step, startPosition, formatter);
	}

	@Nonnull
	public static NumberRange<Double> newInstance(@Nonnull Double min,
												  @Nonnull Double max,
												  @Nonnull Double step,
												  @Nonnull Double selected,
												  @Nullable Formatter<Double> formatter) {
		if (selected < min || selected > max) {
			throw new IllegalArgumentException("Selected value: " + selected + " should be >= " + min + " and <= " + max + "!");
		}

		int startPosition = 0;
		for (double i = min; i < selected; i += step) {
			startPosition += 1;
		}

		return new DoubleRange(min, max, step, startPosition, formatter);
	}

	@Override
	protected int getCount(@Nonnull Double min, @Nonnull Double max, @Nonnull Double step) {
		int result = (int) ((max - min) / step);
		return result + 1;
	}

	@Nonnull
	@Override
	protected Double getValueAt(int position, @Nonnull Double min, @Nonnull Double max, @Nonnull Double step) {
		return min + position * step;
	}
}
