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
import org.solovyev.common.text.NumberIntervalMapper;

import javax.annotation.Nonnull;

/**
 * User: serso
 * Date: 9/21/11
 * Time: 11:41 PM
 */
public class FloatRangeSeekBarPreference extends RangeSeekBarPreference<Float> {

	public FloatRangeSeekBarPreference(@Nonnull Context context, AttributeSet attrs) {
		super(context, attrs, NumberIntervalMapper.of(Float.class));
	}

	@Nonnull
	@Override
	protected Float getDefaultStep() {
		return 1f;
	}

	@Nonnull
	@Override
	protected Float add(@Nonnull Float l, @Nonnull Float r) {
		return l + r;
	}
}
