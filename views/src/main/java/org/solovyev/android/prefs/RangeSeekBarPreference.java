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
import android.view.View;
import android.widget.LinearLayout;
import org.solovyev.android.view.AbstractRangeSeekBar;
import org.solovyev.android.view.NumberRangeSeekBar;
import org.solovyev.common.interval.Interval;
import org.solovyev.common.interval.Intervals;
import org.solovyev.common.text.NumberIntervalMapper;

import javax.annotation.Nonnull;

/**
 * User: serso
 * Date: 9/19/11
 * Time: 12:27 PM
 */
public abstract class RangeSeekBarPreference<T extends Number & Comparable<T>> extends AbstractDialogPreference<Interval<T>> implements AbstractRangeSeekBar.OnRangeSeekBarChangeListener<T> {

	@Nonnull
	private final Interval<T> boundaries;

	@Nonnull
	private final T step;

	public RangeSeekBarPreference(@Nonnull Context context, AttributeSet attrs, @Nonnull NumberIntervalMapper<T> mapper) {
		super(context, attrs, null, true, mapper);

		//noinspection ConstantConditions
		boundaries = mapper.parseValue(attrs.getAttributeValue(AbstractDialogPreference.localNameSpace, "boundaries"));

		final String stepValue = attrs.getAttributeValue(AbstractDialogPreference.localNameSpace, "step");
		if (stepValue == null) {
			step = getDefaultStep();
		} else {
			step = mapper.getMapper().parseValue(stepValue);
		}

	}

	@Nonnull
	protected abstract T getDefaultStep();

	@Nonnull
	protected View createPreferenceView(@Nonnull Context context) {
		int count = 0;
		for (T t = boundaries.getLeftLimit(); t.compareTo(boundaries.getRightLimit()) <= 0; t = add(t, step)) {
			count += 1;
		}
		final NumberRangeSeekBar<T> result = new NumberRangeSeekBar<T>(boundaries, count, context);

		result.setNotifyWhileDragging(true);
		result.setOnRangeSeekBarChangeListener(this);

		return result;
	}

	@Nonnull
	protected abstract T add(@Nonnull T l, @Nonnull T r);

	@Override
	protected LinearLayout.LayoutParams getParams() {
		return null;
	}

	@Override
	protected void initPreferenceView(@Nonnull View v, Interval<T> value) {
		if (value != null) {
			((NumberRangeSeekBar<T>) v).setSelectedMinValue(value.getLeftLimit());
			((NumberRangeSeekBar<T>) v).setSelectedMaxValue(value.getRightLimit());
			setValueText(value);
		}
	}

	@Override
	public void rangeSeekBarValuesChanged(T minValue, T maxValue, boolean changeComplete) {
		final Interval<T> interval = Intervals.newClosedInterval(minValue, maxValue);

		if (changeComplete) {
			persistValue(interval);
		}

		setValueText(interval);
	}

	private void setValueText(@Nonnull Interval<T> interval) {
		final String t = String.valueOf(interval);
		final String valueText = getValueText();
		updateValueText(valueText == null ? t : t.concat(valueText));
	}
}
