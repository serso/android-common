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

import android.content.Context;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.solovyev.common.Converter;
import org.solovyev.common.interval.Interval;
import org.solovyev.common.math.NumberValuer;

/**
 * User: serso
 * Date: 9/19/11
 * Time: 4:26 PM
 */
public class NumberRangeSeekBar<T extends Number & Comparable<T>> extends AbstractRangeSeekBar<T> {

	@Nonnull
	private final NumberType numberType;


	public NumberRangeSeekBar(@Nonnull Interval<T> boundaries, @Nullable Integer steps, Context context) throws IllegalArgumentException {
		this(boundaries.getLeftLimit(), boundaries.getRightLimit(), steps, context);
	}

	/**
	 * Creates a new RangeSeekBar.
	 *
	 * @param minValue The minimum value of the selectable range.
	 * @param maxValue The maximum value of the selectable range.
	 * @param steps	number of steps of range
	 * @param context  parent context
	 * @throws IllegalArgumentException Will be thrown if min/max value types are not one of Long, Double, Integer, Float, Short, Byte or BigDecimal.
	 */
	public NumberRangeSeekBar(@Nonnull T minValue, @Nonnull T maxValue, @Nullable Integer steps, Context context) throws IllegalArgumentException {
		super(minValue, maxValue, steps, context);

		numberType = NumberType.fromNumber(minValue);

	}

	@Nonnull
	@Override
	protected Converter<Double, T> getToTConverter() {
		return new Converter<Double, T>() {
			@Nonnull
			@Override
			public T convert(@Nonnull Double value) {
				return (T) numberType.toNumber(value);
			}
		};
	}

	@Nonnull
	@Override
	protected Converter<T, Double> getToDoubleConverter() {
		return new NumberValuer<T>();
	}


}
