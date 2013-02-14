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

package org.solovyev.android.samples.prefs;

import org.solovyev.android.prefs.NumberIntervalPreference;
import org.solovyev.android.prefs.NumberToStringPreference;
import org.solovyev.android.prefs.Preference;
import org.solovyev.android.prefs.StringPreference;
import org.solovyev.common.interval.Interval;
import org.solovyev.common.interval.Intervals;

/**
 * User: serso
 * Date: 8/8/12
 * Time: 3:01 AM
 */
public final class Preferences {
    public static final Preference<Choice> toBeOrNotTobe = StringPreference.ofEnum("to_be_or_not_to_be", Choice.to_be, Choice.class);
    public static final Preference<Integer> integerNumber = NumberToStringPreference.of("integer_number", 5, Integer.class);
    public static final Preference<Double> doubleNumber = NumberToStringPreference.of("double_number", 0.5d, Double.class);
    public static final Preference<Country> country = StringPreference.ofEnum("country", Country.russia, Country.class);
    public static final Preference<Interval<Float>> floatInterval = NumberIntervalPreference.of("float_interval", Intervals.newClosedInterval(35f, 350f), Float.class);
    public static final Preference<Interval<Integer>> integerInterval = NumberIntervalPreference.of("integer_interval", Intervals.newClosedInterval(35, 350), Integer.class);
}
